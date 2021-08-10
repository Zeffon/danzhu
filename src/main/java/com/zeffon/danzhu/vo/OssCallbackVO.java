package com.zeffon.danzhu.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * Create by Zeffon on 2020/11/3
 */
@Getter
@Setter
public class OssCallbackVO {

    // 文件名称
    private String filename;
    // 文件大小
    private String size;
    // 文件的mimeType
    private String  mimeType;
}
