package com.leyou.user.config;

import com.leyou.user.client.UserClient;
import feign.Logger;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Package: com.leyou.user.config
 * Description：
 * Author: wude
 * Date:  2020-11-29 22:24
 * Modified By:
 */

@EnableFeignClients(basePackages = "com.leyou.user.client")
@Configuration
public class FeignConfig {
    @Bean
    public Logger.Level userFeignLogLevel(){
        return Logger.Level.BASIC;

    }
}