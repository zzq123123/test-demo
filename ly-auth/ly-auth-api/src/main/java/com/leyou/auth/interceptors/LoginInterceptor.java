package com.leyou.auth.interceptors;
import com.leyou.auth.dto.Payload;
import com.leyou.auth.dto.UserDetails;
import com.leyou.auth.local.UserContext;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.CookieUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static com.leyou.auth.constants.JwtConstants.COOKIE_NAME;
/**
 * Package: com.leyou.user.interceptors
 * Description：
 * Author: wude
 * Date:  2020-11-30 17:27
 * Modified By:
 */
@Slf4j
 public class LoginInterceptor implements HandlerInterceptor {
  /*  @Autowired
    JwtUtils jwtUtils;   //这个微服务的  对象在微服务启动的时候通过微服务的密码获取到了key并且注入好了 直接用*/
  private JwtUtils jwtUtils;
     public LoginInterceptor(JwtUtils jwtUtils) {
         this.jwtUtils = jwtUtils;
     }
    /*
    * 属性注入 构造器注入 set注入
    *
    *自动装配到ico  空参构造器 工厂 静态工厂  getobjt
    *
    * */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String jwt = CookieUtils.getCookieValue(request, COOKIE_NAME);
        if (StringUtils.isBlank(jwt)) {
            throw new LyException(401,"未登录!");
        }

        Payload payload = jwtUtils.parseJwt(jwt);
        //来到这里说明无状态校验成功了  无需和数据库挂钩
        UserDetails user = payload.getUserDetail();
        if (user == null) {
            throw new LyException(401, "未登录");  //信息是个字符串不是对象
        }
        log.info("用户{}正在访问",user.getUsername());
        UserContext.setUser(user);
        return true;
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //view 渲染完毕数据之后

        //移除用户
        UserContext.removeUser();








    }
}
