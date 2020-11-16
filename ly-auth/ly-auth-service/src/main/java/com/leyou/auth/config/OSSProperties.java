package com.leyou.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Package: com.leyou.auth.config
 * Description：
 * Author: wude
 * Date:  2020-11-16 17:11
 * Modified By:
 */
@Data
@Component
@ConfigurationProperties("ly.oss")
public class OSSProperties {//自动绑定的感觉
    private String accessKeyId;
    private String accessKeySecret;
    private String host;
    private String endpoint;
    private String dir;
    private long expireTime;
    private long maxFileSize;
}
