package com.leyou.page.vo;

import lombok.Data;

import java.util.List;

/**
 * @author 虎哥
 */
@Data
public class SpuVO {
    //存一点必要的数据
    private Long id;
    private String name;
    private List<Long> categoryIds;
    private Long brandId;
}