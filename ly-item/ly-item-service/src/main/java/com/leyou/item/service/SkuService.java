package com.leyou.item.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leyou.item.dto.SkuDTO;
import com.leyou.item.entity.Sku;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author 虎哥
 */
public interface SkuService extends IService<Sku> {
    List<SkuDTO> querySkuBySpuId(Long id);

    void deductStock(Map<Long, Integer> cartMap);
}
