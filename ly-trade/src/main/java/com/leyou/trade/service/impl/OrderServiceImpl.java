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
import com.leyou.trade.service.OrderService;
import com.leyou.trade.utils.PayHelper;
import com.leyou.user.client.UserClient;
import com.leyou.user.dto.AddressDTO;
import feign.FeignException;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * (Order)表服务实现类
 *
 * @author makejava
 * @since 2020-12-02 13:47:35
 */
@Service
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
  List<SkuDTO> skuList = itemClient.querySkuByIds(idList);
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
  order.setStatus(OrderStatus.INIT); //这个enum的value和数据库绑定了 和前段的绑定需要自己注意
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
  AddressDTO address = userClient.queryAddressById(orderDTO.getAddressId());
  //填写物流信息
  OrderLogistics logistics = address.toEntity(OrderLogistics.class);

  logistics.setOrderId(order.getOrderId()); //最大限度绑定原则
   success = logisticsService.save(logistics);
  if (success == false) {
   throw new LyException(500, "订单创建失败");
  }


  try {
   itemClient.deductStock(carts);  //只有一个远程调用 放在最后 你要放在第一个 那么会出现其他微服务事务提交了 你下面本微服务报错和其他微服务没关系的(在不引入seeate包的时候是这样的)
  } catch (FeignException e) {
   throw new LyException(e.status(),e.contentUTF8());
  }

//清空前台购物车 前台购物车里的商品在 local storage 怎么才能删除呢>
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
  if (order.getStatus() != OrderStatus.INIT) {
   // 订单已经关闭或者已经支付，无需再次获取支付链接
   throw new LyException(400, "订单已经支付或者关闭！");
  }
  // TODO 尝试读取redis中的支付url加上缓存 用模块名 加上方法名 以及参数名为key url为 v存起来 删除时最后删 添加是最后加 修改时最后修改
  // 获取订单金额
  Long actualFee = order.getActualFee();

  // 统一下单，获取支付链接
  String url = payHelper.unifiedOrder(orderId, actualFee);  //如果有异常 抛给jvm去打印 检查是异常 是经常发生的非逻辑错误可以解决如果你解决不掉就转成逻辑异常 抛给jvm得到异常消息

  // TODO 把支付的url缓存在redis中，2小时有效期

  return url;
 }
}