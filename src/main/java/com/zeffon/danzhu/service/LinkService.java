package com.zeffon.danzhu.service;

import com.zeffon.danzhu.core.LocalUser;
import com.zeffon.danzhu.core.enumeration.LinkPasswordType;
import com.zeffon.danzhu.core.enumeration.LinkValidPeriod;
import com.zeffon.danzhu.dto.link.LinkCreateDTO;
import com.zeffon.danzhu.exception.http.ForbiddenException;
import com.zeffon.danzhu.exception.http.NotFoundException;
import com.zeffon.danzhu.exception.http.ParameterException;
import com.zeffon.danzhu.model.Collect;
import com.zeffon.danzhu.model.Link;
import com.zeffon.danzhu.model.User;
import com.zeffon.danzhu.repository.CollectRepository;
import com.zeffon.danzhu.repository.LinkRepository;
import com.zeffon.danzhu.util.CodeUtil;
import com.zeffon.danzhu.util.CommonUtil;
import com.zeffon.danzhu.vo.FileUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Create by Zeffon on 2020/10/3
 */
@Service
public class LinkService {
    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private CollectRepository collectRepository;
    @Autowired
    private FileService fileService;
    @Autowired
    private UserService userService;

    @Value("${danzhu.link-url}")
    private String linkUrl;

    public Link getByCode(String code) {
        Link link = linkRepository.findOneByCode(code);
        if (link == null) {
            throw new ParameterException(60002);
        }
        if (link.getEndTime() != null && link.getEndTime().before(new Date())) {
            throw new ParameterException(60001);
        }
        return link;
    }

    public Link getByCodeAndUserId(String code, Integer userId) {
        Link link = linkRepository.findOneByCodeAndUserId(code, userId);
        if (link == null) {
            throw new ParameterException(60002);
        }
        if (link.getEndTime() != null && link.getEndTime().before(new Date())) {
            throw new ParameterException(60001);
        }
        return link;
    }

    public User valid(String code) {
        Link link = this.getByCode(code);
        return this.userService.getUserById(link.getUserId());
    }

    public User getShareUser() {
        return LocalUser.getUser();
    }

    public List<FileUserVO> list(String code) {
        Integer userId = LocalUser.getUser().getId();
        Link link = this.getByCodeAndUserId(code, userId);
        return fileService.listAllFileByCid(link.getCollectId());
    }

    @Transactional
    public Link create(LinkCreateDTO createDTO) {
        // 1.判断是否有权限操作该收集夹
        Collect collect = hasPermissionToCollect(createDTO.getCid());
        // 2.根据密码类型来决定密码
        String password;
        int type;
        if (createDTO.getCode().length() > 0) {
            password = createDTO.getCode();
            type = 1;
        } else {
            password = CodeUtil.getRandomString(4);
            type = 0;
        }

        // 3.根据有效期来决定endTime(永久则null)
        Date endTime = null;
        Calendar now = Calendar.getInstance(); // 当前时间
        if (createDTO.getStatus().equals(LinkValidPeriod.SEVEN_DAY.getValue())) {
            endTime = CommonUtil.addSomeDays(now, 7).getTime();
        } else if(createDTO.getStatus().equals(LinkValidPeriod.ONE_DAY.getValue())) {
            endTime = CommonUtil.addSomeDays(now, 1).getTime();
        }
        // 4.生成特定的url
        String code = CodeUtil.markLinkCode();
        String url = linkUrl + code;
        // 5.数据保存
        Link linkNew = Link.builder()
                .collectId(collect.getId())
                .passwordType(type)
                .password(password)
                .code(code)
                .url(url)
                .endTime(endTime)
                .userId(collect.getUserId())
                .validPeriod(createDTO.getStatus())
                .build();
        this.linkRepository.save(linkNew);
        return linkNew;
    }

    private Collect hasPermissionToCollect(Integer cid) {
        Integer uid = LocalUser.getUser().getId();
        return this.collectRepository.findFirstByUserIdAndId(uid, cid)
                .orElseThrow(() -> new ForbiddenException(40003));
    }

}
