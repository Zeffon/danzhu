package com.zeffon.danzhu.repository;

import com.zeffon.danzhu.model.UserGroupApply;
import com.zeffon.danzhu.vo.ApplyVO;
import com.zeffon.danzhu.vo.GroupUserApplyVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Create by Zeffon on 2020/10/2
 */
@Repository
public interface UserGroupApplyRepository extends JpaRepository<UserGroupApply, Integer> {

    @Query("SELECT ug FROM UserGroupApply ug\n" +
            "WHERE ug.userId = :uid\n" +
            "AND ug.groupId = :groupId\n" +
            "AND ug.status = 0 \n" +
            "AND ug.deleteTime IS NULL")
    Optional<UserGroupApply> findUserIdAndGroupIdAndStatus(Integer uid, Integer groupId);

    @Query("SELECT new com.zeffon.danzhu.vo.GroupUserApplyVO(u.code, u.wxInfo, ug.id, ug.status, ug.createTime)\n" +
            "FROM UserGroupApply ug\n" +
            "JOIN User u ON u.id = ug.userId\n" +
            "WHERE ug.groupId = :gid\n" +
            "AND ug.deleteTime IS NULL")
    Page<GroupUserApplyVO> findAllByGroupId(Integer gid, Pageable pageable);

    @Query("SELECT ug FROM UserGroupApply ug\n" +
            "JOIN Groups g ON g.id = ug.groupId\n" +
            "WHERE ug.id = :id AND g.userId = :uid\n" +
            "AND ug.deleteTime IS NULL")
    Optional<UserGroupApply> hasPermissionToUserGroupApply(Integer uid, Integer id);

    /** 下面两个搭配，根据status获取不同类型申请列表 */
    @Query("SELECT new com.zeffon.danzhu.vo.ApplyVO(ug.id, ug.status, ug.createTime, g.title, u.wxInfo)\n" +
            "FROM UserGroupApply ug\n" +
            "JOIN User u ON u.id = ug.userId\n" +
            "JOIN Groups g ON ug.groupId = g.id\n" +
            "WHERE ug.userId = :uid\n" +
            "AND ug.deleteTime IS NULL")
    Page<ApplyVO> findByUserId(Integer uid, Pageable pageable);

    @Query("SELECT new com.zeffon.danzhu.vo.ApplyVO(ug.id, ug.status, ug.createTime, g.title, u.wxInfo)\n" +
            "FROM UserGroupApply ug\n" +
            "JOIN User u ON u.id = ug.userId\n" +
            "JOIN Groups g ON ug.groupId = g.id\n" +
            "WHERE ug.userId = :uid\n" +
            "AND ug.status = :status\n" +
            "AND ug.deleteTime IS NULL")
    Page<ApplyVO> findByUserIdAndStatus(Integer uid, int status, Pageable pageable);
}
