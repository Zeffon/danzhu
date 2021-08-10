package com.zeffon.danzhu.util;

import org.springframework.web.client.RestTemplate;

/**
 * Create by Zeffon on 2020/11/28
 */
public class RestTemplateUtil {
    public static RestTemplate getInstance() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new WxMappingJackson2HttpMessageConverter());
        return restTemplate;
    }
}
