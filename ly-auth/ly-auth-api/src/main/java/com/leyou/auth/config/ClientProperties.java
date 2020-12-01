package com.leyou.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Package: com.leyou.auth.config
 * Description：
 * Author: wude
 * Date:  2020-11-30 16:43
 * Modified By:
 */
@Data
@ConfigurationProperties(prefix = "ly.auth")
public class ClientProperties {
    /**
     * 客户端 id 秘钥 要拦截的路径 要放的路径
     */
    private String clientId;
    private String secret;
    private List<String> includePathPatterns;
    private List<String> excludePathPatterns;
    private Boolean enable = false;
}
