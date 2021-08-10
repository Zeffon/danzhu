package com.zeffon.danzhu.dto.collect;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * Create by Zeffon on 2020/10/2
 */
@Getter
@Setter
public class PureDTO {

    private List<Integer> ids;
}
