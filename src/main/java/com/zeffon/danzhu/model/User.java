package com.zeffon.danzhu.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zeffon.danzhu.bo.WxInfoBO;
import com.zeffon.danzhu.util.GenericAndJson;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Create by Zeffon on 2020/10/1
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Setter
@Getter
@Builder
@Where(clause = "delete_time is null")
public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    private String openid;
    private String name;
    private String email;
    private String mobile;
    private String password;
    private String code;

    private String wxInfo;

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
