package com.zeffon.danzhu;

import com.zeffon.danzhu.model.FileLibrary;
import com.zeffon.danzhu.repository.FileRepository;
import com.zeffon.danzhu.vo.FileArchiveVO;
import com.zeffon.danzhu.vo.FileVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Create by Zeffon on 2020/11/9
 */

public class FileTest extends DanzhuApplicationTests {

    @Autowired
    private FileRepository fileRepository;

    @Test
    public void listMyFile() {
        Integer uid = 73;
        List<FileLibrary> list = fileRepository.findAllByUserIdAndDeleteTimeIsNull(uid);
        DateFormat sdf = new SimpleDateFormat("yyyyMM");
        HashMap<String, List<FileVO>> map = new HashMap<>();
        list.forEach(item -> {
            String month = sdf.format(item.getCreateTime().getTime());
            if(!map.containsKey(month)){
                map.put(month, new ArrayList<>());
            }
            map.get(month).add(new FileVO(item));
        });
        FileArchiveVO fileArchiveVO = new FileArchiveVO(map);
        System.out.println(fileArchiveVO);
    }
}
