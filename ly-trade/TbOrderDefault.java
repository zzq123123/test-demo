#save("/entity", ".java")

#setPackageSuffix("entity")


import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

/**
 * @author Mr.Wang
 * @since 
 */
@Data
@ApiModel(description = "信息类")
public class TbOrder extends Model<TbOrder> implements Serializable {
    private static final long serialVersionUID = ;
    
    @ApiModelProperty("订单id")
    private Object orderId;
    
    @ApiModelProperty("总金额，单位为分")
    private Object totalFee;
    
    @ApiModelProperty("实付金额。单位:分。如:20007，表示:200元7分")
    private Object actualFee;
    
    @ApiModelProperty("支付类型，1、微信支付，2、支付宝支付")
    private Object paymentType;
    
    @ApiModelProperty("邮费。单位:分。如:20007，表示:200元7分")
    private Object postFee;
    
    @ApiModelProperty("用户id")
    private Object userId;
    
    @ApiModelProperty("订单的状态，1、未付款 2、已付款,未发货 3、已发货,未确认 4、确认收货，交易成功 5、交易取消，订单关闭 6、交易结束，已评价")
    private Object status;
    
    @ApiModelProperty("创建时间")
    private Date createTime;
    
    @ApiModelProperty("支付时间")
    private Date payTime;
    
    @ApiModelProperty("发货时间")
    private Date consignTime;
    
    @ApiModelProperty("交易完成时间")
    private Date endTime;
    
    @ApiModelProperty("交易关闭时间")
    private Date closeTime;
    
    @ApiModelProperty("评价时间")
    private Date commentTime;
    
    @ApiModelProperty("更新时间")
    private Date updateTime;

}