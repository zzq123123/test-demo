package com.leyou.trade.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.leyou.common.dto.BaseDTO;
import com.leyou.trade.entity.Order;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Package: com.leyou.trade.dto
 * Description：
 * Author: wude
 * Date:  2020-12-07 10:39
 * Modified By:
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class OrderDTOP extends BaseDTO {


        private Long orderId;
        private Long totalFee;
        private Long postFee;
        private Long actualFee;
        private Integer paymentType;
        private Long userId;
        private Integer status;
        private Integer statusCAS;
        private Date createTime;
        private Date payTime;
        private Date consignTime;
        private Date endTime;
         private Date closeTime;
        private Date commentTime;

        public OrderDTOP(Order entity) {
            super(entity);

    }
}
