package com.leyou.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author 虎哥
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)// 就是说默认计算的字段是非静态非瞬时的所有 但是加上了这个之后 计算字段仅仅看include这个元素的值了 这边啥都没写那么就啥都不计算
public class BaseEntity {
    private Date createTime;
    private Date updateTime; //后台和数据库的日期格式不用管  但是前台和后台的日期类型需要注意

}
