package com.leyou.auth.service;

import com.leyou.auth.dto.AliOssSignatureDTO;

/**
 * Package: com.leyou.auth.service
 * Description：
 * Author: wude
 * Date:  2020-11-16 17:16
 * Modified By:
 */

public interface AliAuthService {
    AliOssSignatureDTO getSignature();
}
