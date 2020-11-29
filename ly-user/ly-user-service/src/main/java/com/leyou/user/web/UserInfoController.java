package com.leyou.user.web;

import com.leyou.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
  private    UserService userService;
    /**
     * `
     *
     * @return {@link ResponseEntity<Boolean>} true存在
     * @Description 验证data是否存在
     */

    @GetMapping("/exists/{data}/{type}")
    public ResponseEntity<Boolean> checkDataExists(@PathVariable("data") String data, @PathVariable("type") Integer type) {
        return ResponseEntity.ok(userService.checkDataExists(data, type));
    }


}
