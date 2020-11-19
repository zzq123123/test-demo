package com.leyou.item.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leyou.item.dto.SpuDetailDTO;
import com.leyou.item.entity.SpuDetail;
import com.leyou.item.mapper.SpuDetailMapper;
import com.leyou.item.service.SpuDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 虎哥
 */
@Service
public class SpuDetailServiceImpl extends ServiceImpl<SpuDetailMapper, SpuDetail> implements SpuDetailService {



    @Override
    @Transactional
    public SpuDetailDTO queryDetailBySpuId(Long id) {
        SpuDetail spuDetail = this.getById(id);
        SpuDetailDTO spuDetailDTO = new SpuDetailDTO(spuDetail);

        return spuDetailDTO;
    }
}
