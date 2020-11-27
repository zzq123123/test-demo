package com.leyou.search;

import com.leyou.common.annotation.EnableExceptionAdvice;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author 虎哥
 */

@EnableFeignClients(basePackages = "com.leyou.item.client" )
@SpringBootApplication
@EnableExceptionAdvice

public class  sApplication {

    public static void main(String[] args) {
        SpringApplication.run(sApplication.class, args);
    }
}