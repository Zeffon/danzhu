package com.zeffon.danzhu.service;

import com.zeffon.danzhu.bo.HotWord;
import com.zeffon.danzhu.core.LocalUser;
import com.zeffon.danzhu.core.enumeration.CodeType;
import com.zeffon.danzhu.exception.http.NotFoundException;
import com.zeffon.danzhu.manager.es7.ES7Service;
import com.zeffon.danzhu.model.Groups;
import com.zeffon.danzhu.model.User;
import com.zeffon.danzhu.repository.*;
import com.zeffon.danzhu.util.EnumUtil;
import com.zeffon.danzhu.vo.GroupSearchVO;
import com.zeffon.danzhu.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Zeffon
 * @date 2021/3/18 15:14
 */
@Service
public class SearchService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final FileRepository fileRepository;
    private final StringRedisTemplate redisTemplate;
    private final ES7Service es7Service;

    public SearchService(StringRedisTemplate redisTemplate, UserRepository userRepository, GroupRepository groupRepository, FileRepository fileRepository, ES7Service es7Service) {
        this.redisTemplate = redisTemplate;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.fileRepository = fileRepository;
        this.es7Service = es7Service;
    }

    public Object search(String q) {
        String codeStart = q.substring(0, 1);
        if ("U".equals(codeStart)) {
            return searchUser(q);
        } else if ("G".equals(codeStart)) {
            return searchGroup(q);
        } else {
            this.addHotWord(q);
            return es7Service.getDoc(q);
        }
    }

    /**
     * 设置缓存失效时间，统一为凌晨零点
     * @param hotWord q
     */
    public void addHotWord(String hotWord) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR,1);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.MILLISECOND,0);
        //晚上十二点与当前时间的毫秒差
        long timeOut = (calendar.getTimeInMillis() - System.currentTimeMillis()) / 1000;
        redisTemplate.expire("hotWord", timeOut, TimeUnit.SECONDS);
        // 加入排序set
        redisTemplate.opsForZSet().incrementScore("hotWord", hotWord, 1);
    }

    /**
     * 获取热词前五位
     * @return List<HotWord>
     */
    public List<HotWord> getHotWord() {
        List<HotWord> hotWordList = new ArrayList<>();
        Set<ZSetOperations.TypedTuple<String>> typedTupleSet = redisTemplate.opsForZSet().reverseRangeByScoreWithScores("hotWord",1,100);
        Iterator<ZSetOperations.TypedTuple<String>> iterator = typedTupleSet.iterator();
        int flag = 0;
        while (iterator.hasNext()){
            flag++;
            ZSetOperations.TypedTuple<String> typedTuple = iterator.next();
            String value = typedTuple.getValue();
            int score = (int) Math.ceil(typedTuple.getScore());
            HotWord hotWord = new HotWord(score, value);
            hotWordList.add(hotWord);
            if ( flag >= 5 ) {
                break;
            }
        }
        return hotWordList;
    }

    private GroupSearchVO searchGroup(String code) {
        Integer uid = LocalUser.getUser().getId();
        Groups groups = this.groupRepository.findFirstByCode(code)
                .orElseThrow(() -> new NotFoundException(30002));
        Integer type = this.groupRepository.getRelated(uid, groups.getId());
        return new GroupSearchVO(groups, type);
    }

    private UserVO searchUser(String code) {
        User user = userRepository.findByCode(code)
                .orElseThrow(() -> new NotFoundException(20002));
        Integer fileCount = this.fileRepository.findAllByUserIdAndDeleteTimeIsNullAndOnlineIsTrue(user.getId()).size();
        return new UserVO(user, fileCount);
    }
}
