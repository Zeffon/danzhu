package com.zeffon.danzhu.exception.http;

/**
 * Create by Zeffon on 2020/10/1
 */
public class NotFoundException extends HttpException {
    public NotFoundException(int code) {
        this.httpStatusCode = 404;
        this.code = code;
    }
}
