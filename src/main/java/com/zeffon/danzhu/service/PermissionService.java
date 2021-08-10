package com.zeffon.danzhu.service;

import com.zeffon.danzhu.model.Permission;
import com.zeffon.danzhu.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Create by Zeffon on 2020/11/29
 */
@Service
public class PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;


    public Boolean getShareBtn() {
        String name = "share-btn";
        Permission permission = this.permissionRepository.findOneByName(name);
        return permission.getOnline();
    }
}
