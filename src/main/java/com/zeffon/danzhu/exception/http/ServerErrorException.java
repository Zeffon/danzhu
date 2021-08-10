package com.zeffon.danzhu.exception.http;

/**
 * Create by Zeffon on 2020/10/1
 */
public class ServerErrorException extends HttpException {
    public ServerErrorException(int code) {
        this.code = code;
        this.httpStatusCode = 500;
    }
}
