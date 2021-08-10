package com.zeffon.danzhu.repository;

import com.zeffon.danzhu.model.Groups;
import com.zeffon.danzhu.vo.GroupDetailVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.security.acl.Group;
import java.util.Optional;

/**
 * Create by Zeffon on 2020/10/1
 */
@Repository
public interface GroupRepository extends JpaRepository<Groups, Integer> {

    Page<Groups> findAllByUserId(Integer uid, Pageable pageable);

    @Query("select g from Groups g\n" +
            "join UserGroup ug on g.id = ug.groupId\n" +
            "where g.userId <> :uid\n" +
            "and ug.userId = :uid\n" +
            "and ug.deleteTime is null")
    Page<Groups> findMyJoinGroup(Integer uid, Pageable pageable);

    Optional<Groups> findFirstByUserIdAndId(Integer uid, Integer gid);

    Optional<Groups> findFirstByCode(String code);

    Optional<Groups> findFirstById(Integer gid);

    @Query("select g from Groups g\n" +
            "join UserGroup ug on g.id = ug.groupId\n" +
            "where g.id = :gid\n" +
            "or (ug.userId = :uid\n" +
            "and ug.deleteTime is null)")
    Optional<Groups> getDetail(Integer uid, Integer gid);


    @Modifying
    @Query("update Groups g set g.userNumber = (g.userNumber + :num) where g.id = :id")
    void addUserNumber(Integer id, Integer num);

    @Modifying
    @Query("update Groups g set g.userNumber = (g.userNumber - :num) where g.id = :id")
    void reduceUserNumber(Integer id, Integer num);

    @Query(value = "select ifnull(\n" +
            "    (select 3 from groups g where g.id = :id and g.user_id = :uid and g.delete_time is null limit 1), (select ifnull(\n" +
            "        (select 2 from user_group ug where ug.group_id = :id and ug.user_id = :uid and ug.delete_time is null limit 1), 1)))",
            nativeQuery = true)
    Integer getRelated(Integer uid, Integer id);

    @Query("SELECT new com.zeffon.danzhu.vo.GroupDetailVO(g.id, g.code, g.title, g.status, g.summary, g.userNumber, g.remark, g.createTime, u.wxInfo) \n" +
            "FROM Groups g\n" +
            "JOIN User u ON u.id = g.userId\n" +
            "WHERE g.id = :gid\n" +
            "AND g.deleteTime is NULL ")
    Optional<GroupDetailVO> getMoreDetail(Integer gid);
}
