package com.leyou.auth.config;
import com.leyou.auth.interceptors.LoginInterceptor;
import com.leyou.auth.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Package: com.leyou.user.config
 * Description：
 * Author: wude
 * Date:  2020-11-30 18:00
 * Modified By:
 */
@Configuration
@ConditionalOnBean(value = JwtConfig.class)
@ConditionalOnProperty(prefix = "ly.auth", name = "enable", havingValue = "true")
public class MvcConfig implements WebMvcConfigurer {
    @Autowired
    ClientProperties prop;
    @Autowired
    @Lazy
    JwtUtils jwtUtils;  //不需要把intercepter放入ioc 就这里需要new 一次用一次
    public    MvcConfig() {
        System.out.println("MvcConfig创建了 ----------------------------------------------------------66666666666666666");
    }


//bean方法 就是bean标签
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration = registry.addInterceptor( new LoginInterceptor(jwtUtils));


        List<String> includePathPatterns = prop.getIncludePathPatterns();
        if (!CollectionUtils.isEmpty(includePathPatterns)) {
        registration.addPathPatterns(includePathPatterns);

        }

        List<String> excludePathPatterns = prop.getExcludePathPatterns();
        if (!CollectionUtils.isEmpty(excludePathPatterns)) {
        registration.excludePathPatterns(excludePathPatterns);
        }
    }
}