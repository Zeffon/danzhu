package com.zeffon.danzhu.bo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Create by Zeffon on 2020/10/4
 */
@Getter
@Setter
public class RemarkBO {
    /**
     * 对于@NotBlank和@NotNull区别
     * 在@NotBlank：字符串不能为null,字符串trim()后也不能等于""
     * 而@NotNull是不能为null，可以是空
     */
    @NotBlank(message = "key不能为空")
    private String key;
    @NotNull(message = "缺少value字段")
    private String value;
}
