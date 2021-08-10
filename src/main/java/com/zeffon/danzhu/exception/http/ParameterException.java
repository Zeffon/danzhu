package com.zeffon.danzhu.exception.http;

/**
 * Create by Zeffon on 2020/10/1
 */
public class ParameterException extends HttpException {
    public ParameterException(int code) {
        this.httpStatusCode = 400;
        this.code = code;
    }
}
