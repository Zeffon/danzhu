package com.zeffon.danzhu.core.enumeration;

import lombok.Getter;

/**
 * Create by Zeffon on 2020/10/17
 */
@Getter
public enum GroupType implements ValueEnum {
    CREATE(3, "自己创建"),
    JOIN(2, "加入的"),
    OTHER(1, "其他人的，与自己无关");

    private Integer value;

    GroupType(Integer value, String description) {
        this.value = value;
    }

}
