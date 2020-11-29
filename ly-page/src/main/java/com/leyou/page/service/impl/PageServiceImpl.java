package com.leyou.page.service.impl;

import com.leyou.common.exception.LyException;
import com.leyou.common.utils.JsonUtils;
import com.leyou.item.client.ItemClient;
import com.leyou.item.dto.*;
import com.leyou.page.service.PageService;
import com.leyou.page.vo.*;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Package: com.leyou.page.service.impl
 * Description：
 * Author: wude
 * Date:  2020-11-27 13:41
 * Modified By:
 */
@Service
public class PageServiceImpl implements PageService {

    @Resource
    private ItemClient itemClient;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    /**
     * 业务名   +   后置签名  + 前置签名
     */
    private static final String SPU_KEY_PREFIX = "page:spu:id:";
    private static final String DETAIL_KEY_PREFIX = "page:detail:id:";
    private static final String SKU_KEY_PREFIX = "page:sku:id:";
    private static final String CATEGORY_KEY_PREFIX = "page:category:id:";
    private static final String BRAND_KEY_PREFIX = "page:brand:id:";
    private static final String SPEC_KEY_PREFIX = "page:spec:id:";

    @Override
    public String loadSpuData(Long spuId) {
        //查spu信息
        SpuDTO spuDTO = itemClient.querySpuById(spuId);
        if (spuDTO == null || spuDTO.getId() == null) {
            throw new LyException(404, "商品不存在");
        }
        //转换vo
        SpuVO spuVO = spuDTO.toEntity(SpuVO.class);
        //序列化为json字符串
        String json = saveBrand2Redis(spuId, JsonUtils.toJson(spuVO), SPU_KEY_PREFIX);
        return json;
    }

    @Override
    public String loadSpuDetailData(Long spuId) {
        //查spuDetail信息
        SpuDetailDTO spuDetailDTO = itemClient.querySpuDetailById(spuId);
        if (spuDetailDTO == null) {
            throw new LyException(404, "商品不存在");
        }
        //没有vo我们detail的数据在详情页面都要
        //序列化为json字符串
        String json = saveBrand2Redis(spuId, JsonUtils.toJson(spuDetailDTO), DETAIL_KEY_PREFIX);
        return json;
    }

    @Override
    public String loadSkuListData(Long spuid) {
        //查sku信息
        List<SkuDTO> skuDTOList = itemClient.querySkuBySpuId(spuid);
        if (skuDTOList == null || skuDTOList.size() <= 0) {
            throw new LyException(404, "商品不存在");
        }
        //sku需要在商品详情页面上展示 不需要vo每个信息都有用到
        //序列化为json字符串
        String json = saveBrand2Redis(spuid, JsonUtils.toJson(skuDTOList), SKU_KEY_PREFIX);
        return json;
    }

    @Override
    public String loadCategoriesData(List<Long> ids) {
        //查询category信息
        List<CategoryDTO> categoryDTOList = itemClient.queryCategoryByIds(ids);
        if (categoryDTOList == null || categoryDTOList.size() <= 0) {
            throw new LyException(404, "商品不存在");
        }
        //分类只要名字和id其余的不需要
        List<CategoryVO> categoryVOList = categoryDTOList.stream().map(categoryDTO -> categoryDTO.toEntity(CategoryVO.class))
                .collect(Collectors.toList());
        //序列化为json字符串
        String json = saveBrand2Redis(ids.get(2), JsonUtils.toJson(categoryVOList), CATEGORY_KEY_PREFIX);
        return json;
    }

    @Override
    public String lodaBrandData(Long id) {
        //查category信息
        BrandDTO brandDTO = itemClient.queryBrandById(id);
        if (brandDTO == null) {
            throw new LyException(404, "商品不存在");
        }

        //转换vo
        BrandVO brandVO = brandDTO.toEntity(BrandVO.class);
        String json = saveBrand2Redis(id, JsonUtils.toJson(brandVO), BRAND_KEY_PREFIX);
        return json;
    }

    public String saveBrand2Redis(Long id, String json, String brandKeyPrefix) {
        //序列化为json字符串
        stringRedisTemplate.opsForValue().set(brandKeyPrefix + id, json);
        return json;
    }

    @Override
    public String loadSpecData(Long categoryId) {
        //根据分类id查询规格参数表 规格参数组
        List<SpecGroupDTO> specGroupDTOS = itemClient.querySpecList(categoryId);
        if (specGroupDTOS == null || specGroupDTOS.size() <= 0) {
            throw new LyException(404, "商品不存在");
        }
        //转换vo
        ArrayList<SpecGroupVO> groupVoList = new ArrayList<>(specGroupDTOS.size());
        for (SpecGroupDTO specGroup : specGroupDTOS) {
            //创建vo
            SpecGroupVO groupVO = new SpecGroupVO();
            //放入
            groupVoList.add(groupVO);
            //填写属性
            //name属性
            groupVO.setName(specGroup.getName());

            //params属性
            List<SpecParamVO> params = specGroup.getParams().stream().map(paramDTO -> paramDTO.toEntity(SpecParamVO.class))
                    .collect(Collectors.toList());
            groupVO.setParams(params);
        }
        String json = saveBrand2Redis(categoryId, JsonUtils.toJson(groupVoList), SPEC_KEY_PREFIX);
        return json;
    }


    @Override
    public void deleteSkuList(Long spuId) {
        stringRedisTemplate.delete(SKU_KEY_PREFIX + spuId);
    }

    @Override
    public void deleteBrandById(Long id) {
        stringRedisTemplate.delete(BRAND_KEY_PREFIX + id);
    }
}
