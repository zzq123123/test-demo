package com.leyou.auth.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Package: com.leyou.user.client
 * Description：
 * Author: wude
 * Date:  2020-11-30 1:44
 * Modified By:
 * <p>
 * <p>
 * 这个接口适合别人一体的
 */
@FeignClient(value = "auth-service") //微服务依赖你 你就自动跑到容器离去
public interface AuthClient {
    //被微服务依赖  想authservice发起请求  是用来校验secrit对不对 对就给微服务一个key用来无状态是否登录校验的
    @GetMapping("/client/key")
    String  getJwtKey(@RequestParam("clientId") String clientId,
                                            @RequestParam("secret") String secret);

}