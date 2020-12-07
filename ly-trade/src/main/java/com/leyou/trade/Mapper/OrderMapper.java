package com.leyou.trade.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leyou.trade.dto.OrderDTOP;
import com.leyou.trade.entity.Order;
import org.apache.ibatis.annotations.Update;

import java.util.HashMap;

/**
 * (Order)表数据库访问层
 *
 * @author makejava
        * @since 2020-12-02 13:47:36
        */
public interface OrderMapper extends BaseMapper<Order> {
    @Update(" UPDATE tb_order set status = #{status}, close_time = #{closeTime} where order_id = #{orderId} AND status = #{statusCAS}")
    public Integer updateOrder(OrderDTOP orderDTOP);
}