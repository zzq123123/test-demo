package com.leyou.trade.repository;

import com.leyou.trade.entity.CartItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Package: com.leyou.trade.repository
 * Description：
 * Author: wude
 * Date:  2020-12-01 21:47
 * Modified By:
 */
public interface CartRepositry extends MongoRepository<CartItem,String > {

    List<CartItem> findByUserId(Long userId);
}