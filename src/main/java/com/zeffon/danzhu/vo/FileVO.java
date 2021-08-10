package com.zeffon.danzhu.vo;

import com.zeffon.danzhu.model.FileLibrary;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Create by Zeffon on 2020/11/7
 */
@Getter
@Setter
@NoArgsConstructor
public class FileVO {
    private Integer id;
    private Integer userId;
    private Integer collectId;
    private String title;
    private String url;
    private Integer size;
    private Integer category;
    private Boolean online;
    private Date createTime;

    public static List<FileVO> getList(List<FileLibrary> libraryList) {
        return libraryList.stream()
                .map(FileVO::new)
                .collect(Collectors.toList());
    }

    public FileVO(FileLibrary fileLibrary) {
        BeanUtils.copyProperties(fileLibrary, this);
    }
}
