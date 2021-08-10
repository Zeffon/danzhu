package com.zeffon.danzhu.exception;

import com.zeffon.danzhu.exception.http.HttpException;

/**
 * Create by Zeffon on 2020/10/1
 */
public class UpdateSuccess extends HttpException {
    public UpdateSuccess(int code) {
        this.code = code;
        this.httpStatusCode = 200;
    }
}
