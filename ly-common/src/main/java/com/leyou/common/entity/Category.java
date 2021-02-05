package com.leyou.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Package: com.leyou.common.entity
 * Description：
 * Author: wude
 * Date:  2020-11-14 20:09
 * Modified By:
 */
@Data
@EqualsAndHashCode(callSuper = false)  //计算的带父类字段
@TableName("tb_category")
public class Category  extends BaseEntity{
    @TableId
    private Long id;
    private String name;
    private Long parentId;
    private Boolean isParent;
    private Integer sort;
}
