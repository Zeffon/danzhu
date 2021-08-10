package com.zeffon.danzhu.dto;

import com.zeffon.danzhu.bo.RemarkBO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import java.util.List;

/**
 * Create by Zeffon on 2020/10/7
 */
@Getter
@Setter
public class RemarkDTO {
    @Valid
    private List<RemarkBO> remark;
}
