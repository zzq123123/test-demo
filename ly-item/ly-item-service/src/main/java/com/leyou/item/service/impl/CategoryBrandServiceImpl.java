package com.leyou.item.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leyou.common.entity.Category;
import com.leyou.item.entity.CategoryBrand;
import com.leyou.item.mapper.CategoryBrandMapper;
import com.leyou.item.service.BrandService;
import com.leyou.item.service.CategoryBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Package: com.leyou.item.service.impl
 * Description：
 * Author: wude
 * Date:  2020-11-14 21:33
 * Modified By:
 */
@Service
public class CategoryBrandServiceImpl extends ServiceImpl<CategoryBrandMapper, CategoryBrand> implements CategoryBrandService {

    @Autowired
    private BrandService brandService;//非动态 多态
    @Autowired
    private CategoryBrandService categoryBrandService;
    @Override
    @Transactional
    public void removeBrand(Long brandId) {
        //先删除外键行 中间表      删除品牌
        categoryBrandService.remove(new QueryWrapper<CategoryBrand>().eq("brand_id", brandId));
        //再删除主键行
        brandService.removeById(brandId);
    }
}