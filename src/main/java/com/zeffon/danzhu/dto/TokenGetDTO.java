package com.zeffon.danzhu.dto;

import com.zeffon.danzhu.core.enumeration.LoginType;
import com.zeffon.danzhu.dto.validators.TokenPassword;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * Create by Zeffon on 2020/10/1
 */
@Getter
@Setter
public class TokenGetDTO {
    @NotBlank(message = "account不允许为空")
    private String account;
    @TokenPassword(max=30, message="{token.password}")
    private String password;

    private LoginType type;

}
