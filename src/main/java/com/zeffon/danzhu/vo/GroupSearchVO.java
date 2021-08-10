package com.zeffon.danzhu.vo;

import com.zeffon.danzhu.model.Groups;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

/**
 * Create by Zeffon on 2020/10/17
 */
@Getter
@Setter
public class GroupSearchVO extends GroupVO {
    private Integer type; // 1.表示与自己无关 2.表示自己加入的 3.表示自己创建的

    public GroupSearchVO(Groups groups, Integer type) {
        BeanUtils.copyProperties(groups, this);
        this.type = type;
    }
}
