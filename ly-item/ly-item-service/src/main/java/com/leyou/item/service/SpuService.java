package com.leyou.item.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.leyou.common.dto.PageDTO;
import com.leyou.item.dto.QuerySpuByPageDTO;
import com.leyou.item.dto.SpuDTO;
import com.leyou.item.entity.Spu;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 虎哥
 */
public interface SpuService extends IService<Spu> {
    PageDTO<SpuDTO>  querySpecByPage(QuerySpuByPageDTO querySpuByPageDTO);

    void saveGoods(SpuDTO spuDTO);

    void updateSaleble(Long spuId, Boolean saleable);

    SpuDTO queryGoodsById(Long id);

    void updateGoods(SpuDTO spuDTO);
}
