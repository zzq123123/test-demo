package com.leyou.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author 虎哥
 */
@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class UserDetails {
    /**
     * 用户id  这里没有用户密码
     */
    private Long id;
    /**
     * 用户名
     */
    private String username;


}