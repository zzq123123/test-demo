package com.leyou.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leyou.user.entity.User;

public interface UserService extends IService<User> {
    Boolean checkDataExists(String data, Integer type);
}