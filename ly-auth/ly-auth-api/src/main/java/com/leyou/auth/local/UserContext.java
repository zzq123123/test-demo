package com.leyou.auth.local;

import com.leyou.auth.dto.UserDetails;
 import lombok.Data;

/**
 * Package: com.leyou.auth.local
 * Description：
 * Author: wude
 * Date:  2020-11-30 19:34
 * Modified By:
 */
@Data
public class UserContext {
    private final static ThreadLocal<UserDetails> tl = new ThreadLocal<>();  //泛型代表的是每个线程田字格里方法v是什么类型

    public static void setUser (UserDetails user) {
        tl.set(user);
    }
    public static UserDetails getUser() {
        return tl.get();
    }

    public static void removeUser() {
        tl.remove();

    }
}
