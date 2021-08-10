package com.zeffon.danzhu.vo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zeffon.danzhu.bo.RemarkBO;
import com.zeffon.danzhu.bo.WxInfoBO;
import com.zeffon.danzhu.util.GenericAndJson;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * Create by Zeffon on 2020/10/17
 */
@Getter
@Setter
public class GroupUserVO {
    private Integer id;
    private Integer uid;
    private String code;
    private WxInfoBO wxInfo;
    private List<RemarkBO> remark;
    private Date createTime;

    public GroupUserVO() {}

    public GroupUserVO(Integer uid, String code, String wxInfo, Integer id, String remark, Date createTime) {
        this.uid = uid;
        this.code = code;
        this.wxInfo = GenericAndJson.jsonToObject(wxInfo, new TypeReference<WxInfoBO>() {});
        this.id = id;
        this.remark = GenericAndJson.jsonToObject(remark, new TypeReference<List<RemarkBO>>() {});
        this.createTime = createTime;
    }

}
