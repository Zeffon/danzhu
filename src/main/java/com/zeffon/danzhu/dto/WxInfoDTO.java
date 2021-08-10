package com.zeffon.danzhu.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * Create by Zeffon on 2020/10/30
 */
@Getter
@Setter
public class WxInfoDTO {
    private String city;
    private Integer gender;
    private String country;
    private String language;
    private String nickName;
    private String province;
    private String avatarUrl;
}
