package com.leyou.auth.web;

import com.leyou.auth.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Package: com.leyou.auth.web
 * Description：
 * Author: wude
 * Date:  2020-11-29 22:33
 * Modified By:
 */

//我的dao在远程 对应的数据库是 user 数据表
@RestController
@RequestMapping("/user")
public class UserAuthController {
    @Autowired
    UserAuthService userAuthService;

    /**
     * @param username
     * @param password
     * @param response
     * @return {@link ResponseEntity<Void>}
     * @Description 登陆业务
     */
    @PostMapping("login")
    public ResponseEntity<Void> login(@RequestParam("username") String username
            , @RequestParam("password") String password, HttpServletResponse response) {

        userAuthService.login(username, password, response);

       return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }
    
  /**
   * @param
   * @return {@link null}
   * @Description
   *
   */
  @PostMapping("logout")
  public ResponseEntity<Void> logout(HttpServletRequest request,HttpServletResponse response){
      userAuthService.logout(request, response);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

  }
}
