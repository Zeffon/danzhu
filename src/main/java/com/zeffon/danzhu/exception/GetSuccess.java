package com.zeffon.danzhu.exception;

import com.zeffon.danzhu.exception.http.HttpException;

/**
 * Create by Zeffon on 2020/10/20
 */
public class GetSuccess extends HttpException {
    public GetSuccess(int code) {
        this.code = code;
        this.httpStatusCode = 200;
    }
}
