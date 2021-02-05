package com.leyou.auth.service.impl;
import com.aliyun.oss.OSS;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.leyou.auth.config.OSSProperties;
import com.leyou.auth.dto.AliOssSignatureDTO;
import com.leyou.auth.service.AliAuthService;
import com.leyou.common.exception.LyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;

/**
 * Package: com.leyou.auth.service.impl
 * Description：
 * Author: wude
 * Date:  2020-11-16 17:17
 * Modified By:
 */
@Service
@Slf4j
public class AliAuthServiceImpl implements AliAuthService {
    @Autowired
    private OSSProperties prop;

    @Autowired
    private OSS client;




    @Override
    public AliOssSignatureDTO getSignature() {
        try {
            long expireTime =  prop.getExpireTime();
            long expireEndTime =System.currentTimeMillis() + expireTime;
            Date expiration = new Date(expireEndTime);
            // PostObject请求最大可支持的文件大小为5 GB，即CONTENT_LENGTH_RANGE为5*1024*1024*1024。
            PolicyConditions policyConds = new PolicyConditions();




            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, prop.getMaxFileSize());



            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY,  prop.getDir());
                                                            //CST
            String postPolicy = client.generatePostPolicy(expiration, policyConds);



            byte[] binaryData = postPolicy.getBytes("utf-8");


            String encodedPolicy = BinaryUtil.toBase64String(binaryData);



            // 3.生成签名
            String postSignature = client.calculatePostSignature(postPolicy);

            return AliOssSignatureDTO.of(prop.getAccessKeyId(), prop.getHost(), encodedPolicy, postSignature, expireEndTime, prop.getDir());

        } catch (Exception e) {
            // Assert.fail(e.getMessage());
//            System.out.println(e.getMessage());
            log.error("上传文件失败,原因{}",e.getMessage(),e);
            throw new LyException(500,"文件上传失败");
        } finally {
            client.shutdown();
        }
    }
}
