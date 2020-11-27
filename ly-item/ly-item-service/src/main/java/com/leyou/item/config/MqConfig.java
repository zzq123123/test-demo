package com.leyou.item.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Package: com.leyou.item.config
 * Description：
 * Author: wude
 * Date:  2020-11-26 23:45
 * Modified By:
 */
@Configuration
public class MqConfig {
    @Bean
    MessageConverter messageConverter() {
//        注册一个json的消息处理器 这样MQ发送和接收都是json格式处理
        return new Jackson2JsonMessageConverter(); //序列化和反序列化是json了
    }
}
