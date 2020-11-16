package com.leyou.item.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leyou.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**LTAI4GE4ooPdyEQxAAF5Fg3U   ynK8M1BJaOxFNsgSKnZto9eogoN5vp
 * @auth 虎哥
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_brand")
public class Brand extends BaseEntity {
    @TableId
    private Long id;
    private String name;
    private String image;
    private Character letter;
}