package com.leyou.auth.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Package: com.leyou.auth.config
 * Description：
 * Author: wude
 * Date:  2020-11-16 17:13
 * Modified By:
 */
@Configuration
public class OSSConfig {
    @Bean
    public OSS ossClient(@Autowired OSSProperties prop) {//自动装配
        return new OSSClientBuilder()
                .build(prop.getEndpoint(), prop.getAccessKeyId(), prop.getAccessKeySecret());
    }
}
