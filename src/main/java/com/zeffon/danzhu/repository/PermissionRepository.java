package com.zeffon.danzhu.repository;

import com.zeffon.danzhu.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Create by Zeffon on 2020/11/29
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {

    Permission findOneByName(String name);
}
