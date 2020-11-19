package com.leyou.item.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leyou.item.entity.CategoryBrand;

/**
 * Package: com.leyou.item.service
 * Description：
 * Author: wude
 * Date:  2020-11-14 21:33
 * Modified By:
 */
public interface CategoryBrandService extends IService<CategoryBrand> {
    void removeBrand(Long brandId);
    //这里有各种增删改查的方法
}
