package com.zeffon.danzhu.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Create by Zeffon on 2020/11/3
 */
@Getter
@Setter
public class OssCallbackDTO {
    // 请求的回调地址
    private String callbackUrl;
    // 回调是传入request中的参数
    private String callbackBody;
    // 回调时传入参数格式，比如表单提交形式
    private String callbackBodyType;
}
