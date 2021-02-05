package com.exampleredis;

import com.leyou.common.exception.LyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Package: com.exampleredis
 * Description：
 * Author: wude
 * Date:  2020-12-10 23:20
 * Modified By:
 */
@RestController
@RequestMapping("ceshi")
public class CeshiController {
    @GetMapping("/hello")
    public ResponseEntity<String> m(){

        System.out.println("加好");


            m2();



        return ResponseEntity.ok("sdadsa");
    }




    public void m2(){
        System.out.println("你好");
        throw new RuntimeException("aa" );
    }

    public void m3(){
        System.out.println("你好");
        throw new RuntimeException("aa" );
    }


    public void m4(){
        System.out.println("你好");
        throw new RuntimeException("aa" );
    }
}
