package com.leyou.item.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leyou.common.dto.PageDTO;
import com.leyou.common.exception.LyException;
import com.leyou.item.dto.BrandDTO;
import com.leyou.item.entity.Brand;
import com.leyou.item.entity.CategoryBrand;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.service.BrandService;
import com.leyou.item.service.CategoryBrandService;
import com.netflix.discovery.converters.Auto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;
/**
 * Package: com.leyou.item.service.impl
 * Description：
 * Author: wude
 * Date:  2020-11-16 12:41
 * Modified By:
 */
@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements BrandService {
    @Autowired
    CategoryBrandService categoryBrandService;
    @Override
    public PageDTO<BrandDTO> queryBrandByPage(String key, Integer page, Integer rows) {
        page = Math.min(page, 100);
        rows = Math.min(rows, 5);
        Page<Brand> info = new Page<>(page, rows);
        //判断key是否存在
        boolean isKeyExists = StringUtils.isNoneBlank(key);
        Page<Brand> page1 = this.query().like(isKeyExists, "name", key)
                .or()
                .eq(isKeyExists, "letter", key)
                .page(info);// limit
        //对象里面 的[] 里面放上BrandDTO就好了
        return new PageDTO<>(info.getTotal(),info.getPages(), BrandDTO.convertEntityList(info.getRecords()));//[-] -> [~]
    }

    @Override
    public List<BrandDTO> queryBrandBycategory(Long id) {
        List<Brand> brands = this.baseMapper.queryBycategoryId(id);
        List<BrandDTO> brandDTOS = BrandDTO.convertEntityList(brands);
        return brandDTOS;
    }
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveBrand(BrandDTO brandDTO) {
      //外键引用主键 先添加主键
        //先添加品牌表的对象 null 忽略
        Brand brand = brandDTO.toEntity(Brand.class);
        this.save(brand);
        //再添加中间表的对象
        //目标方法使用了 函数式接口就可以使用lamda表达式   当lamda表达式有一行(前置参数只有一个或者栈板上是调用者) 就可以使用 方法引用
        List<CategoryBrand> categoryBrands = brandDTO.getCategoryIds().stream().map(t -> {
            return CategoryBrand.of(t, brand.getId());
        }).collect(Collectors.toList());
        boolean b = categoryBrandService.saveBatch(categoryBrands);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateBrand(BrandDTO brandDTO) {
        //更新品牌
        boolean b = this.updateById(brandDTO.toEntity(Brand.class));
        if (b == false) {
            //异常信息链路不能断 编译异常不会触发事务回滚(只是针对throws)
            throw new LyException(500,"更新品牌失败");
        }
        //根据品牌id来删除中间表数据


          b = categoryBrandService.remove(new QueryWrapper<CategoryBrand>().eq("brand_id", brandDTO.getId()));
        if (b==false) {
            throw new LyException(500, "更新品牌失败,删除中间表失败");
        }

        //重新插入中间表数据

        List<CategoryBrand> categoryBrands = brandDTO.getCategoryIds().stream().map(t -> {
            return CategoryBrand.of(t, brandDTO.getId());
        }).collect(Collectors.toList());

      categoryBrandService.saveBatch(categoryBrands);

    }
}
