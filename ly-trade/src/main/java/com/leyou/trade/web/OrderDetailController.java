package com.leyou.trade.web;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leyou.trade.entity.OrderDetail;
import com.leyou.trade.service.OrderDetailService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * 订单详情表(OrderDetail)表控制层
 *
 * @author makejava
 * @since 2020-12-02 14:01:54
 */
@RestController
@RequestMapping("orderDetail")
public class OrderDetailController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private OrderDetailService orderDetailService;

    /**
     * 分页查询所有数据
     *
     * @param page        分页对象
     * @param orderDetail 查询实体
     * @return 所有数据
     */
    @GetMapping
    public R selectAll(Page<OrderDetail> page, OrderDetail orderDetail) {
        return success(this.orderDetailService.page(page, new QueryWrapper<>(orderDetail)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return success(this.orderDetailService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param orderDetail 实体对象
     * @return 新增结果
     */
    @PostMapping
    public R insert(@RequestBody OrderDetail orderDetail) {
        return success(this.orderDetailService.save(orderDetail));
    }

    /**
     * 修改数据
     *
     * @param orderDetail 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R update(@RequestBody OrderDetail orderDetail) {
        return success(this.orderDetailService.updateById(orderDetail));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R delete(@RequestParam("idList") List<Long> idList) {
        return success(this.orderDetailService.removeByIds(idList));
    }
}