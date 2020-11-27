package com.leyou.search.mq;

import com.leyou.common.constants.MQConstants;
import com.leyou.search.service.SearchService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.leyou.common.constants.MQConstants.ExchangeConstants.ITEM_EXCHANGE_NAME;
import static com.leyou.common.constants.MQConstants.QueueConstants.SEARCH_ITEM_DOWN;
import static com.leyou.common.constants.MQConstants.QueueConstants.SEARCH_ITEM_UP;
import static com.leyou.common.constants.MQConstants.RoutingKeyConstants.ITEM_DOWN_KEY;
import static com.leyou.common.constants.MQConstants.RoutingKeyConstants.ITEM_UP_KEY;

/**
 * Package: com.leyou.search.mq
 * Description：
 * Author: wude
 * Date:  2020-11-27 1:23
 * Modified By:
 */
@Component
public class ItemListener {
    @Autowired
    SearchService searchService;
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = SEARCH_ITEM_UP,durable = "true"),
            exchange = @Exchange(name = ITEM_EXCHANGE_NAME,type = ExchangeTypes.TOPIC ),
            key = ITEM_UP_KEY

    ))
    public void listenIemUp(Long spuId){

        if (spuId != null) {
            //商品上架 需要写入到 索引库
            searchService.saveSpuById(spuId);
        }

    }




    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = SEARCH_ITEM_DOWN,durable = "true"),
            exchange = @Exchange(name = ITEM_EXCHANGE_NAME,type = ExchangeTypes.TOPIC ),
            key = ITEM_DOWN_KEY

    ))
    public void listenIemDown(Long spuId){

        if (spuId != null) {
            //商品上架 需要写入到 索引库
            searchService.deleteById(spuId);

        }

    }
}
