package com.leyou.sms.utils;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.leyou.common.utils.JsonUtils;
import com.leyou.sms.config.SmsProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static com.leyou.sms.constants.SmsConstants.VERIFY_CODE_PARAM_TEMPLATE;

/**
 * Package: com.leyou.sms.utils
 * Description：
 * Author: wude
 * Date:  2020-11-28 22:14
 * Modified By:
 */
@Slf4j
public class SmsUtil {
    @Autowired
    SmsProperties prop;
    @Autowired
    private IAcsClient acsClient;

    /**
     * @param phone 手机号
     * @param code  要发送的验证码
     * @return
     * 发送验证码的方法  验证码发送失败就到这里为止  重发吧
     */
    public void sendVerifyCodeMessage(String phone, String code) {
        try {
            sendMessage(phone, prop.getSignName(), prop.getVerifyCodeTemplate(), String.format(VERIFY_CODE_PARAM_TEMPLATE, code));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param phone
     * @param signName
     * @param template
     * @param param
     * @return
     * 发送验证码的通用工具
     */
    public void sendMessage(String phone, String signName, String template, String param) {
//准备发送

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain(prop.getDomain());
        request.setSysVersion(prop.getVersion());
        request.setSysAction(prop.getAction());
        request.putQueryParameter("RegionId", prop.getRegionID());
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", template);
        request.putQueryParameter("TemplateParam", param);
        try {

            //发送请求
            CommonResponse response = acsClient.getCommonResponse(request);
            Map<String, String> data = JsonUtils.toMap(response.getData(), String.class, String.class);

//判断业务标识
            if (!"OK".equals(data.get("Code"))) {
                //失败
                log.error("发送短信失败 原因{}", data.get("Message"));
                throw new RuntimeException(data.get("Message"));
            }
            //成功
//            System.out.println(response.getData());

            log.debug("发送短信成功,手机号{}", phone);

        } catch (ServerException e) {
            log.error(" 服务端异常  发送短信失败 {}", e);


        } catch (ClientException e) {

            log.error(" 客户端异常 发送短信失败{}", e);
        }
    }
}
