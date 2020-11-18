package com.leyou.item.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leyou.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Package: com.leyou.item.entity
 * Description：
 * Author: wude
 * Date:  2020-11-17 13:12
 * Modified By:
 */
@TableName("tb_spec_param")
@Data
@EqualsAndHashCode(callSuper = false)
public class SpecParam extends BaseEntity {
    @TableId
    private Long id;
    private Long categoryId;
    private Long groupId;
    @TableField("`name`")//创建和查询用`name`替代name
    private String name;
    @TableField("`numeric`")
    private Boolean numeric;
    private String unit;
    private Boolean generic;
    private Boolean searching;
    private String segments;
    private String options;

}
