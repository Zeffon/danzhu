package com.zeffon.danzhu.core;

import com.zeffon.danzhu.model.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Create by Zeffon on 2020/10/1
 */
public class LocalUser {

    /**
     * 静态变量只能保存数据，是不能保存状态。因为一个静态变量约等于全局共享的变量
     * 那么对于一个全局共享的变量在Web环境中存在：可以同时接收API请求，不能保证静态变量保存哪个用户的User
     */

    private static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<>();

    /** 拦截器preHandle进行处理，保存用户信息 */
    public static void set(User user, Integer scope) {
//        LocalUser.user = user;
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("scope", scope);
        LocalUser.threadLocal.set(map);
    }

    /** 拦截器afterCompletion 释放线程 */
    public static void clear() {
        LocalUser.threadLocal.remove();
    }

    /** 在这线程中随意获取保存的用户信息 */
    public static User getUser() {
        Map<String, Object> map = LocalUser.threadLocal.get();
        User user = (User) map.get("user");
        return user;
    }

    public static Integer getScope() {
        Map<String, Object> map = LocalUser.threadLocal.get();
        Integer scope = (Integer) map.get("scope");
        return scope;
    }

}
