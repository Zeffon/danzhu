package com.zeffon.danzhu.dto.group;

import com.zeffon.danzhu.dto.RemarkDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * Create by Zeffon on 2020/10/2
 */
@Getter
@Setter
public class AddDTO extends RemarkDTO {
    @Positive
    @NotNull(message = "用户组id不能为空")
    private Integer gid;

    @NotBlank(message = "code用户编码不能为空")
    private String code;
}
