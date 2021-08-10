package com.zeffon.danzhu.dto.validators;

import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Create by Zeffon on 2020/10/1
 */
public class TokenPasswordValidator implements ConstraintValidator<TokenPassword, String> {

    private int min;
    private int max;

    @Override
    public void initialize(TokenPassword constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        // 这里是对小程序密码的判空，因为小程序没有密码，因此用户传过来时，密码为空时正确的
        // 如果是其他平台的登录方式需要额外判断
        if (StringUtils.isEmpty(s)) {
            return true;
        }
        return s.length() >= this.min && s.length() <= this.max;
    }
}
