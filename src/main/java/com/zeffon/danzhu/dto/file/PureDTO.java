package com.zeffon.danzhu.dto.file;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * Create by Zeffon on 2020/10/8
 */
@Getter
@Setter
public class PureDTO {
    @Positive
    @NotNull
    private Integer id;

    private Boolean online;
}
