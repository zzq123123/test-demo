package com.leyou.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leyou.auth.entity.ClientInfo;

/**
 * Package: com.leyou.auth.service
 * Description：
 * Author: wude
 * Date:  2020-11-30 1:35
 * Modified By:
 */
public interface ClientService  extends IService<ClientInfo> {
    String getJwtKey(String clientId, String secret);
}
