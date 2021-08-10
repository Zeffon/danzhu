package com.zeffon.danzhu.core.interceptors;

import com.auth0.jwt.interfaces.Claim;
import com.zeffon.danzhu.core.LocalUser;
import com.zeffon.danzhu.exception.http.ForbiddenException;
import com.zeffon.danzhu.exception.http.UnAuthenticatedException;
import com.zeffon.danzhu.model.User;
import com.zeffon.danzhu.service.UserService;
import com.zeffon.danzhu.util.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;

/**
 * 有两种方法
 * 1. 实现HandlerInterceptor
 * 2. 继承HandlerInterceptorAdapter
 * Create by Zeffon on 2020/10/1
 */
public class PermissionInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private UserService userService;

    public PermissionInterceptor() {
        super();
    }

    // 1 请求进入controller之前的时候，会回调的函数
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Optional<ScopeLevel> scopeLevel = this.getScopeLevel(handler);
        if (!scopeLevel.isPresent()) {
            // 获取不到controller方法上的注解，说明访问的方法是公开API，直接放行
            return true;
        }
        // 获取不到header中的token
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.isEmpty(bearerToken)) {
            throw new UnAuthenticatedException(10004);
        }
        // 由于令牌前面加上Bearer，如 Authorization : Bearer <token>
        if(!bearerToken.startsWith("Bearer")) {
            // 判断不是以Bearer开头的
            throw new UnAuthenticatedException(10004);
        }

        // 对空令牌情况直接校验登录情况进行处理
        String tokens[] = bearerToken.split(" ");
        if (!(tokens.length == 2)) {
            throw new UnAuthenticatedException(10004);
        }

        // 提取JWT令牌
        String token = tokens[1];
        Optional<Map<String, Claim>> optionalMap = JwtToken.getClaims(token);
        // 空值的情况，说明校验没有通过
        Map<String, Claim> map = optionalMap.orElseThrow(() -> new UnAuthenticatedException(10004));

        // 权限校验
        boolean valid = this.hasPermission(scopeLevel.get(), map);
        if (valid) {
            this.setToThreadLocal(map);
        }
        return valid;
    }

    private void setToThreadLocal(Map<String, Claim> map) {
        Integer uid = map.get("uid").asInt();
        Integer scope = map.get("scope").asInt();
        User user = userService.getUserById(uid);
        LocalUser.set(user, scope);
    }

    // 想用注解的值与传过来的jwt里scope进行比较，判断是否足够权限访问
    private boolean hasPermission(ScopeLevel scopeLevel, Map<String, Claim> map) {
        // 1. 获取注解的值
        Integer level = scopeLevel.value();
        // 获取JWT令牌的scope值
        Integer scope = map.get("scope").asInt();
        if (level > scope) {
            // 没有相应的身份，权限不够(等于的时候是允许访问的)
            throw new ForbiddenException(10005);
        }
        return true;
    }

    // 2 springboot在渲染页面之前，给了你一个修改modelAndView的机会
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    // 3 主要是清理一些资源的(清除当前变量)
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        LocalUser.clear();
        super.afterCompletion(request, response, handler, ex);
    }

    // 获取到controller方法上注解ScopeLevel
    private Optional<ScopeLevel> getScopeLevel(Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod)handler;
            // 1.获取到方法上的注解
            ScopeLevel scopeLevel = handlerMethod.getMethod().getAnnotation(ScopeLevel.class);
            if (scopeLevel == null) {
                return Optional.empty();
            }
            return Optional.of(scopeLevel);
        }
        return Optional.empty();

    }
}
