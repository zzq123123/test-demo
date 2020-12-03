package com.leyou.trade.web;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leyou.trade.entity.OrderLogistics;
import com.leyou.trade.service.OrderLogisticsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (OrderLogistics)表控制层
 *
 * @author makejava
 * @since 2020-12-02 14:02:24
 */
@RestController
@RequestMapping("orderLogistics")
public class OrderLogisticsController extends ApiController  {
    /**
     * 服务对象
     */
    @Resource
    private OrderLogisticsService orderLogisticsService;

    /**
     * 分页查询所有数据
     *
     * @param page           分页对象
     * @param orderLogistics 查询实体
     * @return 所有数据
     */
    @GetMapping
    public R selectAll(Page<OrderLogistics> page, OrderLogistics orderLogistics) {
        return success(this.orderLogisticsService.page(page, new QueryWrapper<>(orderLogistics)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return success(this.orderLogisticsService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param orderLogistics 实体对象
     * @return 新增结果
     */
    @PostMapping
    public R insert(@RequestBody OrderLogistics orderLogistics) {
        return success(this.orderLogisticsService.save(orderLogistics));
    }

    /**
     * 修改数据
     *
     * @param orderLogistics 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R update(@RequestBody OrderLogistics orderLogistics) {
        return success(this.orderLogisticsService.updateById(orderLogistics));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R delete(@RequestParam("idList") List<Long> idList) {
        return success(this.orderLogisticsService.removeByIds(idList));
    }
}