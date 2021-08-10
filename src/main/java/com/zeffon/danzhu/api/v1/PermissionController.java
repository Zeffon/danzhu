package com.zeffon.danzhu.api.v1;

import com.zeffon.danzhu.model.Permission;
import com.zeffon.danzhu.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Create by Zeffon on 2020/11/29
 */
@RestController
@RequestMapping("/permission")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping("share")
    public Boolean getShareBtn() {
        return permissionService.getShareBtn();
    }

}
