package com.zeffon.danzhu.dto.collect;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;

/**
 * Create by Zeffon on 2020/10/2
 */
@Getter
@Setter
public class CreateDTO {

    @NotBlank(message = "title不能为空")
    private String title;

    private String summary;

    @Range(min = 0, max = 7, message = "validPeriod数值不在指定范围内")
    private Integer validPeriod;

    @Range(min = 10, max = 100, message = "userLimit数值不在指定范围内")
    private Integer userLimit;

    private Integer groupId;
}
