package com.leyou.auth;

import com.leyou.auth.dto.Payload;
import com.leyou.auth.dto.UserDetails;
import com.leyou.auth.utils.JwtUtils;
import org.junit.Test;

/**
 * @author 虎哥
 */
public class JwtTest {
    private JwtUtils jwtUtils = new JwtUtils("helloWorldJavaLeyouAuthServiceSecretKey");
    @Test
    public void testJwtUtils() throws InterruptedException {
        // 生成JWT
        String jwt = jwtUtils.createJwt(UserDetails.of(110L, "JackMa"), 3600);
        // String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIwNzI5YTEwYzcwNDg0ZjE2OTZlZGVhNjMzNTNhN2EwNCIsInVzZXIiOiJ7XCJpZFwiOjExMCxcInVzZXJuYW1lXCI6XCJNYVl1blwifSIsImV4cCI6MTYwNjU0OTgyNX0.ITGJHc0633mUUXVauOXhdnzaB7RX7D1zc9KkPODUuCQ";

        System.out.println("jwt = " + jwt);
        Thread.sleep(2000);
        // 解析token
        Payload payload = jwtUtils.parseJwt(jwt);
        System.out.println("payload = " + payload);
    }
}
