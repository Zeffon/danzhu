package com.zeffon.danzhu.exception.http;

/**
 * Create by Zeffon on 2020/10/1
 */
public class ForbiddenException extends HttpException {
    public ForbiddenException(int code) {
        this.code = code;
        this.httpStatusCode = 403;
    }
}
