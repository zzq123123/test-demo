package com.leyou.auth.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.auth.dto.Payload;
import com.leyou.auth.dto.UserDetails;
import com.leyou.common.exception.LyException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author 虎哥
 */
@Data
@Slf4j
public class JwtUtils {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public static final String KEY_PREFIX = "auth:jti:uid";
    /**
     * JWT解析器
     */
    private JwtParser jwtParser;
    /**
     * 秘钥
     */
    private SecretKey secretKey;

    private final static ObjectMapper mapper = new ObjectMapper();

    public JwtUtils(String key) {
        // 生成秘钥
        secretKey = Keys.hmacShaKeyFor(key.getBytes(Charset.forName("UTF-8")));
        // JWT解析器
        this.jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();
    }

    public JwtUtils() {
        // 生成秘钥
        // JWT解析器
        log.info("默认化完毕");
    }

    /**
     * 生成jwt，用默认的JTI
     *
     * @param userDetails 用户信息
     * @return JWT
     */
    public String createJwt(UserDetails userDetails) {
        return createJwt(userDetails, 1800);
    }

    /**
     * 生成jwt，自己指定的过期时间
     *
     * @param userDetails 用户信息
     * @return JWT
     */
    public String createJwt(UserDetails userDetails, int expireSeconds) {
        try {
            //获取jti
            String jti = createJti();

            // 生成token
            String jwt = Jwts.builder().signWith(secretKey)
                    .setId(jti)
                    .claim("user", mapper.writeValueAsString(userDetails))
                   .setExpiration(DateTime.now().plusSeconds(expireSeconds).toDate())  //传入的expireSeconds只对jti起作用
                    .compact();

//把生成的jwt 的jwt id jti 放redis中 设置时间
            stringRedisTemplate.opsForValue().set(KEY_PREFIX + userDetails.getId(), jti, expireSeconds, TimeUnit.SECONDS);

            return jwt;

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 验证并解析jwt，返回包含用户信息的载荷
     *
     * @param jwt token
     * @return 载荷，包含JTI和用户信息
     */
    public Payload parseJwt(String jwt) {

        try {

            //验证并解析
            Jws<Claims> claimsJws = jwtParser.parseClaimsJws(jwt);
            Claims claims = claimsJws.getBody();
            //封装payload
            Payload payload = new Payload();
            payload.setJti(claims.getId());
            payload.setUserDetail(mapper.readValue(claims.get("user", String.class), UserDetails.class));
            payload.setExpiration(claims.getExpiration());
            //验证jti
            //获取当前jwt jti
            String jti = payload.getJti();
            //获取 user id
            Long userId = payload.getUserDetail().getId();
            //去redis 获取 jti
            String cacheJti = stringRedisTemplate.opsForValue().get(KEY_PREFIX + userId);
            //比较

            if (StringUtils.isBlank(cacheJti)) {
                throw new LyException(401, "登录无效或者已经超时");
            }
            if (!StringUtils.equals(jti, cacheJti)) {
                //我现在是微服务的对象
                throw new LyException(401, "账户已在别处登录");
            }
            return payload;
        } catch (IllegalArgumentException e) {
            throw new LyException(401, "用户未登录！");
        } catch (JwtException e) {
            throw new LyException(401, "登录无效或者已经超时！");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private String createJti() {
        return StringUtils.replace(UUID.randomUUID().toString(), "-", "");
    }
    protected void setKey(String jwtKey) {
        // 生成秘钥
        secretKey = Keys.hmacShaKeyFor(jwtKey.getBytes(Charset.forName("UTF-8")));
        // JWT解析器
        this.jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();
    }

    public void refreshJwt(Long userId,Long expireSeconds) {
        //给key 重置有效期
        stringRedisTemplate.expire(KEY_PREFIX + userId, expireSeconds, TimeUnit.SECONDS);
    }
    public void refreshJwt(Long userId) {
        //给key 重置有效期
        stringRedisTemplate.expire(KEY_PREFIX + userId, 1800, TimeUnit.SECONDS);
    }
}