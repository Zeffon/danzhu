package com.zeffon.danzhu.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zeffon.danzhu.bo.RemarkBO;
import com.zeffon.danzhu.util.GenericAndJson;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Create by Zeffon on 2020/10/1
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "delete_time is null")
public class UserGroup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    private Integer userId;
    private Integer groupId;
    private String remark;

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
