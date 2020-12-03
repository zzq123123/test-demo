package com.leyou.item.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leyou.item.entity.Sku;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.Map;

/**
 * @author 虎哥
 */
public interface SkuMapper extends BaseMapper<Sku> {
    @Update("UPDATE tb_sku SET stock = stock - #{num}, sold = sold + #{num} WHERE id = #{id}")
    int deductStock(Map<String, Object> sku);  //可变形参 参数  自动绑定
}