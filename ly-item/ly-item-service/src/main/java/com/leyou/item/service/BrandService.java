package com.leyou.item.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leyou.common.dto.PageDTO;
import com.leyou.item.dto.BrandDTO;
import com.leyou.item.entity.Brand;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Package: com.leyou.item.service
 * Description：
 * Author: wude
 * Date:  2020-11-16 12:40
 * Modified By:
 */
@Service

public interface BrandService extends IService<Brand> {

    PageDTO<BrandDTO> queryBrandByPage(String key, Integer page, Integer rows);

    List<BrandDTO> queryBrandBycategory(Long id);

    void saveBrand(BrandDTO brandDTO);

    void updateBrand(BrandDTO brandDTO);
}
