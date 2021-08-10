package com.zeffon.danzhu.vo;

import com.zeffon.danzhu.model.Link;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * Create by Zeffon on 2020/11/15
 */
@Getter
@Setter
public class LinkVO {
    private Integer id;
    private Integer collectId;
    private Integer userId;
    private String code;
    private String url;
    private String password;
    private Integer validPeriod;
    private Date endTime;

    public LinkVO(Link link) {
        BeanUtils.copyProperties(link, this);
    }
}
