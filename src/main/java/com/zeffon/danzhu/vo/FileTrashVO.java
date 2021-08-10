package com.zeffon.danzhu.vo;

import com.zeffon.danzhu.model.FileLibrary;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Create by Zeffon on 2020/11/9
 */
@Getter
@Setter
public class FileTrashVO {
    private Integer id;
    private String title;
    private String url;
    private Integer size;
    private Integer category;
    private Date deleteTime;

    public static List<FileTrashVO> getList(List<FileLibrary> libraryList) {
        return libraryList.stream()
                .map(FileTrashVO::new)
                .collect(Collectors.toList());
    }

    public FileTrashVO(FileLibrary fileLibrary) {
        BeanUtils.copyProperties(fileLibrary, this);
    }

    // f.id, f.title, f.url, f.size, f.category, f.deleteTime
}
