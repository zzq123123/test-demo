package com.leyou.search;

import com.leyou.common.annotation.EnableExceptionAdvice;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author 虎哥
 */

@EnableFeignClients(basePackages = "com.leyou.item.client" )   //自动启动原理写配置文件取代替这个也是可以
@SpringBootApplication(scanBasePackages = {"com.leyou.search"})
@EnableExceptionAdvice
public class LySearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(LySearchApplication.class, args);
    }
}