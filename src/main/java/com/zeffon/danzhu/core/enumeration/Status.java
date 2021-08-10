package com.zeffon.danzhu.core.enumeration;

import lombok.Getter;

/**
 * Create by Zeffon on 2020/10/1
 */
@Getter
public enum Status implements ValueEnum {

    ALLOW(0, "无需验证"),
    VERIFY(1, "需要验证"),
    REFUSE(2, "不可加入");

    private Integer value;

    Status(Integer value, String description) {
        this.value = value;
    }
}
