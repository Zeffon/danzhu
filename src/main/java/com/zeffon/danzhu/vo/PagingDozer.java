package com.zeffon.danzhu.vo;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by Zeffon on 2020/10/1
 */
public class PagingDozer<T, K> extends Paging {

    @SuppressWarnings("unchecked")
    public PagingDozer(Page<T> pageT, Class<K> kClass) {
        this.initPageParameters(pageT);

        List<T> tList = pageT.getContent();
        Mapper mapper = DozerBeanMapperBuilder.buildDefault();
        List<K> voList = new ArrayList<>();

        tList.forEach(t -> {
            K vo = mapper.map(t, kClass);
            voList.add(vo);
        });
        this.setItems(voList);
    }
}
