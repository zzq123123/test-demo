package com.exampleredis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
/**
 * Package: com.exampleredis.config
 * Description：
 * Author: wude
 * Date:  2020-12-06 12:46
 * Modified By:
 */
@Configuration
public class RedisConfig {
    /**
     * 配置Redis消息监听器的容器
     */
        @Bean
public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {
            RedisMessageListenerContainer container = new RedisMessageListenerContainer();
            container.setConnectionFactory(connectionFactory);
            return container;
        }

    /**
     * 配置一个过期key的消息监听器
     */
    @Bean
    public KeyExpirationEventMessageListener redisKeyExpirationListener(RedisMessageListenerContainer container){
        // 创建监听器，覆盖监听器默认的doHandleMessage方法  有子一定有父 父创建对象顺便把子创建了
        return new KeyExpirationEventMessageListener(container) {
            //匿名内部类对象的类体
   //父子关系能重写方法 匿名内部类的方法和构造方法没有一点用 因为你是匿名的无法好好使用 只能重写
            @Override
            protected void doHandleMessage(Message message) {
                // 获取消息体
                byte[] body = message.getBody();
                //获取消息类型
                byte[] channel = message.getChannel();
                //输出
                System.out.println("body=" + new String(body));
//                 __keyevent@0__:expired  总体来看这就是一个频道
                System.out.println("chanel = " + new String (channel));
            }
        };
}
}