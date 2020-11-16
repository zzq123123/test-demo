package com.leyou.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Package: com.leyou.auth.dto
 * Description：
 * Author: wude
 * Date:  2020-11-16 17:04
 * Modified By:
 */
@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class AliOssSignatureDTO {
    private String accessId;
    private String host;
    private String policy;
    private String signature;
    private long expire;
    private String dir;
}
