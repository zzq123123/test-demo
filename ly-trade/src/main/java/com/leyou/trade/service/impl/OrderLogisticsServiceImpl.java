package com.leyou.trade.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leyou.trade.Mapper.OrderLogisticsMapper;
 import com.leyou.trade.entity.OrderLogistics;
import com.leyou.trade.service.OrderLogisticsService;
import org.springframework.stereotype.Service;

/**
 * (OrderLogistics)表服务实现类
 *
 * @author makejava
 * @since 2020-12-02 14:02:24
 */
@Service("orderLogisticsService")
public class OrderLogisticsServiceImpl extends ServiceImpl<OrderLogisticsMapper, OrderLogistics> implements OrderLogisticsService {

}