package com.leyou.item.service.impl;
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
import com.thoughtworks.xstream.core.util.ThreadSafeSimpleDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
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
    @Autowired
    CategorySerice categoryService;
    @Autowired
    SkuService skuService;
    @Autowired
    SpuDetailService detailservice;
    @Override
    public  PageDTO<SpuDTO>  querySpecByPage(QuerySpuByPageDTO querySpuByPageDTO) {
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
//        return new PageDTO<>(result.getTotal(), result.getPages(), dtoList);
        return new PageDTO<SpuDTO>(result.getTotal(), result.getPages(), dtoList);
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
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveGoods(SpuDTO spuDTO) {
        Spu spu = spuDTO.toEntity(Spu.class);
        spu.setSaleable(false);//下架
        boolean success = this.save(spu);
        if (!success) {
            throw new LyException(500, "新增商品失败");
        }
        SpuDetailDTO spuDetail = spuDTO.getSpuDetail();
        //一对一 不忽略你的id
        spuDetail.setSpuId(spu.getId());
        success = detailservice.save(spuDetail.toEntity(SpuDetail.class));
        if (!success) {
            throw new LyException(500, "新增商品失败");
        }
        List<SkuDTO> skus = spuDTO.getSkus();
        List<Sku> skuList = skus.stream().map(skuDTO -> {
            Sku sku = skuDTO.toEntity(Sku.class);
            sku.setSpuId(spu.getId());
            sku.setSold(0L);
            sku.setSaleable(false);
            return sku;
        }).collect(Collectors.toList());
        skuService.saveBatch(skuList);
    }
    @Transactional
    @Override
    public void updateSaleble(Long spuId, Boolean saleable) {
        //update tb_spu p set p.saleable = 1 where p.id =1
        Spu spu = new Spu();
        spu.setId(spuId);
        spu.setSaleable(saleable);
        boolean success = this.updateById(spu);
        if (!success) {
            throw new LyException(500, "上下架更新失败");
        }

        //update tb_sku p set p.saleable = 1 where p.sku_id =1
        skuService.update().set("saleable", saleable).eq("sku_id", spuId);
    }
    @Transactional
    @Override
    public SpuDTO queryGoodsById(Long id) {
        Spu spu = this.getById(id);
        SpuDTO spuDTO = new SpuDTO(spu);
        handleCategoryAndBrandName(spuDTO);
        //分步查询 单表查询性能好
        spuDTO.setSpuDetail(detailservice.queryDetailBySpuId(id));
        ;
        spuDTO.setSkus((skuService.querySkuBySpuId(id)));
        return spuDTO;
    }


/*- 是否包含spu信息
- 是否包含spuDetail信息
- 是否包含sku信息
  - sku中有没有saleable：
    - 有则删除的sku，
    - 没有则是增或改的sku
      - 有id：改
      - 无id：增*/

    @Transactional
    @Override
    public void updateGoods(SpuDTO spuDTO) {
        if (spuDTO.getId()!=null) {  //如果有id就是要修改 但是不可以修改able
            //spu数据需要 ,转换为po
            Spu spu = spuDTO.toEntity(Spu.class);
            spu.setSaleable(null); //mp 设置not null
            boolean success = updateById(spu);
            if (success==false) {
                throw new LyException(500, "更新商品失败");
            }
        }

        SpuDetailDTO spuDetailDTO = spuDTO.getSpuDetail();

        if (spuDetailDTO!=null && spuDetailDTO.getSpuId()!=null) {
        //detail存在 且有id存在 那么修改
            SpuDetail spuDetail = spuDetailDTO.toEntity(SpuDetail.class);


            boolean success = detailservice.updateById(spuDetail);

            if (!success) {
                throw new LyException(500, "更新商品失败");
            }
        }

        //3.修改sku
        List<SkuDTO> dtoList = spuDTO.getSkus();
        if (CollectionUtils.isEmpty(dtoList)) {
            return;
        }
       // 转换DTO，并将sku根据saleable是否为null来分组。null，是新增或修改，不是null是删除
        Map<Boolean, List<Sku>> map = dtoList.stream().map(skuDTO -> skuDTO.toEntity(Sku.class)).collect(Collectors.groupingBy(sku -> sku.getSaleable() == null));//根据true 和false来分组

        List<Sku> insertOrUpdateList = map.get(true);//不存在 null, mp忽略掉
        if (!CollectionUtils.isEmpty(insertOrUpdateList)) {
            skuService.saveOrUpdateBatch(insertOrUpdateList);
        }

        List<Sku> deleteSkuList = map.get(false);//存在 必须是1 直接根据id删除
        if(!CollectionUtils.isEmpty(deleteSkuList)){
            List<Long> idList = deleteSkuList.stream().map(Sku::getId).collect(Collectors.toList());
            skuService.removeByIds(idList);
        }
    }
}