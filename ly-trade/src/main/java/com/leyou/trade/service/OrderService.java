package com.leyou.trade.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leyou.trade.dto.OrderFormDTO;
import com.leyou.trade.entity.Order;

import javax.validation.Valid;
import java.util.Map;

/**
 * (Order)表服务接口
 *
 * @author makejava
 * @since 2020-12-02 13:47:35
 */
public interface OrderService extends IService<Order> {

    Long createOrder(@Valid OrderFormDTO orderDTO);

    String getPayUrl(Long orderId);

    void handleNotify(Map<String, String> data);

    Integer queryOrderState(Long orderId);

    void evictOrderIfNecessary(Long orderId);

    Integer activeQuery2Wx(Long id);
}