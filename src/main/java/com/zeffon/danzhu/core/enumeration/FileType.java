package com.zeffon.danzhu.core.enumeration;

import lombok.Getter;

/**
 * Create by Zeffon on 2020/11/22
 */
@Getter
public enum FileType implements ValueEnum {
    NO_GROUP_ING(0, "非用户组，未上传文件"),
    NO_GROUP_ED(1, "非用户组，已上传文件"),
    YES_GROUP_ING(2, "用户组，未上传文件"),
    YES_GROUP_ED(3, "用户组，已上传文件");

    private Integer value;

    FileType(Integer value, String description) {
        this.value = value;
    }
}
