package com.leyou.trade.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
/**
 * Package: com.ly.trade.entity
 * Description：
 * Author: wude
 * Date:  2020-12-01 17:13
 * Modified By:
 */
@Data
@Document("#{@collectionNameBuilder.build()}")
public class CartItem {
    @Id
    @JsonIgnore
    private String id;
    @JsonIgnore
    private Long userId;
    //这个要展示给前端的
    private Long skuId;// 商品id
    private String title;// 标题
    private String image;// 图片
    private Long price;// 加入购物车时的价格
    private Integer num;// 购买数量
    private String spec;// 商品规格参数
    private Date updateTime;// 更新时间
}