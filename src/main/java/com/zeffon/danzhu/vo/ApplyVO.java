package com.zeffon.danzhu.vo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zeffon.danzhu.bo.WxInfoBO;
import com.zeffon.danzhu.util.GenericAndJson;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * Create by Zeffon on 2020/10/29
 */
@Getter
@Setter
@NoArgsConstructor
public class ApplyVO {
    private Integer id;
    private Integer status;
    private Date createTime;
    private String title;
    private WxInfoBO wxInfo;

    public ApplyVO(Integer id, Integer status, Date createTime, String title, String wxInfo) {
        this.id = id;
        this.status = status;
        this.createTime = createTime;
        this.title = title;
        this.wxInfo = GenericAndJson.jsonToObject(wxInfo, new TypeReference<WxInfoBO>() {});
    }
}
