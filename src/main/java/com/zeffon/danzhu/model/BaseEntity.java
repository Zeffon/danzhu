package com.zeffon.danzhu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * Create by Zeffon on 2020/10/1
 */
@Setter
@Getter
// 标明一个类并非是真实Entity，而是Entity的父类
@MappedSuperclass
public abstract class BaseEntity {

    @Column(insertable = false, updatable = false) // 新增、更新避免null覆盖问题
    private Date createTime;
    @JsonIgnore
    @Column(insertable = false, updatable = false)
    private Date updateTime;
    @JsonIgnore
    private Date deleteTime;
}
