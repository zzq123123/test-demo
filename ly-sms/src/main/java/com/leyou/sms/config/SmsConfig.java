package com.leyou.sms.config;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Package: com.leyou.sms.config
 * Description：
 * Author: wude
 * Date:  2020-11-28 22:22
 * Modified By:
 */
@Configuration
@EnableConfigurationProperties(SmsProperties.class)
public class SmsConfig {


    @Bean
    public IAcsClient acsClient(SmsProperties prop) {
        DefaultProfile profile = DefaultProfile.getProfile(prop.getRegionID(), prop.getAccessKeyID(), prop.getAccessKeySecret());
        IAcsClient client = new DefaultAcsClient(profile);

        return client;
    }





}
