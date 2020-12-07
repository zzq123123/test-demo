package com.leyou.auth.config;

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

@EnableFeignClients(basePackages = "com.leyou.auth.client")
@Configuration
public class FeignConfig { //类名相同 没事 只要限定名不同就好
    @Bean("userFeignLogLevell")
    public Logger.Level userFeignLogLevel(){
        return Logger.Level.BASIC;
    }
}