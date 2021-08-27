package com.zeffon.danzhu.exception.http;

import lombok.Getter;

/**
 * Create by Zeffon on 2020/10/1
 */
@Getter
public class HttpException extends RuntimeException {
    protected Integer code;
    protected String message;
    protected Integer httpStatusCode = 500;

}
