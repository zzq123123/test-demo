package com.leyou.item.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leyou.common.entity.Category;
import com.leyou.item.dto.CategoryDTO;

import java.util.List;

/**
 * Package: com.leyou.item.service
 * Description：
 * Author: wude
 * Date:  2020-11-14 20:12
 * Modified By:
 */
public interface CategorySerice extends IService<Category> {
    List<CategoryDTO> queryCategoryBybrand(Long brandId);
}
