package com.leyou.trade.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

/**
 * @author 虎哥
 */
@Data  //在序列化成xml的时候 会的到一个包裹的标签名
@JacksonXmlRootElement(localName = "xml")
public class PayResultDTO {
    @JacksonXmlProperty(localName = "return_code")
    private String returnCode = "SUCCESS";
    @JacksonXmlProperty(localName = "return_msg")
    private String returnMsg = "OK";
}