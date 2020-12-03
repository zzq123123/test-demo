package com.leyou.trade.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

/**
 * Package: com.leyou.trade.entity
 * Description：
 * Author: wude
 * Date:  2020-12-02 13:51
 * Modified By:
 */
 public enum OrderStatus  {
    INIT(1, "初始化，未付款"),
    PAY_UP(2, "已付款，未发货"),
    DELIVERED(3, "已发货，未确认"),
    CONFIRMED(4, "已确认,未评价"),
    CLOSED(5, "已关闭"),
    RATED(6, "已评价，交易结束")
    ;

    @EnumValue
    @JsonValue
    private final Integer value;

    private final String msg;


   private OrderStatus(Integer value, String msg) {


       this.value = value;


       this.msg = msg;
   }

    public Integer getValue() {
        return value;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
//   重写Enum里的toString
}
