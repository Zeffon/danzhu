/*
 * @Description: 
 * @Author: Zeffon
 * @Date: 2021-08-27 16:19:50
 * @LastEditors: Zeffon
 * @LastEditTime: 2021-08-27 16:21:25
 */
package com.zeffon.danzhu.exception.http;

/**
 * Create by Zeffon on 2020/10/1
 */
public class ParameterException extends HttpException {
    public ParameterException(int code) {
        this.httpStatusCode = 400;
        this.code = code;
    }
    public ParameterException(String message) {
        this.httpStatusCode = 400;
        this.message = message;
    }
}
