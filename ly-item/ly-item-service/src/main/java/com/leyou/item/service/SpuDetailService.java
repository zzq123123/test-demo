package com.leyou.item.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leyou.item.dto.SpecParamDTO;
import com.leyou.item.dto.SpuDetailDTO;
import com.leyou.item.entity.SpuDetail;

import java.util.List;

/**
 * @author 虎哥
 */
public interface SpuDetailService extends IService<SpuDetail> {
    SpuDetailDTO queryDetailBySpuId(Long id);

    List<SpecParamDTO> querySpecValues(Long id, Boolean searching);
}
