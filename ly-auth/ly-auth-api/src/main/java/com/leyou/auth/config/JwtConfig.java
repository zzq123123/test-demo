package com.leyou.auth.config;
import com.leyou.auth.client.AuthClient;
import com.leyou.auth.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.annotation.Resource;
/**
 * Package: com.leyou.user.config
 * Description：
 * Author: wude
 * Date:  2020-11-30 15:42
 * Modified By:
 */
@Configuration
@EnableConfigurationProperties(value = ClientProperties.class)
@Slf4j
@ConditionalOnProperty(prefix = "ly.auth" , name = {"clientId","secret"})
public class JwtConfig {
    @Resource
    AuthClient authClient;
    @Bean
    public JwtUtils jwtUtils(ClientProperties prop) {
      /*  new Thread(() -> {

        }).start();
*/
      /*  while (true) {
            try {
                return    new JwtUtils(authClient.getJwtKey("user-service","1234"));
            } catch (Exception e) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }*/
        return new JwtUtils( ) {   //先默认化一个对象 暂时不能用 底下慢慢初始化
            {
                new Thread(() -> {
                    while (true) {
                        try {
                            String jwtKey = authClient.getJwtKey(prop.getClientId(), prop.getSecret()); //这个密码去加密 密文中有盐的信息 和强度信息 所以不需要知道太多 直接校验就好
                            //获取到之后给默认化的对象初始化  公钥被封装在了jwt对象 Jwt对象存在ioc中
                            this.setKey(jwtKey);
                            log.info("秘钥加载成功");
                            break;
                        } catch (Exception e) {
                            try {
                                log.error("jwt秘钥加载失败 10秒后重试原因{}",e);
                                Thread.sleep(10000);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace(); //有异常等10秒再次去获取
                            }
                        }
                    }
                }).start();
            }
        };
    }
}