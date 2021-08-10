package com.zeffon.danzhu.core;

import com.zeffon.danzhu.exception.CreateSuccess;
import com.zeffon.danzhu.exception.DeleteSuccess;
import com.zeffon.danzhu.exception.UpdateSuccess;

/**
 * Create by Zeffon on 2020/10/1
 */
public class UnifyResponse {
    private int code;
    private String message;
    private String request;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getRequest() {
        return request;
    }

    public UnifyResponse(int code, String message, String request) {
        this.code = code;
        this.message = message;
        this.request = request;
    }

    public static void getSuccess(int code) {

    }

    public static void createSuccess(int code) {
        throw new CreateSuccess(code);
    }

    public static void deleteSuccess(int code) {
        throw new DeleteSuccess(code);
    }

    public static void updateSuccess(int code) {
        throw new UpdateSuccess(code);
    }
}
