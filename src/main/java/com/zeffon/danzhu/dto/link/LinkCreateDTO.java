package com.zeffon.danzhu.dto.link;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * Create by Zeffon on 2020/10/3
 */
@Getter
@Setter
public class LinkCreateDTO {

    @Positive
    @NotNull
    private Integer cid;

    @NotNull
    private Integer status; // 0:永久 1:7天 2:1天

    private String code;
}
