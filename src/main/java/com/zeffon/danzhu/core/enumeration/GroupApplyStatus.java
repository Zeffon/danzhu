package com.zeffon.danzhu.core.enumeration;

import lombok.Getter;

/**
 * Create by Zeffon on 2020/10/25
 */
@Getter
public enum GroupApplyStatus implements ValueEnum {
    WAIT(0, "等待"),
    PASS(1, "通过"),
    REFUSE(2, "拒绝"),
    ALL(3, "全部");

    private Integer value;

    GroupApplyStatus(Integer value, String description) {
        this.value = value;
    }
}
