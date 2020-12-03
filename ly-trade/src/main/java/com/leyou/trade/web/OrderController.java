package com.leyou.trade.web;
import com.leyou.trade.dto.OrderDTO;
import com.leyou.trade.dto.OrderFormDTO;
import com.leyou.trade.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
/**
 * (Order)表控制层
 *
 * @author makejava
 * @since 2020-12-02 13:47:36
 */
@RestController
@RequestMapping("order")
public class OrderController {
    @Autowired
    OrderService orderService;


    @PostMapping
    public ResponseEntity<Long> createOrder(@RequestBody @Valid OrderFormDTO orderDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(orderDTO));
    }

    /**
     * 根据id查询订单
     *
     * @param
     * @return 订单对象
     */
    @GetMapping("{id}")
    public ResponseEntity<OrderDTO> queryOrderById(@PathVariable(value = "id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(new OrderDTO(orderService.getById(id)));//有一个枚举属性 没有绑定 手动绑定好

    }


}