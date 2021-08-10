package com.zeffon.danzhu.vo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zeffon.danzhu.bo.WxInfoBO;
import com.zeffon.danzhu.util.GenericAndJson;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * Create by Zeffon on 2020/11/6
 */
@Getter
@Setter
@NoArgsConstructor
public class CollectUserVO {
    private Integer uid;
    private String code;
    private WxInfoBO wxInfo;
    private Integer cid;
    private Integer status;
    private Date createTime;

    public CollectUserVO(Integer uid, String code, String wxInfo, Integer cid, Date createTime) {
        this.uid = uid;
        this.code = code;
        this.wxInfo = GenericAndJson.jsonToObject(wxInfo, new TypeReference<WxInfoBO>() {});
        this.cid = cid;
        this.createTime = createTime;
    }

    public CollectUserVO(Integer uid, String code, String wxInfo, Integer cid, Integer status, Date createTime) {
        this.uid = uid;
        this.code = code;
        this.wxInfo = GenericAndJson.jsonToObject(wxInfo, new TypeReference<WxInfoBO>() {});
        this.cid = cid;
        this.status = status;
        this.createTime = createTime;
    }
}
