package com.leyou.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.RegexUtils;
import com.leyou.user.dto.UserDTO;
import com.leyou.user.entity.User;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static com.leyou.common.constants.MQConstants.ExchangeConstants.SMS_EXCHANGE_NAME;
import static com.leyou.common.constants.MQConstants.RoutingKeyConstants.VERIFY_CODE_KEY;
/**
 * @author 虎哥
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private AmqpTemplate amqpTemplate;


    private static final String KEY_PREFIX = "user:verify:code:";


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

    /**
     * @param phone
     * @return
     * @Description 这里的业务逻辑是这样的：
     * <p>
     * - 1）我们接收页面发送来的手机号码
     * - 2）生成一个随机验证码
     * - 3）将验证码保存在服务端（要用redis代替session）
     * - 4）发送短信，将验证码发送到用户手机（向MQ发送消息）
     */
    @Override
    public void sendCode(String phone) {
        if (!RegexUtils.isPhone(phone)) {
            throw new LyException(400, "手机号格式不正确");

        }

        String code = RandomStringUtils.randomNumeric(6);

        redisTemplate.opsForValue().set(KEY_PREFIX + phone, code, 5, TimeUnit.MINUTES);
        //发送MQ消息 通知sms服务 应该发送短信验证码到指定手机号
        HashMap<String, String> msg = new HashMap<>();
        msg.put("phone", phone);
        msg.put("code", code);
        //绑定
        amqpTemplate.convertAndSend(SMS_EXCHANGE_NAME, VERIFY_CODE_KEY, msg);
    }

    /**
     * @param user
     * @param code
     * @return
     * @Description 基本功能 基本逻辑：
     * <p>  加密在这里进行 如果有多台机器 就要把 强度 和 scrit写死 存起来 加密是不可逆的
     * - 1）校验短信验证码
     * - 2）对密码加密
     * - 3）写入数据库
     */
    @Override
    @Transactional   //抛出个编译异常不可
    public void register(User user, String code) {
        // 1.校验验证码
        // 1.1 取出redis中的验证码
        String cacheCode = redisTemplate.opsForValue().get(KEY_PREFIX + user.getPhone());
        // 1.2 比较验证码
        if (!StringUtils.equals(code, cacheCode)) {
            throw new LyException(400, "验证码错误 ");
        }
        // 2.对密码加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        //用户主要是 username phone password
        this.save(user);
    }
//这里是登陆核心代码
    @Override
    public UserDTO queryUserByPhoneAndPassword(String username, String password) {
        // 1.根据用户名查询   查询包装纸 对象
//        this.getOne(new QueryWrapper<User>().eq("username", username));  密码不是索引 分开查询
        User user = this.query().eq("username", username).one(); //唯一约束 one
        // 2.判断是否存在
        if (user == null) {
            //用户名错误
            throw new LyException(400, "用户名或者密码错误");
        }

        //3.校验密码  用户得到的密码是 经过BC加密的 所以 我们这里要使用BC对象来处理
        /*if (user.getPassword()==password) {
      要注意，查询时也要对密码进行加密后判断是否一致。
        }*/


        if (!passwordEncoder.matches(password, user.getPassword())) {
            //就是新的密码  按照加密层数 和 加密的 secrit 来加密和 已经加密的来匹配   至于多少层和 加密的盐是什么 密文中就有 加密好加密 如果你得到层数和salt去解密基本不可能
            throw new LyException(400, "用户名或者密码错误");

        }

        return new UserDTO(user);
    }
}