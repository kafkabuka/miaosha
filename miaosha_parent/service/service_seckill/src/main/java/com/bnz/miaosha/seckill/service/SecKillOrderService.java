package com.bnz.miaosha.seckill.service;

import com.bnz.miaosha.entity.Result;
import com.bnz.miaosha.entity.SeckillStatus;
import com.bnz.miaosha.seckill.pojo.Order;

public interface SecKillOrderService {

    // 秒杀下单
    Result add(String commodityCode, String time, String userId) throws Exception;

    // 秒杀下单前置商品校验
    boolean checkSeckillGoodsBeforeKill(String username,String commodityCode,String time);

    // 发送订单消息
    void sendOrder(Order order);

    // 插入订单
    void insertOrder(Order order) throws Exception;

    // 更新订单状态
    void updateOrder(String orderId,Integer status);

    // 预减redis库存
    boolean preReduceGoodsStock(String commodityCode) throws Exception;

    // 根据userId获取用户秒杀状态
    SeckillStatus getSeckillStatus(String userId);

    // 保存用户秒杀状态
    void putSeckillStatus(String userId,SeckillStatus seckillStatus);

    // 删除用户秒杀状态
    boolean delSeckillStatus(String userId);

    Order getOrder(Long orderId);

    int setOrder(Order order);
}
