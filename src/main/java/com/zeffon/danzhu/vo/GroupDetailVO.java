package com.zeffon.danzhu.vo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zeffon.danzhu.bo.RemarkBO;
import com.zeffon.danzhu.bo.WxInfoBO;
import com.zeffon.danzhu.util.GenericAndJson;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * Create by Zeffon on 2020/10/26
 */
@Getter
@Setter
@NoArgsConstructor
public class GroupDetailVO {
    private Integer id;
    private String title;
    private Integer status;
    private String summary;
    private Integer userNumber;
    private List<RemarkBO> remark;
    private Date createTime;
    private String code;
    private WxInfoBO wxInfo;


    public GroupDetailVO (Integer id, String code, String title, Integer status, String summary, Integer userNumber, String remark, Date createTime, String wxInfo) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.summary = summary;
        this.userNumber = userNumber;
        this.remark = GenericAndJson.jsonToObject(remark, new TypeReference<List<RemarkBO>>() {});
        this.createTime = createTime;
        this.code = code;
        this.wxInfo = GenericAndJson.jsonToObject(wxInfo, new TypeReference<WxInfoBO>() {});

    }
}
