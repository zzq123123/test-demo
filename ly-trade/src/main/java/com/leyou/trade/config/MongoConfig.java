package com.leyou.trade.config;

import com.leyou.trade.utils.CollectionNameBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Package: com.leyou.trade.config
 * Description：
 * Author: wude
 * Date:  2020-12-01 21:39
 * Modified By:
 */

//xml 文件
@Configuration
public class MongoConfig {
    @Value("${ly.mongo.collectionNamePrefix}")
    private String collectionNamePrefix;

    @Bean
    public CollectionNameBuilder collectionNameBuilder() {
        return new CollectionNameBuilder(collectionNamePrefix);
    }
}
