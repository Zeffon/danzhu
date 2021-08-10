package com.zeffon.danzhu.core.configuration;

import com.aliyun.oss.OSSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Create by Zeffon on 2020/11/3
 */
@Configuration
public class OssConfiguration {
    @Value("${aliyun.oss.endpoint}")
    private String ENDPOINT;
    @Value("${aliyun.oss.accessKeyId}")
    private String ACCESS_KEY_ID;
    @Value("${aliyun.oss.accessKeySecret}")
    private String ACCESS_KEY_SECRET;

    @Bean
    public OSSClient ossClient(){
        return new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
    }
}
