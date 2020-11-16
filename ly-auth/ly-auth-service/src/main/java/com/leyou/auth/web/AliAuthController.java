package com.leyou.auth.web;
import com.leyou.auth.dto.AliOssSignatureDTO;
import com.leyou.auth.service.AliAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Package: com.leyou.auth.web
 * Description：
 * Author: wude
 * Date:  2020-11-16 17:06
 * Modified By:
 */
@RestController
@RequestMapping("ali")
public class AliAuthController {
    @Autowired
    AliAuthService aliAuthService;
    @GetMapping("/oss/signature")
    public ResponseEntity<AliOssSignatureDTO> getAliSignature(){
        return ResponseEntity.ok(aliAuthService.getSignature());
    }
}
