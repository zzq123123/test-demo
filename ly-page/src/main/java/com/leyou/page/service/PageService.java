package com.leyou.page.service;

import java.util.List;

/**
 * Package: com.leyou.page.service
 * Description：  以分类 和spu 为核心
 * Author: wude
 * Date:  2020-11-27 12:26
 * Modified By:
 */
public interface PageService {
    /**
     * 加载spu到redis中并返回
     *
     * @param spuId 商品id
     */
    String loadSpuData(Long spuId);

    /**
     * 加载spuDetail到redis中并返回
     *
     * @param spuId 商品id
     */
    String loadSpuDetailData(Long spuId);

    /**
     * @param spuid 商品的id
     * @return {@link java.lang.String}
     * @description 加载sku信息到redis中并且返回
     */
    String loadSkuListData(Long spuid);

    /**
     * @param ids 商品的分类id
     * @return {@link java.lang.String}
     * @description 加载分类信息到redis中并且返回
     */
    String loadCategoriesData(List<Long> ids);

    /**
     * @param brandId 品牌id
     * @return {@link java.lang.String}
     */
    String lodaBrandData(Long brandId);

    /**
     * 加载规格参数信息到redis中并返回
     *
     * @param categoryId 商品第三级分类id
     */
    String loadSpecData(Long categoryId);

    /**
     * @param spuId spu的id sku要删一起删 要增一起增 要改一起改
     * @return
     * 删除ski集合
     */
    void deleteSkuList(Long spuId);

     String saveBrand2Redis(Long id, String json, String brandKeyPrefix);

    void deleteBrandById(Long id);
}
