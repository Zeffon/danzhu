package com.zeffon.danzhu.exception;

import com.zeffon.danzhu.exception.http.HttpException;

/**
 * Create by Zeffon on 2020/10/1
 */
public class DeleteSuccess extends HttpException {
    public DeleteSuccess(int code) {
        this.code = code;
        this.httpStatusCode = 200;
    }
}
