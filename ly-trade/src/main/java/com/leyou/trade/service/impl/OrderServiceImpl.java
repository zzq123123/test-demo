package com.leyou.trade.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leyou.auth.local.UserContext;
import com.leyou.common.exception.LyException;
import com.leyou.item.client.ItemClient;
import com.leyou.item.dto.SkuDTO;
import com.leyou.trade.Mapper.OrderMapper;
import com.leyou.trade.dto.OrderFormDTO;
import com.leyou.trade.entity.Order;
import com.leyou.trade.entity.OrderDetail;
import com.leyou.trade.entity.OrderLogistics;
import com.leyou.trade.entity.enums.OrderStatus;
import com.leyou.trade.service.OrderDetailService;
import com.leyou.trade.service.OrderService;
import com.leyou.trade.utils.PayHelper;
import com.leyou.user.client.UserClient;
import com.leyou.user.dto.AddressDTO;
import feign.FeignException;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.*;
import static com.leyou.common.constants.MQConstants.ExchangeConstants.ORDER_EXCHANGE_NAME;
import static com.leyou.common.constants.MQConstants.RoutingKeyConstants.EVICT_ORDER_KEY;
import static com.leyou.trade.constants.PayConstants.ORDER_NO_KEY;
import static com.leyou.trade.constants.PayConstants.TOTAL_FEE_KEY;

/**
 * (Order)表服务实现类
 *
 * @author makejava
 * @since 2020-12-02 13:47:35
 */
@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
 @Resource
 private OrderDetailServiceImpl orderDetailServiceImpl;   //动态代理
 @Resource
 PayHelper payHelper;

 @Resource
 private ItemClient itemClient;

 @Resource
 private UserClient userClient;
@Autowired
OrderLogisticsServiceImpl  logisticsService;
    @Autowired
    AmqpTemplate amqpTemplate;

    @Autowired
    CartServiceImpl cartService;
    @Resource
    OrderDetailService orderDetail;

 @Override
 @GlobalTransactional
 public Long createOrder(OrderFormDTO orderDTO) {
  //批量新增用list就好 批量修改 需要复杂 有jdbc 和mp两种
  //写order
  Order order = new Order();
  //用户id
  Long userId = UserContext.getUser().getId();
  order.setUserId(userId);
  //金钱
  Map<Long, Integer> carts = orderDTO.getCarts();
  //获取sku id集合
  ArrayList<Long> idList = new ArrayList<>(carts.keySet());
     List<SkuDTO> skuList = itemClient.querySkuByIds(idList);  //每次执行完都提交事物 查询回滚和不会滚都没区别 e
  //计算金额
  long total = 0;
  for (SkuDTO skuDTO : skuList) {
   int num = carts.get(skuDTO.getId());
   total += skuDTO.getPrice() * num;

  }

  //填写金额数据
  order.setTotalFee(total);
  order.setPaymentType(orderDTO.getPaymentType());
  order.setPostFee(0L);
  order.setActualFee(total+order.getPostFee()/* - 优惠金额*/);
     order.setStatus(1); //这个enum的value和数据库绑定了 和前段的绑定需要自己注意
  boolean success = this.save(order);
  //order 主键回填 在下面一对多种会有用处
  if (!success) {
   throw new LyException(500, "订单创建失败");
  }
//写orderdetail
  //定义一个orderdetail集合  order 和 此 一对多

  ArrayList<OrderDetail> details = new ArrayList<>();
  for (SkuDTO skuDTO : skuList) {
//每个商品的数量
   int num = carts.get(skuDTO.getId());
   //组装Orderdetail
   OrderDetail detail = new OrderDetail();
   detail.setOrderId(order.getOrderId());
   detail.setNum(num);
   detail.setSkuId(skuDTO.getId());
   detail.setSpec(skuDTO.getSpecialSpec());
   detail.setPrice(skuDTO.getPrice());
   detail.setTitle(skuDTO.getTitle());
   details.add(detail);
  }

  //批量新增
  //局部变量废物利用
  success = orderDetailServiceImpl.saveBatch(details);
  if (!success) {
   //抛出 运行是异常
   throw new LyException(500,"订单创建失败");

  }

  //写orderLogistics


  //查询收货地址
     AddressDTO address = userClient.queryAddressById(orderDTO.getAddressId());  //commit 才算事务结束 才算锁结束
  //填写物流信息
  OrderLogistics logistics = address.toEntity(OrderLogistics.class);

     logistics.setOrderId(order.getOrderId()); //最大限度绑定原则  不是同一个jvm就不是事务的传播了
   success = logisticsService.save(logistics);
  if (success == false) {
   throw new LyException(500, "订单创建失败");
  }


  try {
   itemClient.deductStock(carts);  //只有一个远程调用 放在最后 你要放在第一个 那么会出现其他微服务事务提交了 你下面本微服务报错和其他微服务没关系的(在不引入seeate包的时候是这样的)
  } catch (FeignException e) {
   throw new LyException(e.status(),e.contentUTF8());
  }
     amqpTemplate.convertAndSend(ORDER_EXCHANGE_NAME, EVICT_ORDER_KEY, order.getOrderId());

     //发送id 到 普通交换机

//清空前台购物车 前台购物车里的商品在 local storage 怎么才能删除呢>  前台在发送pst list就清楚了
     /*下单完成删除购物车里的detail*/

/*
     List<String> collect = idList.stream().map(i -> Long.toString(i)).collect(Collectors.toList());
*/
     cartService.deleteCarts(idList);
  return order.getOrderId();
 }

 @Override
 public String getPayUrl(Long orderId) {
  // 根据id查询订单
  Order order = this.getById(orderId);
  //判断是否存在
  if (order == null) {
   throw new LyException(400, "订单编号错误，订单不存在！");
  }
  // 判断订单状态是否是未付款
     if (order.getStatus() != 1) {
   // 订单已经关闭或者已经支付，无需再次获取支付链接
   throw new LyException(400, "订单已经支付或者关闭！");
  }
  // TODO 尝试读取redis中的支付url加上缓存 用模块名 加上方法名 以及参数名为key url为 v存起来 删除时最后删 添加是最后加 修改时最后修改
  // 获取订单金额
  Long actualFee = order.getActualFee();

  // 统一下单，获取支付链接
     String url = payHelper.unifiedOrder(orderId, 1L);
             // TODO 把支付的url缓存在redis中，2小时有效期
     return url;
 }
    /*
    *
    * - 业务标示判断
    - 签名校验
    - 数据校验
      - 订单号码校验
      - 订单金额校验
    - 更新订单状态（需要保证幂等）*/
    @Override
    public void handleNotify(Map<String, String> data) {
        // 1.业务标示校验  return是业务通信校验

        payHelper.checkResultCode(data);

        //2.签名校验

        try {
            payHelper.checkSignature(data);
        } catch (Exception e) {
            throw new LyException(400, "签名校验有错误");  //重新让wx再发
        }

        // 3.订单状态校验（保证幂等，防止重复通知）
        String outTrandeNo = data.get(ORDER_NO_KEY);  //订单编号
        String totalFee = Long.toString(1L         /*actualFee*/);  //如果有异常 抛给jvm去打印 检查是异常 是经常发生的非逻辑错误可以解决如果你解决不掉就转成逻辑异常 抛给jvm得到异常消息
        data.get(TOTAL_FEE_KEY);   //总金额
        if (StringUtils.isBlank(outTrandeNo) || StringUtils.isBlank(totalFee)) {
//            throw new LyException(400,"响应数据有误,订单金额或者编号为空");
            throw new RuntimeException("响应数据有误，订单金额或编号为空！"); //者code 自动维护?

        }

        Long orderId = Long.valueOf(outTrandeNo);
        Order order = this.getById(orderId);
        if (!order.getStatus().equals(OrderStatus.INIT.toString())) {
            // 说明订单已经支付过了，属于重复通知，直接返回 或者已经关闭了
            return; //直接给WX返回成功 结束了
        }
        Long total = Long.valueOf(totalFee);
        if (!total.equals(order.getActualFee())) {
            //我给你的是这个钱 你当然要返回给我这个价钱让我做最后的确认如果有误我可以阻止我的业务拒绝状态为付款状态并且告诉微信异常消息
            throw new RuntimeException("订单金额有误 我要报警了");
        }

        //来到这里 说明通信正常 业务结果正常 签名正常 价钱正常 状态正常
        //修改订单状态 更新状态和支付时间两个字段  注意乐观锁保证幂等性
        this.update()
                .set("status", OrderStatus.PAY_UP.getValue()).set("pay_time", new Date())
                .eq("order_id", orderId).eq("status", OrderStatus.INIT.getValue()
        )
        ;
        log.info("处理微信支付通知成功!{}", data);
    }

    @Override
    public Integer queryOrderState(Long orderId) {
        //查询订单
        Order order = this.getById(orderId);
        //判断是否存在
        if (order == null) {
            throw new LyException(400, "订单不存在");
        }
        return order.getStatus();
    }



    @Override
    @Transactional
    public void evictOrderIfNecessary(Long orderId) {
        //查询订单
        Order order = this.getById(orderId);
        if (order == null) {
            //订单不存在说明
            log.warn("要清理的订单不存在！订单id: {}", orderId);
            return;
        }

        //判断订单是否支付
        if (!order.getStatus().equals(OrderStatus.INIT.getValue())) {
            return;
        }
        // 3.订单未支付，需要关闭订单
        // update tb_order set status = 5, close_time = NOW() where id = x AND status = 1
        boolean success = update().set("status", OrderStatus.CLOSED.getValue())
                .set("close_time", new Date())
                .eq("order_id", orderId)
                .eq("status", OrderStatus.INIT.getValue())
                .update();
       /* HashMap<String , Object> updateOrdermap = new HashMap<>(4);
//tb_order set status = #{statusp}, close_time = #{closeTimep} where id = #{orderIdp} AND status = #{statusi}
        updateOrdermap.put("statusp",   OrderStatus.CLOSED.getValue());
        updateOrdermap.put("closeTimep",   new Date());
        updateOrdermap.put("orderIdp",   orderId);
        updateOrdermap.put("statusi",   OrderStatus.INIT.getValue());
*/
   /*     OrderDTOP orderDTOP = new OrderDTOP();
        orderDTOP.setStatus(OrderStatus.CLOSED.getValue());
        orderDTOP.setCloseTime(new Date());
        orderDTOP.setStatusCAS( OrderStatus.INIT.getValue());
        orderDTOP.setOrderId(orderId);

        Integer ints = this.getBaseMapper().updateOrder(orderDTOP);*/
        if (success == false) {
            // 更新失败，订单状态已经改变，无需处理
            return;

        }
        log.info("已关闭超时未支付订单：{}", orderId);

        // 4.查询OrderDetail 动态多态对象
        List<OrderDetail> details = orderDetail.query()
                .eq("order_id", orderId).list();

        //5.获取商品及其商品数量信息

        Map<Long, Integer> skuMap = new HashMap<>();

        //得到其中的商品和数量信息

        for (OrderDetail detail : details) {
            skuMap.put(detail.getSkuId(), detail.getNum());

        }
        itemClient.addStock(skuMap);
    }

    @Override
    public Integer activeQuery2Wx(Long id) {

        return null;
    }

}
