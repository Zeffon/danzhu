package com.zeffon.danzhu.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * Create by Zeffon on 2020/11/9
 */
@Getter
@Setter
public class FileArchiveVO {
    private List<String> months;
    private List<List<FileVO>> files;

    public FileArchiveVO(Map<String, List<FileVO>> map) {
        List<String> months = new ArrayList<>();
        List<List<FileVO>> files = new ArrayList<>();

        List<Map.Entry<String, List<FileVO>>> list = new ArrayList<>(map.entrySet());
        //按key值字符串比较从大到小
        list.sort((o1, o2) -> o2.getKey().compareTo(o1.getKey()));
        for(Map.Entry<String, List<FileVO>> entry : list){
            months.add(entry.getKey());
            files.add(entry.getValue());
        }
        this.months = months;
        this.files = files;
    }
}
