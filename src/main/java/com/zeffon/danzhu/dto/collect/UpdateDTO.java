package com.zeffon.danzhu.dto.collect;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * Create by Zeffon on 2020/10/7
 */
@Getter
@Setter
public class UpdateDTO extends CreateDTO {
    @Positive
    @NotNull
    private Integer cid;
}
