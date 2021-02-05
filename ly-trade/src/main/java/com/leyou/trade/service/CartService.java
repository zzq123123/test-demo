package com.leyou.trade.service;

import com.leyou.trade.entity.CartItem;

import java.util.List;

/**
 * Package: com.leyou.trade.service
 * Description：
 * Author: wude
 * Date:  2020-12-01 21:47
 * Modified By:
 */
public interface CartService  {

    /**
     * @param
     * @return {@link null}
     * @Description 新增购物车 里面的商品
     */
    void saveCartItem(CartItem cartItem);

    /**
     * 查询用户的购物车商品集合
     * @return 购物车商品集合
     */
    List<CartItem> queryCartList();
    /**
     * 更新购物车指定商品的数量
     * @param skuId 商品id
     * @param num 数量
     */
    void updateNum(Long skuId, Integer num);


    /**
     * 删除购物车指定商品
     * @param skuId 商品id
     */
    void deleteCart(Long skuId);

    /**
     * 批量删除购物车指定商品
     * @param ids 商品id
     */
    void deleteCarts(  List<Long> ids);
    /**
     * 批量新增购物车商品
     * @param cartItemList 购物车商品列表
     */
    void addCartItemList(List<CartItem> cartItemList);
}
