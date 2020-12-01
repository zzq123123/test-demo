package com.leyou.user.client;

import com.leyou.user.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Package: com.leyou.user.client
 * Description：
 * Author: wude
 * Date:  2020-11-29 22:18
 * Modified By:
 */




@FeignClient("user-service")
public interface UserClient {


    /**
     * 根据用户名和密码查询用户
     *
     * @return 用户信息
     */
    @GetMapping("/info")

    public UserDTO queryUserByPhoneAndPassword(@RequestParam("username") String username,
                                                               @RequestParam("password") String password);


}