package com.leyou.user.web;

import com.leyou.auth.dto.UserDetails;
import com.leyou.auth.local.UserContext;
import com.leyou.common.exception.LyException;
import com.leyou.user.dto.UserDTO;
import com.leyou.user.entity.User;
import com.leyou.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
 import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;

/**
 * Package: com.leyou.user.web
 * Description：
 * Author: wude
 * Date:  2020-11-29 12:52
 * Modified By: zzq
 */

@RestController
@RequestMapping("info")
public class UserInfoController {
    @Autowired
  private UserService userService;
    /**
     *
     *
     * @return {@link ResponseEntity<Boolean>} true存在
     * @Description 验证data是否存在
     */

    @GetMapping("/exists/{data}/{type}")
    public ResponseEntity<Boolean> checkDataExists(@PathVariable("data") String data, @PathVariable("type") Integer type) {
        return ResponseEntity.ok(userService.checkDataExists(data, type));
    }

    /**
     * 发送短信验证码
     *
     * @return 无
     */

    @PostMapping("/code")
    public ResponseEntity<Void> sendCode(@RequestParam("phone") String phone) {
        userService.sendCode(phone);
        return ResponseEntity.noContent().build();
    }

    /**
     * 注册功能
     *
     * @return 无
     */
    //属性对应一样就自动绑定了
    @PostMapping
    public ResponseEntity<Void> register(@Valid User user, Errors result, @RequestParam("code") String code) {
        // 判断校验的结果

        userService.register(user, code);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    /**
 * 根据用户名和密码查询用户
 * @param username  用户名
 * @param password 密码
 * @return 用户信息
 */
   @GetMapping
    public ResponseEntity<UserDTO> queryUserByPhoneAndPassword(@RequestParam("username") String username,
           @RequestParam("password")String password){
       return ResponseEntity.ok(userService.queryUserByPhoneAndPassword(username, password)); //200
    }


    //1.获取jwt
    //解析
    //返回身份

    @GetMapping("me")
    public ResponseEntity<UserDetails> whoAmI(){
       //从上下文中去获取 在拦截器存 在controller 取
        UserDetails user = UserContext.getUser();
        return ResponseEntity.status(HttpStatus.OK).body(user); //get 创建是created

    }
}