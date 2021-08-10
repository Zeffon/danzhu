package com.zeffon.danzhu.vo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zeffon.danzhu.bo.RemarkBO;
import com.zeffon.danzhu.model.Groups;
import com.zeffon.danzhu.util.GenericAndJson;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;

/**
 * Create by Zeffon on 2020/10/1
 */
@Getter
@Setter
public class GroupVO {
    private Integer id;
    private String code;
    private String title;
    private String summary;
    private Integer status;
    private Integer userNumber;
    private Date createTime;
    private String remark;

    public GroupVO() {

    }

    public GroupVO(Groups groups) {
        BeanUtils.copyProperties(groups, this);
        this.createTime = groups.getCreateTime();
    }

    public List<RemarkBO> getRemark() {
        return GenericAndJson.jsonToObject(this.remark,
                new TypeReference<List<RemarkBO>>() {});
    }

    public void setRemark(List<RemarkBO> remark) {
        if (remark == null) {
            return;
        }
        this.remark = GenericAndJson.objectToJson(remark);
    }
}
