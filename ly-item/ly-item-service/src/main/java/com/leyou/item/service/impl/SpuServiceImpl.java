package com.leyou.item.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leyou.common.dto.PageDTO;
import com.leyou.common.entity.Category;
import com.leyou.common.exception.LyException;
import com.leyou.item.dto.QuerySpuByPageDTO;
import com.leyou.item.dto.SkuDTO;
import com.leyou.item.dto.SpuDTO;
import com.leyou.item.dto.SpuDetailDTO;
import com.leyou.item.entity.*;
import com.leyou.item.mapper.SpuMapper;
import com.leyou.item.service.*;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author 虎哥
 */
@Service
public class SpuServiceImpl extends ServiceImpl<SpuMapper, Spu> implements SpuService {
    @Autowired
    BrandService brandService;
    CategorySerice categoryService;
    @Override
    public Page<SpuDTO> querySpecByPage(QuerySpuByPageDTO querySpuByPageDTO) {
        Long brandId = querySpuByPageDTO.getBrandId();
        Boolean saleable = querySpuByPageDTO.getSaleable();
        Long categoryId = querySpuByPageDTO.getCategoryId();
        Long id = querySpuByPageDTO.getId();
        Integer rows = querySpuByPageDTO.getRows();
         Integer page = querySpuByPageDTO.getPage();
        Page<Spu> result = query()
                .eq(saleable != null, "saleable", saleable)
                .eq(categoryId != null, "cid3", categoryId)
                .eq(brandId != null, "brand_id", brandId)
                .eq(id != null, "id", id)
                .page(new Page<>(page, rows));//limit
        long total = result.getTotal();
        long pages = result.getPages();
        List<Spu> list = result.getRecords();
        List<SpuDTO> dtoList = SpuDTO.convertEntityList(list);
        for (SpuDTO spuDTO : dtoList) {

            handleCategoryAndBrandName(spuDTO);

        }
        return null;
    }

    private void handleCategoryAndBrandName(SpuDTO spuDTO) {
        // 根据品牌id查询品牌名称

        Brand brand = brandService.getById(spuDTO.getBrandId());//根据id去品牌表中去查
        if (brand!=null) {
            spuDTO.setBrandName(brand.getName());

        }
        // 根据三级分类id查询分类集合
        List<Category> categories = categoryService.listByIds(spuDTO.getCategoryIds());
        if (!CollectionUtils.isEmpty(categories)) {
            String names = categories.stream().map(Category::getName).collect(Collectors.joining("/"));
            spuDTO.setCategoryName(names);


        }
    }
}
