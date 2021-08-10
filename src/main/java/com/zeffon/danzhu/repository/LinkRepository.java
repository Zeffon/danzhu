package com.zeffon.danzhu.repository;

import com.zeffon.danzhu.model.Link;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Create by Zeffon on 2020/10/3
 */
@Repository
public interface LinkRepository extends JpaRepository<Link, Integer> {

    Optional<Link> findFirstByUserIdAndCollectId(Integer uid, Integer collectId);

    Link findOneByCode(String code);

    Link findOneByCodeAndUserId(String code, Integer userId);
}
