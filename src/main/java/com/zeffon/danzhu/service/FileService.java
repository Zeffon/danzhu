package com.zeffon.danzhu.service;

import com.zeffon.danzhu.core.LocalUser;
import com.zeffon.danzhu.dto.file.PureDTO;
import com.zeffon.danzhu.dto.file.Upload2DTO;
import com.zeffon.danzhu.dto.file.UploadDTO;
import com.zeffon.danzhu.exception.http.ForbiddenException;
import com.zeffon.danzhu.exception.http.ParameterException;
import com.zeffon.danzhu.manager.es7.ES7Service;
import com.zeffon.danzhu.manager.es7.ElasticsearchConfig;
import com.zeffon.danzhu.manager.es7.document.FileDocument;
import com.zeffon.danzhu.model.Collect;
import com.zeffon.danzhu.model.FileLibrary;
import com.zeffon.danzhu.model.User;
import com.zeffon.danzhu.model.UserCollect;
import com.zeffon.danzhu.repository.CollectRepository;
import com.zeffon.danzhu.repository.FileRepository;
import com.zeffon.danzhu.repository.UserCollectRepository;
import com.zeffon.danzhu.repository.UserRepository;
import com.zeffon.danzhu.util.PinyinUtils;
import com.zeffon.danzhu.vo.FileCountVO;
import com.zeffon.danzhu.vo.FileUserVO;
import com.zeffon.danzhu.vo.FileVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static java.util.Arrays.asList;

/**
 * Create by Zeffon on 2020/10/8
 */
@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private CollectRepository collectRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserCollectRepository userCollectRepository;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private ES7Service es7Service;

    public static final String DELETE_FILE = "DELETE_FILE_";

    @Transactional
    public void userUploadFile(UploadDTO uploadDTO) {
        Integer uid = LocalUser.getUser().getId();
        this.saveFile(uid, uploadDTO.getCid(), uploadDTO.getTitle(), uploadDTO.getUrl(), uploadDTO.getSize(), uploadDTO.getCategory());
    }

    @Transactional
    public void adminUploadFile(Upload2DTO upload2DTO) {
        Integer uid = LocalUser.getUser().getId();
        this.collectRepository.findFirstByUserIdAndId(uid, upload2DTO.getCid()).orElseThrow(() -> new ForbiddenException(40006));

        this.saveFile(upload2DTO.getUid(), upload2DTO.getCid(),
                upload2DTO.getTitle(), upload2DTO.getUrl(), upload2DTO.getSize(), upload2DTO.getCategory());
    }

    public List<FileLibrary> listFile(Integer uid, Integer cid) {
        return this.fileRepository.findAllByUserIdAndCollectIdAndDeleteTimeIsNull(uid, cid);
    }

    public void adminDeleteFile(Integer fid) {
        FileLibrary fileLibrary = this.fileRepository.findById(fid).orElseThrow(() -> new ParameterException(50001));
        this.hasPermissionToCollect(fileLibrary.getCollectId());
        fileLibrary.setDeleteTime(new Date());
        this.fileRepository.save(fileLibrary);
        // redis主键回填 将文件id记录 设置7天删除
        this.redisTemplate.opsForValue().set(DELETE_FILE + fid, "1", 7*24*60*60, TimeUnit.SECONDS);
    }

    public Page<FileLibrary> listMySendFile(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createTime").descending());
        Integer uid = LocalUser.getUser().getId();
        return fileRepository.findAllByUserIdAndDeleteTimeIsNull(uid, pageable);
    }

    public LinkedHashMap<String, List<FileVO>> listMonthFile() {
        Integer uid = LocalUser.getUser().getId();
        List<FileLibrary> list = fileRepository.findAllByUserIdAndDeleteTimeIsNull(uid);
        DateFormat sdf = new SimpleDateFormat("yyyyMM");
        LinkedHashMap<String, List<FileVO>> map = new LinkedHashMap<>();
        list.forEach(item -> {
            String month = sdf.format(item.getCreateTime().getTime());
            if(!map.containsKey(month)){
                map.put(month, new ArrayList<>());
            }
            map.get(month).add(new FileVO(item));
        });
        return map;
    }


    public LinkedHashMap<String, List<FileVO>> listLetterFile() {
        Integer uid = LocalUser.getUser().getId();
        List<FileLibrary> list = fileRepository.findAllByUserIdAndDeleteTimeIsNull(uid);
        LinkedHashMap<String, List<FileVO>> map = new LinkedHashMap<>();
        List<String> letters = asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");
        list.forEach(item -> {
            String title = item.getTitle();
            String letter = PinyinUtils.toPinYinUppercaseInitials(title);
            if (!letters.contains(letter)) {
                letter = "#";
            }
            if(!map.containsKey(letter)){
                map.put(letter, new ArrayList<>());
            }
            map.get(letter).add(new FileVO(item));
        });
        return map;
    }

    public Map listFileByCid(Integer cid) {
        User user;
        Map<String, Object> map = new HashMap<>();
        if (cid == 0) {
            user = LocalUser.getUser();
        } else {
            user = this.userRepository.findOneByCollectId(cid);
            Collect collect = this.collectRepository.getOne(cid);
            map.put("endTime", collect.getEndTime());
        }
        List<FileLibrary> list = this.fileRepository.findAllByUserIdAndCollectIdAndDeleteTimeIsNull(user.getId(), cid);
        map.put("cid", cid);
        map.put("uid", user.getId());
        map.put("avatar", user.getWxInfo().getAvatarUrl());
        map.put("nickName", user.getWxInfo().getNickName());
        map.put("files", FileVO.getList(list));
        return map;
    }

    public List<FileUserVO> listAllFileByCid(Integer cid) {
        // 获取收集夹内的文件(考虑根据用户分组，上传时间排序，排除已删除的)
        return this.fileRepository.listAllFileByCid(cid);
    }

    @Transactional
    public void updateOnline(PureDTO pureDTO) {
        Integer uid = LocalUser.getUser().getId();
        FileLibrary fileLibrary = this.fileRepository.findFirstByIdAndUserId(pureDTO.getId(), uid);
        fileLibrary.setOnline(pureDTO.getOnline());
        fileRepository.save(fileLibrary);
        if (pureDTO.getOnline()) {
            // 添加文档
            FileDocument fileDocument = new FileDocument(fileLibrary);
            es7Service.addDoc(fileDocument);
        } else {
            // 删除文档
            es7Service.deleteDoc(String.valueOf(fileLibrary.getId()));
        }
    }

    public Page<FileLibrary> pageFileInTrash(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("deleteTime").descending());
        Integer uid = LocalUser.getUser().getId();
        return this.fileRepository.findAllByUserIdAndDeleteTimeIsNotNull(uid, pageable);
    }

    public List<FileLibrary> listFileInTrash() {
        Integer uid = LocalUser.getUser().getId();
        return this.fileRepository.findAllByUserIdAndDeleteTimeIsNotNull(uid);
    }

    public FileCountVO countFile() {
        Integer uid = LocalUser.getUser().getId();
        Integer trashCount = this.fileRepository.findAllByUserIdAndDeleteTimeIsNotNull(uid).size();
        Integer fileCount = this.fileRepository.findAllByUserIdAndDeleteTimeIsNull(uid).size();
        return new FileCountVO(fileCount, trashCount);
    }

    @Transactional
    public void restoreFile(Integer fid) {
        Integer uid = LocalUser.getUser().getId();
        this.fileRepository.restoreFile(fid, uid);
        // 删除redis中key 避免七天后删除
        this.redisTemplate.delete(DELETE_FILE + fid);
    }

    public void realDeleteFile(Integer fid) {
        Integer uid = LocalUser.getUser().getId();
        this.fileRepository.realDeleteFile(fid, uid);
    }

    public void realDeleteFile2(Integer fid) {
        this.fileRepository.realDeleteFile2(fid);
    }

    public List<FileLibrary> listShareFileByUserId(Integer uid) {
        return this.fileRepository.findAllByUserIdAndDeleteTimeIsNullAndOnlineIsTrue(uid);
    }

    private void saveFile(Integer uid, Integer cid, String title, String url, Integer size, Integer category) {
        UserCollect userCollect = this.userCollectRepository.findFirstByUserIdAndCollectId(uid, cid)
                .orElseThrow(() -> new ParameterException(40004));
        if (userCollect.getStatus() == 0) {
            userCollect.setStatus(1);
            this.userCollectRepository.save(userCollect);
        }
        if (userCollect.getStatus() == 2) {
            userCollect.setStatus(3);
            this.userCollectRepository.save(userCollect);
        }

        FileLibrary fileLibrary = FileLibrary.builder()
                .userId(uid)
                .collectId(cid)
                .title(title)
                .url(url)
                .size(size)
                .category(category)
                .online(false)
                .build();
        this.fileRepository.save(fileLibrary);
    }

    private Collect hasPermissionToCollect(Integer collectId) {
        Integer uid = LocalUser.getUser().getId();
        return this.collectRepository.findFirstByUserIdAndId(uid, collectId)
                .orElseThrow(() -> new ForbiddenException(40003));
    }
}
