package com.zeffon.danzhu.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Create by Zeffon on 2020/10/1
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Link extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    private Integer collectId;
    private Integer userId;
    private String code;
    private String url;
    private Integer passwordType;
    private String password;
    private Integer validPeriod;
    private Date endTime;
}
