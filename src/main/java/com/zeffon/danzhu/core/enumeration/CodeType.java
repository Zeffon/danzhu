package com.zeffon.danzhu.core.enumeration;

import lombok.Getter;

/**
 * Create by Zeffon on 2020/10/4
 */
@Getter
public enum CodeType implements ValueEnum {
    USER_CODE("U", "用户编号"),
    GROUP_CODE("G", "用户组编号"),
    COLLECT_CODE("C", "收集夹编号"),
    LINK_CODE("L", "分享链接编号");

    private String value;

    CodeType(String value, String description) {
        this.value = value;
    }
}
