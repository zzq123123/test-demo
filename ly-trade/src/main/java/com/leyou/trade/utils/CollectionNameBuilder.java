package com.leyou.trade.utils;

import com.leyou.auth.dto.UserDetails;
import com.leyou.auth.local.UserContext;

/**
 * Package: com.leyou.trade.utils
 * Description：
 * Author: wude
 * Date:  2020-12-01 21:31
 * Modified By:
 */



public class CollectionNameBuilder {
    //代码块 构造器 显式
    private final String namePrefix;




    public CollectionNameBuilder(String namePrefix) {
        this.namePrefix = namePrefix + "_";
    }


    public String  build(){
        UserDetails user = UserContext.getUser();
        if (user == null) {
            //拦截器存入的 现在是null 说明不是被拦截的请求那么 直接返回空串继续执行其他代码

            return "";
        }


        return namePrefix + user.getId().hashCode() % 100;
    }
}
