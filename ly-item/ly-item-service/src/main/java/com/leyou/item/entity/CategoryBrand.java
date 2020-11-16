package com.leyou.item.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author 虎哥
 */
@Data
@AllArgsConstructor(staticName = "of")
@TableName("tb_category_brand")
public class CategoryBrand {
    @TableId(type = IdType.INPUT)
    private Long categoryId;
//    @TableId(type = IdType.INPUT)
    @TableField
    private Long brandId;
}