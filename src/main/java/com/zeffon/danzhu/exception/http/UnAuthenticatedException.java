package com.zeffon.danzhu.exception.http;

/**
 * Create by Zeffon on 2020/10/1
 */
public class UnAuthenticatedException extends HttpException {
    public UnAuthenticatedException(int code) {
        this.httpStatusCode = 401;
        this.code = code;
    }
}
