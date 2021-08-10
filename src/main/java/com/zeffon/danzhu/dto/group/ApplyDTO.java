package com.zeffon.danzhu.dto.group;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

/**
 * Create by Zeffon on 2020/10/27
 */
@Getter
@Setter
public class ApplyDTO {
    @Positive
    @NotNull(message = "用户组id不能为空")
    private Integer id;
    @Min(value = 1, message = "状态值不正确")
    @Max(value = 2, message = "状态值不正确")
    private Integer status;
}
