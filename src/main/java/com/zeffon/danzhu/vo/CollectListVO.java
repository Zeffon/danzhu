package com.zeffon.danzhu.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Create by Zeffon on 2020/10/30
 */
@Getter
@Setter
public class CollectListVO {
    private Integer id;
    private String title;
    private String code;
    private Integer userLimit;
    private Integer userNumber;
    private Date createTime;
    private Date endTime;
}
