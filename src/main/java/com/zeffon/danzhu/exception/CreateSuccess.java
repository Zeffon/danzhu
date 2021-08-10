package com.zeffon.danzhu.exception;

import com.zeffon.danzhu.exception.http.HttpException;

/**
 * Create by Zeffon on 2020/10/1
 */
public class CreateSuccess extends HttpException {
    public CreateSuccess(int code) {
        this.code = code;
        this.httpStatusCode = 201;
    }
    // Create: 201
    // Get: 200
    // Put: 200
    // Delete: 204(客户端接受不到信息的) 200
}
