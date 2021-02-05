package com.leyou.gateway.filters;
import com.leyou.auth.constants.JwtConstants;
import com.leyou.auth.dto.Payload;
import com.leyou.auth.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


import javax.servlet.*;
import java.io.IOException;

@Slf4j
@Component
@Order
public class AuthRefreshFilter implements GlobalFilter {

    @Autowired
    JwtUtils jwtUtils;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain ) {
        //获取cookie
        ServerHttpRequest request = exchange.getRequest();
        MultiValueMap<String, HttpCookie> cookies = request.getCookies();
        HttpCookie cookie = cookies.getFirst(JwtConstants.COOKIE_NAME);
        if (cookie==null) {
            return chain.filter(exchange);
        }
        String jwt = cookie.getValue();

        if (StringUtils.isBlank(jwt)) {
            //jwt为空 失败
            return chain.filter(exchange);
        }
        //验证是否登录
        try {
            Payload payload = jwtUtils.parseJwt(jwt);
            //成功 登录
            //刷新有效期
            jwtUtils.refreshJwt(payload.getUserDetail().getId());
        } catch (Exception e) {
            //未登录放行
            log.info("解析失败");
        }
        return chain.filter(exchange);
    }
}