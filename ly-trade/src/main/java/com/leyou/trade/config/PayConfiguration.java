package com.leyou.trade.config;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConfigImpl;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Package: com.leyou.trade.config
 * Description：
 * Author: wude
 * Date:  2020-12-04 22:35
 * Modified By:
 */
@Configuration
//WXPayConfigImpl 目标类不能加上configurationproperties注解所以直接注入
public class PayConfiguration {
    
    @Bean
    @ConfigurationProperties(prefix = "ly.pay.wx")
    public WXPayConfigImpl payConfig(){
    return  new WXPayConfigImpl();              //new 对象 直接自动绑定
    }


    @Bean
    public WXPay wxPay(WXPayConfigImpl wxPayConfig) throws Exception{
        return new WXPay(wxPayConfig,wxPayConfig.getNotifyUrl());
        //
    }

}
