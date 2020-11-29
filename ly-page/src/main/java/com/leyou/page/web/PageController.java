package com.leyou.page.web;


import com.leyou.page.service.PageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 虎哥
 */
@RestController
@RequestMapping("page")
public class PageController {

    @Resource
    PageService pageService;

    /**
     * 查询商品spu数据
     * @param spuId 商品id
     * @return spu数据
     */
    @GetMapping("/spu/{id}")
    public ResponseEntity<String> querySpuPageData(@PathVariable("id") Long spuId){
        return ResponseEntity.ok(pageService.loadSpuData(spuId));
    }

    /**
     * 查询商品sku数据
     * @param spuId 商品id
     * @return sku数据
     */
    @GetMapping("/sku/{id}")
    public ResponseEntity<String> querySkuPageData(@PathVariable("id") Long spuId){
        return ResponseEntity.ok(pageService.loadSkuListData(spuId));
    }

    /**
     * 查询商品spuDetail数据
     * @param spuId 商品id
     * @return spu数据
     */
    @GetMapping("/detail/{id}")
    public ResponseEntity<String> queryDetailPageData(@PathVariable("id") Long spuId){
        return ResponseEntity.ok(pageService.loadSpuDetailData(spuId));
    }

    /**
     * 查询商品分类数据
     * @param ids 商品分类id
     * @return 分类数据   传来的是数组字符串
     */
    @GetMapping("/categories")
    public ResponseEntity<String> queryCategoryPageData(@RequestParam("ids") List<Long> ids){
        return ResponseEntity.ok(pageService.loadCategoriesData(ids));
    }

    /**
     * 查询品牌数据
     * @param id 品牌id
     * @return spu数据
     */
    @GetMapping("/brand/{id}")
    public ResponseEntity<String> queryBrandPageData(@PathVariable("id") Long id){
        return ResponseEntity.ok(pageService.lodaBrandData(id));
    }

    /**
     * 查询规格数据
     * @param categoryId 分类id
     * @return 规格参数
     */
    @GetMapping("/spec/{id}")
    public ResponseEntity<String> queryGoodsPageData(@PathVariable("id") Long categoryId){
        return ResponseEntity.ok(pageService.loadSpecData(categoryId));
    }
}