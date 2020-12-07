package com.leyou.item.config;
import feign.Logger;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * @author 虎哥
 */
@Configuration
@EnableFeignClients(basePackages = "com.leyou.item.client" )
public class FeignConfig {
    /*@Bean
    public Logger.Level itemFeignLogLevelItem(){
        return Logger.Level.BASIC;
    }*/
}