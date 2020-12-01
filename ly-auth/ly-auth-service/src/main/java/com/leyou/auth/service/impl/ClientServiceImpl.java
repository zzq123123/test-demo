package com.leyou.auth.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leyou.auth.entity.ClientInfo;
import com.leyou.auth.mapper.ClientMapper;
import com.leyou.auth.service.ClientService;
import com.leyou.common.exception.LyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Package: com.leyou.auth.service.impl
 * Description：
 * Author: wude
 * Date:  2020-11-30 1:36
 * Modified By:
 */
@Service
@Slf4j
public class ClientServiceImpl extends ServiceImpl<ClientMapper, ClientInfo> implements ClientService {
    @Autowired
   private   PasswordEncoder passwordEncoder;
    @Value("${ly.jwt.key}")
    private String key;
    @Override
    public String getJwtKey(String clientId, String secret) {

        //验证
        ClientInfo clientInfo = this.query()
                .eq("client_id", clientId).one();
        //比较scret

        if (clientInfo==null) {
            throw new LyException(400,"客户端不存在");
        }
        boolean b = passwordEncoder.matches(secret, clientInfo.getSecret());
        //返回秘钥
        if (b==false) {

            throw new LyException(400, "微服务密码提供不对");


        }
        return key;
    }
}
