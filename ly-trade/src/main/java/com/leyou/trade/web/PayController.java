package com.leyou.trade.web;

import com.leyou.trade.dto.PayResultDTO;
import com.leyou.trade.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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


    @PostMapping(value = "/wx/notify", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<PayResultDTO> handleWxNotify(@RequestBody Map<String, String> data) {
        //去处理业务如果能成功返回固定模板
        orderService.handleNotify(data);
        return ResponseEntity.status(HttpStatus.OK).body(new PayResultDTO());
    }
}
