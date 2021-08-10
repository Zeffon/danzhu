package com.zeffon.danzhu.core.enumeration;

import lombok.Getter;

/**
 * Create by Zeffon on 2020/10/3
 */
@Getter
public enum LinkPasswordType {
    RANDOM(0, "随机生成"),
    CUSTOMIZED(1, "自定义");

    private Integer value;

    LinkPasswordType(Integer value, String description) {
        this.value = value;
    }
}
