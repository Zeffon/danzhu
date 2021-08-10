package com.zeffon.danzhu.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zeffon.danzhu.bo.RemarkBO;
import com.zeffon.danzhu.util.GenericAndJson;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
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
public class Groups extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    private Integer userId;
    private String code;
    private String title;
    private String summary;
    private Integer status;
    private Integer userNumber;

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
