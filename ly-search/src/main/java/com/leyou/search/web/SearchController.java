package com.leyou.search.web;

import com.leyou.search.service.SearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
    private   SearchService searchService;

    /**
     * 初始化索引库
     */
    @GetMapping("initialization")
    public ResponseEntity<String> init() {
        searchService.createIndexAndMapping();
        searchService.loadData();
        return ResponseEntity.ok("导入成功");
    }




}
