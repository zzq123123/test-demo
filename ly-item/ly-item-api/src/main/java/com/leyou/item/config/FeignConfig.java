package com.leyou.item.config;

import feign.Logger;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 虎哥
 */
@Configuration
public class FeignConfig {

    @Bean
    public Logger.Level itemFeignLogLevel(){
        return Logger.Level.BASIC;
    }
}