package com.leyou.auth.service.impl;

import com.leyou.auth.constants.JwtConstants;
import com.leyou.auth.dto.Payload;
import com.leyou.auth.dto.UserDetails;
import com.leyou.auth.service.UserAuthService;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.CookieUtils;
import com.leyou.user.client.UserClient;
import com.leyou.user.dto.UserDTO;
import feign.FeignException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.leyou.auth.constants.JwtConstants.COOKIE_NAME;
import static com.leyou.auth.constants.JwtConstants.DOMAIN;
import static com.leyou.auth.utils.JwtUtils.KEY_PREFIX;

/**
 * Package: com.leyou.auth.service.impl
 * Description：
 * Author: wude
 * Date:  2020-11-29 22:38
 * Modified By:
 */
@Service
public class UserAuthServiceImpl implements UserAuthService {
    @Resource
    private UserClient userClient;
    @Autowired
    private JwtUtils jwtUtils;   //来自auth pai
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Override
    public void login(String username, String password, HttpServletResponse response) {
        try {
            //查用户
            UserDTO user = userClient.queryUserByPhoneAndPassword(username, password);
            //判断
          /*  if (user == null) {
                throw new RuntimeException("用户或者密码有误"); //会被转换成 ly
            }*/
            //来到这肯定ok
            writeCookie(response, user);

        } catch (FeignException e) {

            throw new LyException(e.status(), e.getMessage());
        }
    }

    private void writeCookie(HttpServletResponse response, UserDTO user) {
        //生成jwt
        String jwt = jwtUtils.createJwt(UserDetails.of(user.getId(), user.getUsername()));
        //写入cookie
        Cookie cookie = new Cookie(COOKIE_NAME, jwt);
        cookie.setDomain(DOMAIN);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(-1);

        //写入到 cookie
        response.addCookie(cookie);

    }


    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Payload payload = null;
        try {
            //获取用户的cookie
            String jwt = CookieUtils.getCookieValue(request, COOKIE_NAME);
            if (StringUtils.isBlank(jwt)) {
                return;  //说明你还没有登录
            }
            //校验cookie中的token是否有效
              payload = jwtUtils.parseJwt(jwt);
        } catch (Exception e) {
            //校验除了问题说明 你自己乱登录
             return;
        }

        //删除cookie
        CookieUtils.deleteCookie(COOKIE_NAME,DOMAIN,response);

        //删除jti
        UserDetails userDetail = payload.getUserDetail();
        stringRedisTemplate.delete(KEY_PREFIX + userDetail.getId());

    }
}