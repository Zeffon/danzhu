package com.zeffon.danzhu.dto.collect;

import com.zeffon.danzhu.dto.RemarkDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * Create by Zeffon on 2020/10/7
 */
@Getter
@Setter
public class JoinDTO extends RemarkDTO {
    @Positive
    @NotNull
    private Integer id;
}
