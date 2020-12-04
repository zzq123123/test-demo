package com.leyou.trade.utils;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConfigImpl;
import com.leyou.trade.constants.PayConstants;
 import org.apache.commons.lang3.StringUtils;
 import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

/**
 * Package: com.leyou.trade.utils
 * Description：
 * Author: wude
 * Date:  2020-12-04 22:40
 * Modified By:
 */
@Component
public class PayHelper {
    private final WXPay wxPay;
    private final WXPayConfigImpl payConfig;

    //没有空参要想 注入到ioc只能通过这个方法生成对象
    public PayHelper (WXPay wxPay,WXPayConfigImpl payConfig) {
        this.payConfig = payConfig;
        this.wxPay = wxPay;
    }

    public String unifiedOrder(Long orderId, Long totalFee){
        // 1.准备请求参数：
        Map<String, String> data = new HashMap<String, String>();
        // 1.1.商品有关信息
        data.put("body", PayConstants.ORDER_DESC);
        data.put(PayConstants.ORDER_NO_KEY, orderId.toString());
        data.put(PayConstants.TOTAL_FEE_KEY, totalFee.toString());
        // 1.2.交易的固定参数
        data.put("spbill_create_ip", payConfig.getSpbillCreateIp());//这个暂时 不管
        data.put("trade_type", PayConstants.UNIFIED_ORDER_TRADE_TYPE);


        try {
            // 2.统一下单
            Map<String, String> resp = wxPay.unifiedOrder(data);
            // 3.数据校验
            // 3.1.returnCode校验
            checkReturnCode(resp);
            // 3.2.resultCode校验
            checkResultCode(resp);
            // 3.3.签名校验
            checkSignature(data);
            String url = resp.get(PayConstants.PAY_URL_KEY);
            if (StringUtils.isBlank(url)) {
                throw new RuntimeException("支付链接为空");  //逻辑错误 不需要解决 直接报错


            }

            return url;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
    public void checkResultCode(Map<String, String> resp) {
        String resultCode = resp.get(PayConstants.RESULT_CODE_KEY);
        if(PayConstants.FAIL.equals(resultCode)){
            // returnCode错误
            throw new RuntimeException(resp.get(PayConstants.ERROR_CODE_KEY));
        }
    }
    private void checkReturnCode(Map<String, String> resp) {
        String returnCode = resp.get(PayConstants.RETURN_CODE_KEY);
        if(PayConstants.FAIL.equals(returnCode)){
            // returnCode错误
            throw new RuntimeException(resp.get(PayConstants.RETURN_MESSAGE_KEY));
        }


    }

    public void checkSignature(Map<String, String> data) throws Exception {
        boolean signatureValid = wxPay.isResponseSignatureValid(data);
        if(!signatureValid){
            // 签名有误
            throw new RuntimeException("签名有误！");
        }
    }
}
