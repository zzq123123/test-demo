package com.leyou.trade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.extension.handlers.MybatisEnumTypeHandler;
import com.leyou.common.entity.BaseEntity;
import com.leyou.trade.entity.enums.OrderStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

import java.util.Date;

/**
 * (Order)表实体类
 *
 * @author makejava
 * @since 2020-12-02 13:47:35
 */
@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tb_order")
public class Order    extends BaseEntity {
    //订单id
    @TableId(type= IdType.ASSIGN_ID)
    private Long orderId;
    //总金额，单位为分
    private Long totalFee;
    //实付金额。单位:分。如:20007，表示:200元7分
    private Long actualFee;
    //支付类型，1、微信支付，2、支付宝支付
    private Integer paymentType;
    //邮费。单位:分。如:20007，表示:200元7分
    private Long postFee;
    //用户id
    private Long userId;
    //订单的状态，1、未付款 2、已付款,未发货 3、已发货,未确认 4、确认收货，交易成功 5、交易取消，订单关闭 6、交易结束，已评价


    @TableField(typeHandler = MybatisEnumTypeHandler.class)
    private OrderStatus status;
    //创建时间
    private Date createTime;
    //支付时间
    private Date payTime;
    //发货时间
    private Date consignTime;
    //交易完成时间
    private Date endTime;
    //交易关闭时间
    private Date closeTime;
    //评价时间
    private Date commentTime;
    //更新时间
    private Date updateTime;


    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Long totalFee) {
        this.totalFee = totalFee;
    }

    public Long getActualFee() {
        return actualFee;
    }

    public void setActualFee(Long actualFee) {
        this.actualFee = actualFee;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public Long getPostFee() {
        return postFee;
    }

    public void setPostFee(Long postFee) {
        this.postFee = postFee;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Date getConsignTime() {
        return consignTime;
    }

    public void setConsignTime(Date consignTime) {
        this.consignTime = consignTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Date closeTime) {
        this.closeTime = closeTime;
    }

    public Date getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}