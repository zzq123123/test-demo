package com.leyou.item.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leyou.common.entity.Category;
import com.leyou.common.exception.LyException;
import com.leyou.item.dto.CategoryDTO;
import com.leyou.item.entity.CategoryBrand;
import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.service.CategoryBrandService;
import com.leyou.item.service.CategorySerice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
/**
 * Package: com.leyou.item.service.impl
 * Description：
 * Author: wude
 * Date:  2020-11-14 20:13
 * Modified By:
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategorySerice {
    @Autowired  //非动态代理多态对象
    CategoryBrandService categoryBrandService;

    @Override
    public List<CategoryDTO> queryCategoryBybrand(Long brandId) {
        List<CategoryBrand> categoryBrandList = categoryBrandService.query().eq("brand_id", brandId).list();
      /*  categoryBrandList.stream().map((a) -> {
            return a.getBrandId();
        });*/
        List<Long> categoryIds = categoryBrandList.stream().map(CategoryBrand::getCategoryId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(categoryIds)) {
            throw new LyException(500, "没有相关分类"); //in() 有错误
        }
        List<Category> categories = this.listByIds(categoryIds);
        List<CategoryDTO> categoryDTOS = CategoryDTO.convertEntityList(categories);
        return categoryDTOS;
    }
}
