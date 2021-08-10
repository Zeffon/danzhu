package com.zeffon.danzhu.bo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Create by Zeffon on 2020/10/1
 */
@Getter
@Setter
@Builder
public class PageCounter {
    private Integer page;
    private Integer count;
}
