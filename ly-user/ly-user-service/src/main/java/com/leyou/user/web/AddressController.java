package com.leyou.user.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户地址管理
 */
@RestController
@RequestMapping("address")
public class AddressController {

    @GetMapping("hello")
    public ResponseEntity<String> hello(){
        // 校验JWT
        return ResponseEntity.ok("上海浦东新区航头镇航头路18号传智播客");
    }
    @GetMapping("hello2")
    public ResponseEntity<String> hello2(){
        // 校验JWT2
        return ResponseEntity.ok("上海浦东新区航头镇航头路18号黑马程序员");
    }
}