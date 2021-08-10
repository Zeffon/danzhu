package com.zeffon.danzhu.repository;

import com.zeffon.danzhu.model.UserCollect;
import com.zeffon.danzhu.vo.CollectUserVO;
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
public interface UserCollectRepository extends JpaRepository<UserCollect, Integer> {

    Optional<UserCollect> findFirstByUserIdAndCollectId(Integer uid, Integer collectId);

    UserCollect findFirstByUserIdAndCollectIdAndStatus(Integer uid, Integer collectId, Integer status);

    List<UserCollect> findAllByIdIn(List<Integer> userIds);

    @Query("SELECT new com.zeffon.danzhu.vo.CollectUserVO(u.id, u.code, u.wxInfo, uc.collectId, uc.status, uc.createTime)\n" +
            "FROM UserCollect uc\n" +
            "JOIN User u ON u.id = uc.userId\n" +
            "WHERE uc.collectId = :cid\n" +
            "AND uc.status <> 0\n" +
            "AND uc.deleteTime IS NULL")
    Page<CollectUserVO> findAllByCollectId(Integer cid, Pageable pageable);

    @Query(value = "SELECT new com.zeffon.danzhu.vo.CollectUserVO(u.id as id, u.code as code, u.wx_info as wxInfo, uc.collect_id as collectId, sum(1) as fileCount, uc.create_time as createTime)\n" +
            "join user u on u.id = uc.user_id\n" +
            "join file_library f on f.collect_id = uc.collect_id\n" +
            "where uc.collect_id = :cid\n" +
            "and uc.status <> 0\n" +
            "and uc.delete_time is null \n" +
            "GROUP BY f.user_id", nativeQuery = true)
    Page<CollectUserVO> findAllByCollectId2(Integer cid, Pageable pageable);
    // @Query(value = "SELECT new EvaUserResp(b.branch_name as branchName,a.username as userName,c.job_name as jobName,c.job_type as jobType ) FROM tab_user a INNER JOIN tab_branch b ON b.branch_id = a.branch_id INNER JOIN tab_job c on c.job_id = a.job_id WHERE user_id = 4 ", nativeQuery = true)

    List<UserCollect> findAllByCollectId(Integer uid);
}
