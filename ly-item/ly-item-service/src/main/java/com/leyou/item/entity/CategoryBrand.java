package com.leyou.item.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @author 虎哥
 */
@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@TableName("tb_category_brand")
public class CategoryBrand {
    // IdType.INPUT，代表主键采用自己填写而不是自增长。
    @TableId(type = IdType.INPUT)
    private Long categoryId;
   @TableField
    private Long brandId;
}