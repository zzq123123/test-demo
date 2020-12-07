package com.leyou.trade.mq;

import com.leyou.trade.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.leyou.common.constants.MQConstants.QueueConstants.EVICT_ORDER_QUEUE;

/**
 * Package: com.leyou.trade.mq
 * Description：
 * Author: wude
 * Date:  2020-12-06 17:08
 * Modified By:
 */
@Component
@Slf4j
public class OrderListener {
    @Autowired
    private OrderService orderService;

/**
 * 监听清理订单的消息
 * @param orderId 订单id
 */

@RabbitListener(queues = EVICT_ORDER_QUEUE)
public void listenOrderMessage(Long orderId) throws InterruptedException{
    if (orderId != null) {
        log.info("接收到订单 {}",orderId);

        try {
            orderService.evictOrderIfNecessary(orderId);
        } catch (Exception e) {
            Thread.sleep(5000);
            throw new RuntimeException(e);

         }
    }


}
}
