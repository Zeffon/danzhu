package com.zeffon.danzhu.dto.file;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * Create by Zeffon on 2020/11/5
 */
@Getter
@Setter
public class Upload2DTO extends UploadDTO{
    @Positive
    @NotNull
    private Integer uid;
}
