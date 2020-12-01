package com.leyou.common.config;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;



//创建redisproperties 读取的是可执行jar包的配置文件 不能读取自己的非执行jar包的配置文件
@Configuration
public class RedisConfig {
    @Bean
    @Primary
    public RedisProperties redisProperties() {
        RedisProperties redisProperties = new RedisProperties();
        redisProperties.setHost("ly-redis"); //以前自动绑定 现在手写+自动绑定 而且先加载 把系统的挤下去
        return redisProperties;
    }

}
