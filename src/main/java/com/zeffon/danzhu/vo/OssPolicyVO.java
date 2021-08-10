package com.zeffon.danzhu.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * Create by Zeffon on 2020/11/3
 */
@Getter
@Setter
public class OssPolicyVO {

        // 访问身份验证中用到用户标识
        private String accessKeyId;
        // 用户表单上传的策略，经过base64编码过的字符串
        private String policy;
        // 对policy签名后的字符串
        private String signature;
        // 上传文件夹路径前缀
        private String dir;
        // oss对外服务的访问域名
        private String host;
        // 上传成功后的回调设置
        private String callback;
}
