package com.zeffon.danzhu.repository;

import com.zeffon.danzhu.model.FileLibrary;
import com.zeffon.danzhu.vo.FileUserVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Create by Zeffon on 2020/10/8
 */
@Repository
public interface FileRepository extends JpaRepository<FileLibrary, Integer> {

    List<FileLibrary> findAllByUserIdAndCollectIdAndDeleteTimeIsNull(Integer userId, Integer collectId);

    Page<FileLibrary> findAllByUserIdAndDeleteTimeIsNull(Integer uid, Pageable pageable);

    List<FileLibrary> findAllByUserIdAndDeleteTimeIsNull(Integer uid);

    List<FileLibrary> findAllByUserIdAndDeleteTimeIsNullOrderByCreateTimeDesc(Integer uid);

    Page<FileLibrary> findAllByUserIdAndDeleteTimeIsNotNull(Integer uid, Pageable pageable);

    List<FileLibrary> findAllByUserIdAndDeleteTimeIsNotNull(Integer uid);

    FileLibrary findFirstByIdAndUserId(Integer id, Integer userId);

    @Modifying
    @Query("update FileLibrary f set f.online = :online where f.userId = :uid and f.id = :id")
    void setOnline(Integer uid, Integer id, Boolean online);

    @Modifying
    @Query("update FileLibrary f set f.deleteTime = null where f.id = :fid and f.userId = :uid")
    void restoreFile(Integer fid, Integer uid);

    @Transactional
    @Modifying
    @Query(value = "delete from FileLibrary f where f.id = :fid and f.userId = :uid")
    void realDeleteFile(Integer fid, Integer uid);

    @Transactional
    @Modifying
    @Query(value = "delete from FileLibrary f where f.id = :fid and f.deleteTime IS NOT NULL")
    void realDeleteFile2(Integer fid);

    /** 获取某用户分享的文件列表 */
    List<FileLibrary> findAllByUserIdAndDeleteTimeIsNullAndOnlineIsTrue(Integer uid);

    List<FileLibrary> findAllByCollectIdAndDeleteTimeIsNull(Integer cid);

    @Query("SELECT count(f.id) from FileLibrary f\n" +
            "WHERE f.collectId = :cid\n" +
            "AND f.deleteTime IS NULL \n" +
            "GROUP BY f.userId")
    List<Integer> findAllByCollectIdAndDeleteTimeIsNullAndGroupByUserId(Integer cid);

    @Query("SELECT new com.zeffon.danzhu.vo.FileUserVO(u.id, u.wxInfo, fl.id, fl.title, fl.url, fl.size, fl.category, fl.createTime)\n" +
            "FROM FileLibrary fl\n" +
            "JOIN User u ON u.id = fl.userId\n" +
            "WHERE fl.collectId = :cid\n" +
            "AND fl.deleteTime IS NULL\n" +
            "ORDER BY fl.userId, fl.createTime ASC")
    List<FileUserVO> listAllFileByCid(Integer cid);
}
