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

    /*5分钟之后未付款 微信是不会管的 那么前台不可能一直轮询
    所以5分钟到了删除订单 判断基准是延迟队列订阅
    5分钟到了清除轮询 判断基准就是主动询问
    询问结果
    正 调用notify业务
    负  最后查一次状态 正 那么返回成功 负 返回负 以上过程有异常为负
    */
    @GetMapping("status/{id}")
    public ResponseEntity<Integer> activeQuery2Wx(@PathVariable Long id) {

        return ResponseEntity.status(HttpStatus.OK).body(orderService.activeQuery2Wx(id));
    }
}
