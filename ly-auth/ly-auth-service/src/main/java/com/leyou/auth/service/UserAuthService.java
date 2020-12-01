package com.leyou.auth.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Package: com.leyou.auth.service
 * Description：
 * Author: wude
 * Date:  2020-11-29 22:38
 * Modified By:
 */
public interface UserAuthService {
    void login(String username, String password, HttpServletResponse response);

    void logout(HttpServletRequest request, HttpServletResponse response);
}
