package com.zeffon.danzhu.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Create by Zeffon on 2020/10/17
 */
@Getter
@Setter
public class WxInfoBO {
    private String city;
    private Integer gender;
    private String country;
    private String language;
    @JsonProperty("nickName")
    private String nickName;
    private String province;
    @JsonProperty("avatarUrl")
    private String avatarUrl;
}
