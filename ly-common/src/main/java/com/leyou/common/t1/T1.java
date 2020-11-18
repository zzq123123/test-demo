package com.leyou.common.t1;

import com.leyou.common.utils.JsonUtils;

/**
 * Package: com.leyou.common.t1
 * Description：
 * Author: wude
 * Date:  2020-11-16 22:23
 * Modified By:
 */
public class T1 {

    public static void main(String[] args) {
        String s = JsonUtils.toJson(User1.of("zhang", "19"));
        System.out.println(s);
    }
}
