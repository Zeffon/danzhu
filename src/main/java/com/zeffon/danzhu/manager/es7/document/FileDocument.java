package com.zeffon.danzhu.manager.es7.document;

import com.zeffon.danzhu.model.FileLibrary;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * Create by Zeffon on 2021/3/21
 */
@Getter
@Setter
@NoArgsConstructor
public class FileDocument {
    private Integer id;
    private Integer userId;
    private String title;
    private String url;
    private Integer size;
    private Date createTime;

    public FileDocument(FileLibrary fileLibrary) {
        BeanUtils.copyProperties(fileLibrary, this);
    }
}
