package com.zeffon.danzhu.core.enumeration;

import lombok.Getter;

/**
 * Create by Zeffon on 2020/10/1
 */
@Getter
public enum LoginType {
    USER_WX(0, "微信登录"),
    USER_EMAIL(1, "邮箱登录"),
    LINK_CODE(2, "链接提取码授权");

    private Integer value;

    LoginType(Integer value, String description) {
        this.value = value;
    }
}
