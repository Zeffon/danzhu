package com.zeffon.danzhu.dto.collect;

import com.zeffon.danzhu.dto.RemarkDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * Create by Zeffon on 2020/10/3
 */
@Getter
@Setter
public class AddDTO extends RemarkDTO {
    @Positive
    @NotNull(message = "收集夹id不能为空")
    private Integer cid;

    @NotBlank(message = "code用户编码不能为空")
    private String code;
}
