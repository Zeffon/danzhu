package com.zeffon.danzhu.vo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zeffon.danzhu.bo.RemarkBO;
import com.zeffon.danzhu.bo.WxInfoBO;
import com.zeffon.danzhu.util.GenericAndJson;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Create by Zeffon on 2020/10/26
 */
@Getter
@Setter
public class GroupUserApplyVO {
    private String code;
    private WxInfoBO wxInfo;
    private Integer id;
    private Integer status;
    private Date createTime;

    public GroupUserApplyVO() {}

    public GroupUserApplyVO(String code, String wxInfo, Integer id, Integer status, Date createTime) {
        this.code = code;
        this.wxInfo = GenericAndJson.jsonToObject(wxInfo, new TypeReference<WxInfoBO>() {});
        this.id = id;
        this.status = status;
        this.createTime = createTime;
    }
}
