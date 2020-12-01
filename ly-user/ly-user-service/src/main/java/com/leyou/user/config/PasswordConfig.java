package com.leyou.user.config;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.security.SecureRandom;
/**
 * Package: com.leyou.user.config
 * Description：
 * Author: wude
 * Date:  2020-11-29 16:54
 * Modified By:
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "ly.encoder.crypt")
public class PasswordConfig {
    //自动绑定
//
    private int strength;
    private String secret;
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        //生成随机安全码
        SecureRandom secureRandom = new SecureRandom(secret.getBytes());
        // 处理登陆业务的一部分
      return   new BCryptPasswordEncoder(strength, secureRandom);
    }
}