package com.zeffon.danzhu.dto.group;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * Create by Zeffon on 2020/10/4
 */
@Getter
@Setter
public class PureDTO {
    @Positive
    @NotNull
    private Integer id;

    @NotBlank(message = "code编号不可为空")
    private String code;
}
