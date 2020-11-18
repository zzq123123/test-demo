package com.leyou.item.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leyou.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.xml.bind.PrintConversionEvent;

/**
 * Package: com.leyou.item.entity
 * Description：
 * Author: wude
 * Date:  2020-11-17 13:10
 * Modified By:
 */
@TableName("tb_spec_group")
@Data
@EqualsAndHashCode(callSuper = false)
public class SpecGroup extends BaseEntity {
    @TableId
    private Long id;
    private Long categoryId;
    private String name;
}
