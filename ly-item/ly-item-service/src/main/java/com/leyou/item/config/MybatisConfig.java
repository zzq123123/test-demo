package com.leyou.item.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Package: com.leyou.item.config
 * Description：
 * Author: wude
 * Date:  2020-11-14 19:53
 * Modified By:
 */
@Configuration
public class MybatisConfig {

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));//开启count的join以后花
//设置最大单页限制数量,默认500条,-1不受限制
        paginationInterceptor.setLimit(500);


        return paginationInterceptor;

    }
}
