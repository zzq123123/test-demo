package com.leyou.search.service;

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
}