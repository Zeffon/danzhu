package com.zeffon.danzhu.vo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zeffon.danzhu.bo.WxInfoBO;
import com.zeffon.danzhu.util.GenericAndJson;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * Create by Zeffon on 2021/4/3
 */
@Getter
@Setter
@NoArgsConstructor
public class FileUserVO {
    private Integer userId;
    private WxInfoBO wxInfo;
    private Integer fileId;
    private String title;
    private String url;
    private Integer size;
    private Integer category;
    private Date createTime;

    public FileUserVO (Integer userId, String wxInfo, Integer fileId, String title,
                       String url, Integer size, Integer category, Date createTime) {
        this.userId = userId;
        this.wxInfo = GenericAndJson.jsonToObject(wxInfo, new TypeReference<WxInfoBO>() {});
        this.fileId = fileId;
        this.title = title;
        this.url = url;
        this.size = size;
        this.category = category;
        this.createTime = createTime;
    }
}
