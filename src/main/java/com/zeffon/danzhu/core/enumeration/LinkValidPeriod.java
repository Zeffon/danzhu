package com.zeffon.danzhu.core.enumeration;

import lombok.Getter;

/**
 * Create by Zeffon on 2020/10/3
 */
@Getter
public enum LinkValidPeriod {
    PERMANENT(0, "永久"),
    ONE_DAY(1, "1天有效期"),
    SEVEN_DAY(7, "7天有效期");

    private Integer value;

    LinkValidPeriod(Integer value, String description) {
        this.value = value;
    }
}
