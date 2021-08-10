package com.zeffon.danzhu.repository;

import com.zeffon.danzhu.model.UserGroup;
import com.zeffon.danzhu.vo.GroupUserVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Create by Zeffon on 2020/10/2
 */
@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Integer> {

    Optional<UserGroup> findFirstByUserIdAndGroupId(Integer uid, Integer groupId);

    Optional<UserGroup> findFirstByUserIdAndId(Integer uid, Integer id);

    @Query("SELECT ug FROM UserGroup ug\n" +
            "JOIN Groups g ON g.id = ug.groupId\n" +
            "WHERE ug.id = :id AND g.userId = :uid")
    Optional<UserGroup> hasPermissionToUserGroup(Integer uid, Integer id);

    @Query("SELECT ug FROM UserGroup ug\n" +
            "JOIN Groups g ON g.id = ug.groupId\n" +
            "WHERE g.userId = :uid AND ug.id in :ids")
    Optional<List<UserGroup>> hasPermissionToUserGroupList(Integer uid, List<Integer> ids);

    @Query("SELECT new com.zeffon.danzhu.vo.GroupUserVO(u.id, u.code, u.wxInfo, ug.id, ug.remark, ug.createTime)\n" +
            "FROM UserGroup ug\n" +
            "JOIN User u ON u.id = ug.userId\n" +
            "WHERE ug.groupId = :groupId\n" +
            "AND ug.deleteTime IS NULL")
    Page<GroupUserVO> findAllByGroupId(Integer groupId, Pageable pageable);

    List<UserGroup> findAllByIdIn(List<Integer> ids);

    @Query("SELECT ug.userId FROM UserGroup ug\n" +
            "WHERE ug.groupId = :groupId\n" +
            "AND ug.deleteTime IS NULL ")
    List<Integer> findUserIdsByGroupId(Integer groupId);
}
