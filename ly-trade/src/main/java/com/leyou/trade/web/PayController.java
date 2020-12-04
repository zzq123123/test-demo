package com.leyou.trade.web;

import com.leyou.trade.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Package: com.leyou.trade.web
 * Description：
 * Author: wude
 * Date:  2020-12-04 23:27
 * Modified By:
 */
@RestController
@RequestMapping("pay")
public class PayController {
    private final OrderService orderService;
    public PayController(OrderService orderService) {
        this.orderService = orderService;
    }
    /**
     * 根据订单编号创建支付链接
     * @param orderId 订单编号
     * @return 支付链接
     */
    @GetMapping("/url/{id}")
    public ResponseEntity<String> getPayUrl(@PathVariable("id") Long orderId ){
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getPayUrl(orderId)) ;


        
    }
}
