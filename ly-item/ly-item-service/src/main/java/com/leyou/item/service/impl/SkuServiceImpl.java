package com.leyou.item.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leyou.item.dto.SkuDTO;
import com.leyou.item.entity.Sku;
import com.leyou.item.mapper.SkuMapper;
import com.leyou.item.service.SkuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author 虎哥
 */
@Service
public class SkuServiceImpl extends ServiceImpl<SkuMapper, Sku> implements SkuService {
    @Override
    @Transactional
    public List<SkuDTO> querySkuBySpuId(Long id) {
        List<Sku> skuList = this.query().eq("spu_id", id).list();
        List<SkuDTO> skuDTOS = SkuDTO.convertEntityList(skuList);
        return skuDTOS;
    }
}
