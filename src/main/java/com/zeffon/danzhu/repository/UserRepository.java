package com.zeffon.danzhu.repository;

import com.zeffon.danzhu.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Create by Zeffon on 2020/10/1
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    Optional<User> findByOpenid(String openid);
    User findFirstById(Integer id);
    Optional<User> findByCode(String code);

    @Query("SELECT u FROM User u\n" +
            "JOIN Collect c ON u.id = c.userId\n" +
            "WHERE c.id = :cid")
    User findOneByCollectId(Integer cid);
}
