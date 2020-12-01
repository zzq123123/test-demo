package com.leyou.auth.config;

import com.leyou.auth.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Package: com.leyou.auth.config
 * Description：
 * Author: wude
 * Date:  2020-11-29 21:21
 * Modified By:
 */

@Configuration
public class JwtConfig {
    @Value("${ly.jwt.key}")
    private String key;

    @Bean
    public JwtUtils  jwtUtils(){

        return new JwtUtils(key);
    }


    //user微服务也用到了加密工具 但是这两个微服务没有强依赖  他是自定义的参数 这里 使用默认就好

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
