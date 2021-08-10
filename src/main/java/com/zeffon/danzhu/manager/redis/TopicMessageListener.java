package com.zeffon.danzhu.manager.redis;

import com.zeffon.danzhu.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

/**
 * @author Zeffon
 * @date 2021/3/18 18:49
 */
@Slf4j
@Component
public class TopicMessageListener implements MessageListener {


    private static FileService fileService;

    @Autowired
    public void setPublisher(FileService fileService) {
        TopicMessageListener.fileService = fileService;
    }

    /** 接受redis键空间方法 */
    @Override
    public void onMessage(Message message, byte[] bytes) {
        byte[] body = message.getBody();
        String expiredKey = new String(body);

        System.out.println(expiredKey);
        if (expiredKey.startsWith(FileService.DELETE_FILE)) {
            Integer id = Integer.parseInt(expiredKey.substring(FileService.DELETE_FILE.length()));
            log.info("******************删除文件id：" + id);
            fileService.realDeleteFile2(id);
        }
    }
}
