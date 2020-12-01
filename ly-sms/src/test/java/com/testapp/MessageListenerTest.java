package com.testapp;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import javax.annotation.Resource;
import java.util.HashMap;
import static com.leyou.common.constants.MQConstants.ExchangeConstants.SMS_EXCHANGE_NAME;
import static com.leyou.common.constants.MQConstants.RoutingKeyConstants.VERIFY_CODE_KEY;
/**
 * Package: com.testapp
 * Description：
 * Author: wude
 * Date:  2020-11-28 23:27
 * Modified By:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageListenerTest {
    @Resource
    private AmqpTemplate amqpTemplate;

    @Test
    public void testSendMessage() {
        String code = RandomStringUtils.randomNumeric(6);
        HashMap<String, String> msg = new HashMap<>();
        msg.put("phone", "17351156912");
        msg.put("code", code);
        amqpTemplate.convertAndSend(SMS_EXCHANGE_NAME, VERIFY_CODE_KEY,msg );
    }
}
