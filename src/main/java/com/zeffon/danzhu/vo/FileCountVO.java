package com.zeffon.danzhu.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * Create by Zeffon on 2020/11/9
 */
@Getter
@Setter
public class FileCountVO {
    private Integer fileCount;
    private Integer trashCount;

    public FileCountVO(Integer fileCount, Integer trashCount) {
        this.fileCount = fileCount;
        this.trashCount = trashCount;
    }
}
