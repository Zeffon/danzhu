package com.zeffon.danzhu.core.enumeration;

import lombok.Getter;

/**
 * 收集夹统计分类
 * Create by Zeffon on 2020/11/21
 */
@Getter
public enum StatCategory implements ValueEnum {
    BAR(1, "用户提交情况"),
    GAUGE(2, "用户组收集进度"),
    LINE(3, "提交时间统计"),
    PIE(4, "提交数量对比");

    private Integer value;

    StatCategory(Integer value, String description) {
        this.value = value;
    }

}
