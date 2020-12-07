package com.leyou.trade.web;

import com.leyou.trade.entity.CartItem;
import com.leyou.trade.service.CartService;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Package: com.leyou.trade.web
 * Description：
 * Author: wude
 * Date:  2020-12-01 21:50
 * Modified By:
 */
@RestController
@RequestMapping("cart")
public class CartController {
    @Autowired
    CartService cartService;

    /**
     * @param cartItem 购物车数据
     * @return {@link ResponseEntity<Void>}
     * @Description 新增购物车
     */
    @PostMapping
    public ResponseEntity<Void> saveCartItem(@RequestBody CartItem cartItem) {

        cartService.saveCartItem(cartItem);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    /**
     * 查询购物车列表
     * @return 购物车商品列表
     */

    @GetMapping("list")
public ResponseEntity<List<CartItem>> findByUserId( ) {


        return ResponseEntity.status(HttpStatus.OK).body(cartService.queryCartList());    //用于get请求

}

/**
 * 更新购物车指定商品的数量
 * @param skuId 商品id
 * @param num 数量
 * @return 无  前台就没有用户id 在cookie里自己去拿 前台的核心是sku id
 */

@PutMapping
public ResponseEntity<Void> updateNum(
        @RequestParam("id") Long skuId,
        @RequestParam("num") Integer num



){

    cartService.updateNum(skuId,num);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

}

/**
 * 删除指定的购物车商品
 * @param skuId 商品id
 * @return 无
 */

@DeleteMapping("{skuId}")
public ResponseEntity<Void> deleteCart(

        @PathVariable("skuId")  Long skuId

){

    cartService.deleteCart(skuId);

   return ResponseEntity.status(HttpStatus.NO_CONTENT).build();    //增加用 created get 用ok 其余用 nocontent
}


/**
 * 批量添加购物车  合并购物车
 *
 * @return 无
 */
    @PostMapping("list")
    public ResponseEntity<Void> addCartItemList(@RequestBody List<CartItem> itemList) {

        cartService.addCartItemList(itemList);
       return ResponseEntity.status(HttpStatus.CREATED).build();
    }




        


}
