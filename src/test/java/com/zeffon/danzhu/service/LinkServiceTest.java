package com.zeffon.danzhu.service;

import com.zeffon.danzhu.DanzhuApplicationTests;
import com.zeffon.danzhu.vo.FileUserVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Create by Zeffon on 2021/4/3
 */
class LinkServiceTest extends DanzhuApplicationTests {

    @Autowired
    private LinkService linkService;

    @Test
    void list() {
        List<FileUserVO> list = linkService.list("LAB1588951");
        System.out.println(list);
    }
}