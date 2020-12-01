package com.leyou.auth.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author 虎哥
 */
@Data
public class Payload {
    /**
     * JWT的id，唯一标示
     */
    private String jti;
    /**
     * 用户信息
     */
    private UserDetails userDetail;

    private Date expiration;
}