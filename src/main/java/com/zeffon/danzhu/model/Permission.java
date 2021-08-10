package com.zeffon.danzhu.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Create by Zeffon on 2020/11/29
 */
@Entity
@Getter
@Setter
public class Permission {
    @Id
    private int id;
    private String name;
    private Boolean online;
}
