package com.leyou.sms.mq;

import com.leyou.common.utils.RegexUtils;
import com.leyou.sms.utils.SmsUtil;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import static com.leyou.common.constants.MQConstants.ExchangeConstants.SMS_EXCHANGE_NAME;
import static com.leyou.common.constants.MQConstants.QueueConstants.SMS_VERIFY_CODE_QUEUE;
import static com.leyou.common.constants.MQConstants.RoutingKeyConstants.VERIFY_CODE_KEY;
/**
 * Package: com.leyou.sms.mq
 * Description：
 * Author: wude
 * Date:  2020-11-28 22:56
 * Modified By:
 */
@Component
public class MassageListener {

    @Autowired
    private SmsUtil smsUtil;

    /**
     * @param
     * @return
     * @Description 监听 并且构建 交换机 路由 和队列
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = SMS_VERIFY_CODE_QUEUE, durable = "true"),
            exchange = @Exchange(name = SMS_EXCHANGE_NAME, type = ExchangeTypes.TOPIC),
            key = VERIFY_CODE_KEY
    ))

    //对象自动转绑定为一个map
    public void listenVerify(Map<String, String> msg) {
        //垃圾消息 数据为空 , 放弃  这里不能抛出异常 这里的异常会被mq感知 然后给你发好多
        if (CollectionUtils.isEmpty(msg)) {
            return;
        }
        String phone = msg.get("phone");

        if ( !RegexUtils.isPhone(phone)) {
            return;
        }
        String code = msg.get("code");

        if (!RegexUtils.isCodeValid(code)) {
            return;
        }
        //可能抛出异常
        smsUtil.sendVerifyCodeMessage(phone, code);
    }
}
