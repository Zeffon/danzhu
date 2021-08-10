package com.zeffon.danzhu.dto.group;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * Create by Zeffon on 2020/10/4
 */
@Getter
@Setter
public class UpdateDTO extends CreateDTO {
    @Positive
    @NotNull
    private Integer id;
}
