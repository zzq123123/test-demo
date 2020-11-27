package com.leyou.search.web;

import com.leyou.search.dto.SearchParamDTO;
import com.leyou.search.entity.Goods;
import com.leyou.search.service.SearchService;
import com.leyou.starter.elastic.dto.PageInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Package: com.leyou.search.web
 * Description：
 * Author: wude
 * Date:  2020-11-22 17:49
 * Modified By:
 */
@RestController
@RequestMapping("goods")
public class SearchController {
@Resource
    private SearchService searchService;

    /**
     * 初始化索引库
     */
    @GetMapping("initialization")
    public ResponseEntity<String> init() {
        searchService.createIndexAndMapping();
        searchService.loadData();
        return ResponseEntity.ok("导入成功");
    }


    @GetMapping("/suggestion")
    public Mono<List<String>> getSuggestion(@RequestParam("key") String key) {
        System.out.println("我被执行");
        return searchService.getSUggestion(key);  //异步调用
    }
    /**
     * 分页搜索商品数据
     * @param request 请求参数
     * @return 分页结果
     */
    //分词查询
    @PostMapping("/list")
    public Mono<PageInfo<Goods>> searchGoods(@RequestBody SearchParamDTO request) {
        return searchService.searchGoods(request);
    }

    /**
     * 查询顶部过滤项
     * @param request
     * @return
     */
    @PostMapping("/filter")
    public Mono<Map<String,List<?>>> getFilterList(@RequestBody SearchParamDTO request) {
        return searchService.getFilterList(request);
    }

}
