package com.leyou.trade.service.impl;

import com.leyou.auth.dto.UserDetails;
import com.leyou.auth.local.UserContext;
import com.leyou.common.exception.LyException;
import com.leyou.trade.entity.CartItem;
import com.leyou.trade.repository.CartRepositry;
import com.leyou.trade.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Package: com.leyou.trade.service.impl
 * Description：
 * Author: wude
 * Date:  2020-12-01 21:49
 * Modified By:
 */
//不是所有请求都需要拦截 但是这个请求必须要拦截
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    CartRepositry cartRepositry;

    @Override
    public void saveCartItem(CartItem cartItem) {
        //获取用户信息
        UserDetails user = UserContext.getUser();
        //用户id在之前的拦截器去到过了 用于bean的初始化 在这里作为联合主键
        Long userId = user.getId();
        cartItem =   handleUpdateItem(cartItem, userId);
        cartRepositry.save(cartItem);  //全覆盖

    }

    private CartItem handleUpdateItem(CartItem cartItem, Long userId) {
        //查询购物车商品   生成联合主键
        String id = createId(cartItem.getSkuId());
        Optional<CartItem> optional = cartRepositry.findById(id);//根据用户去查询购物车里面有没有
        if (optional.isPresent()) {
            //存在就是修改 只能改数字  获取传来的num
            Integer num = cartItem.getNum();
            //拿到传来的num 变量就没有利用价值了 换成指向本地的 item
            cartItem = optional.get();
            cartItem.setNum(num + cartItem.getNum());
        }//本地变量id就存在了 再加一次也没关系
        //补充数据
        cartItem.setId(id);
        cartItem.setUserId(userId);
        cartItem.setUpdateTime(new Date());   //utc+8
//写入nosql
        return cartItem;
    }

    private String createId(Long skuId) {
        //找到 userid

        return String.format("u%ds%d", UserContext.getUser().getId(), skuId);
    }

    @Override
    public List<CartItem> queryCartList() {
        //这条路线是需要被拦截的  所以来到这肯定请求通过了校验
        UserDetails user = UserContext.getUser();
        Long userId = user.getId();

        return cartRepositry.findByUserId(userId);
    }

    @Override
    public void updateNum(Long skuId, Integer num) {
        mongoTemplate.update(CartItem.class)
                //筛选条件
                .matching(Query.query(Criteria.where("_id").is(createId(skuId))))
                //修改条件
                .apply(Update.update("num", num))
                .first();  //all
    }

    @Override
    public void deleteCart(Long skuId) {
        cartRepositry.deleteById(createId(skuId));

    }

    @Override
    public void addCartItemList(List<CartItem> cartItemList) {
        // 1.获取用户id
        Long userId = UserContext.getUser().getId();
        List<CartItem> list = cartItemList.stream().map(item -> {
            return handleUpdateItem(item, userId);

        }).collect(Collectors.toList());




        //不管存不存在数据库   现在的到的对象要么是丰满的  要么是修改数据后丰满的 直接保存就好


        cartRepositry.saveAll(list);

    }
}