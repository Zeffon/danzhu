package com.zeffon.danzhu.dto.group;

import com.zeffon.danzhu.dto.RemarkDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Create by Zeffon on 2020/10/1
 */
@Getter
@Setter
public class CreateDTO extends RemarkDTO {
    @NotBlank(message = "title不允许为空")
    private String title;

    private String summary;

    @NotNull(message = "status不能为空")
    private Integer status;
}
