package com.leyou.auth.web;

import com.leyou.auth.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Package: com.leyou.auth.web
 * Description：
 * Author: wude
 * Date:  2020-11-30 1:49
 * Modified By:
 */
@RestController
@RequestMapping("/client")
public class ClientAuthController {
    @Autowired
    private ClientService clientService;

    //
    @GetMapping("key")
    public ResponseEntity<String> getJwtKey(@RequestParam("clientId") String clientId,
                            @RequestParam("secret") String secret) {

      return   ResponseEntity.status(HttpStatus.OK).body(clientService.getJwtKey(clientId,secret));
    }

}
