package com.zeffon.danzhu.core.configuration;

import com.zeffon.danzhu.core.interceptors.PermissionInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 注册拦截器
 * Create by Zeffon on 2020/10/1
 */
@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {

    /**
     * 在PermissionInterceptor类上@Component加入到IoC容器中很可能跟
     * registry.addInterceptor(new PermissionInterceptor())并不是同一个对象
     * 所以应该是将new PermissionInterceptor()的对象加入到IoC容器中，
     * 故巧妙地采用@Configuration和@Bean的形式将以实例化的PermissionInterceptor()加入到IoC容器中
     */
    @Bean
    public HandlerInterceptor getPermissionInterceptor() {
        return new PermissionInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 可以注册多个拦截器
        registry.addInterceptor(this.getPermissionInterceptor());
    }
}
