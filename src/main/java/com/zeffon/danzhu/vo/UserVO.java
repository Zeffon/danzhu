package com.zeffon.danzhu.vo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zeffon.danzhu.bo.WxInfoBO;
import com.zeffon.danzhu.model.User;
import com.zeffon.danzhu.util.GenericAndJson;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

/**
 * Create by Zeffon on 2020/10/4
 */
@Getter
@Setter
public class UserVO {

    private Integer id;
    private String name;
    private String code;
    private String wxInfo;
    private Integer fileCount;

    public UserVO(User user) {
        BeanUtils.copyProperties(user, this);
    }

    public UserVO(User user, Integer fileCount) {
        BeanUtils.copyProperties(user, this);
        this.fileCount = fileCount;
    }

    public WxInfoBO getWxInfo() {
        return GenericAndJson.jsonToObject(this.wxInfo,
                new TypeReference<WxInfoBO>() {});
    }

    public void setWxInfo(WxInfoBO wxInfo) {
        if (wxInfo == null) {
            return;
        }
        this.wxInfo = GenericAndJson.objectToJson(wxInfo);
    }
}
