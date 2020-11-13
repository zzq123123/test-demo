package com.leyou.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author 虎哥
 */
@Configuration
public class RateLimitConfig {
    //定义一个KeyResolver
    @Bean
    public KeyResolver ipKeyResolver() {
        return new KeyResolver() {
            @Override
            public Mono<String> resolve(ServerWebExchange exchange) {
                return Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
            }
        };
//        这是个匿名内部类
        // JDK8 的Lambda写法：  目标方法使用了函数式接口 接口中只存在一个抽象的方法
        // return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
    }
}