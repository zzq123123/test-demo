package com.leyou.search.service;

import com.leyou.search.dto.SearchParamDTO;
import com.leyou.search.entity.Goods;
import com.leyou.starter.elastic.dto.PageInfo;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * @author 虎哥
 */
public interface SearchService {
    /**
     * 创建索引库并设置映射
     */
    void createIndexAndMapping();

    /**
     * 加载数据到索引库
     */
    void loadData();

    Mono<List<String>> getSUggestion(String key);

    Mono<PageInfo<Goods>> searchGoods(SearchParamDTO param);

    Mono<Map<String, List<?>>> getFilterList(SearchParamDTO request );

    void saveSpuById(Long spuId);

    void deleteById(Long spuId);
}