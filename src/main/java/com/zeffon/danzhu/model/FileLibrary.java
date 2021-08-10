package com.zeffon.danzhu.model;

import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Create by Zeffon on 2020/10/5
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileLibrary extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    private Integer userId;
    private Integer collectId;
    private String title;
    private String url;
    private Integer size;
    private Boolean online;
    private Integer category;
}
