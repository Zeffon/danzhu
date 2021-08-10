package com.zeffon.danzhu.manager.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;

/**
 * @author Zeffon
 * @date 2021/3/18 18:48
 */
@Configuration
public class MessageListenerConfiguration {

    @Value("${spring.redis.listen-pattern}")
    public String pattern;

    @Bean
    public RedisMessageListenerContainer listenerContainer(RedisConnectionFactory redisConnection) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        // 设置连接 和 设置监听主题/模式
        container.setConnectionFactory(redisConnection);
        Topic topic = new PatternTopic(this.pattern);
        // 添加监听器,绑定主题
        container.addMessageListener(new TopicMessageListener(), topic);
        return container;
    }
}
