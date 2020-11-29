package com.leyou.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leyou.common.exception.LyException;
import com.leyou.user.entity.User;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author 虎哥
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Override
    public Boolean checkDataExists(String data, Integer type) {
        //查询 去数据库查 不是按照id来查  是条件查询 用query() 如果不是1 2 就是没条件 查了很多数据 不好
        //feign调用 是http远程调用 只会自动解包   不会管异常状态码  之类的 所以feign是不会接受到异常的
        if (type != 1 && type != 2) {
            throw new LyException(400, "参数类型错误!");
        }
        Integer count = query()
                .eq(type == 1, "username", data)
                .eq(type == 2, "phone", data)
                .count();
        return count != 0;  //0就是false
    }
}