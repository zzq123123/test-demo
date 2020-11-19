package com.leyou.item.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leyou.item.dto.SpuDetailDTO;
import com.leyou.item.entity.SpuDetail;

/**
 * @author 虎哥
 */
public interface SpuDetailService extends IService<SpuDetail> {
    SpuDetailDTO queryDetailBySpuId(Long id);
}
