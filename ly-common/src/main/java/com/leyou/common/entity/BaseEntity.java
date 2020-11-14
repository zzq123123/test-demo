package com.leyou.common.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author 虎哥
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)// 提供比较方法和hash方法 不带父类属性
public class BaseEntity {
    private Date createTime;
    private Date updateTime; //后台和数据库的日期格式不用管  但是前台和后台的日期类型需要注意

}
