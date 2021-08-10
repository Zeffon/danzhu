package com.zeffon.danzhu.dto.group;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Create by Zeffon on 2020/10/5
 */
@Getter
@Setter
public class DeleteDTO {
    @Size(min = 1, max = 50, message = "传入的id数量必须在1-50个")
    private List<Integer> ids; // user_group表中的id
}
