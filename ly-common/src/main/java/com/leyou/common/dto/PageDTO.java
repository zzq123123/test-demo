package com.leyou.common.dto;

import lombok.Data;

import java.util.List;

/**
 * 通用的分页结果对象
 * @param <T> 分页数据的类型
 */
@Data
public class PageDTO<T> {
//    前台一般需要的是List 和总条数  至于总页数的话可以计算的出
    private Long total;// 总条数
    private Long totalPage;// 总页数
    private List<T> items;// 当前页数据

    public PageDTO() {
    }

    public PageDTO(Long total, List<T> items) {
        this.total = total;
        this.items = items;
    }

    public PageDTO(Long total, Long totalPage, List<T> items) {
        this.total = total;
        this.totalPage = totalPage;
        this.items = items;
    }
}