package com.zeffon.danzhu.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Create by Zeffon on 2020/10/1
 */
@Getter
@Setter
@NoArgsConstructor
public class Paging<T> {

    private Long total;  // 总数量
    private Integer count;  // 当前请求需要多少条
    private Integer page;  // 页码
    private Integer totalPage; // 总共有多少页
    private List<T> items;

    public Paging(Page<T> pageT) {
        this.initPageParameters(pageT);
        this.items = pageT.getContent();
    }

    void initPageParameters(Page<T> pageT) {
        this.total = pageT.getTotalElements();
        this.count = pageT.getSize();
        this.page = pageT.getNumber();
        this.totalPage = pageT.getTotalPages();
    }
}
