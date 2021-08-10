package com.zeffon.danzhu.repository;

import com.zeffon.danzhu.model.Collect;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Create by Zeffon on 2020/10/1
 */
@Repository
public interface CollectRepository extends JpaRepository<Collect, Integer> {

    Page<Collect> findAllByUserId(Integer uid, Pageable pageable);

    @Query("select c from Collect c\n" +
            "join UserCollect uc on c.id = uc.collectId\n" +
            "where c.userId <> :uid\n" +
            "and uc.userId = :uid")
    Page<Collect> findMyJoinGroup(Integer uid, Pageable pageable);

    Optional<Collect> findFirstByUserIdAndId(Integer uid, Integer collectId);

    Optional<Collect> findFirstByCode(String code);

    @Modifying
    @Query("update Collect c set c.deleteTime = :date where c.userId = :uid and c.id in :ids")
    void batchDeleteById(Integer uid, List<Integer> ids, Date date);

    @Modifying
    @Query("update Collect c set c.userNumber = (c.userNumber - :num) where c.id = :id")
    void reduceUserNumber(Integer id, Integer num);

    @Modifying
    @Query("update Collect c set c.userNumber = (c.userNumber + :num) where c.id = :id")
    void addUserNumber(Integer id, Integer num);
}
