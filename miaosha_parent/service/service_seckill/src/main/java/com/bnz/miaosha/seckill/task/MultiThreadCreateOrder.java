package com.bnz.miaosha.seckill.task;

import com.bnz.miaosha.entity.SeckillStatus;
import com.bnz.miaosha.seckill.pojo.Order;
import com.bnz.miaosha.seckill.pojo.SeckillGoods;
import com.bnz.miaosha.seckill.service.SecKillOrderService;
import com.bnz.miaosha.seckill.utils.PayStatus;
import com.bnz.miaosha.seckill.utils.RedisConstans;
import com.bnz.miaosha.seckill.utils.RedisUtil;
import com.bnz.miaosha.utils.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.UUID;

@Slf4j
@Component
public class MultiThreadCreateOrder {

    @Autowired
    private RedisUtil redisUtil;

    @Resource
    private RedissonClient redissonClient;

    @Autowired
    private IdWorker idWorker;

    @Resource
    private SecKillOrderService secKillOrderService;

    //@Async(value = "taskExecutor")
    public void createOrder() throws Exception {

        // 取排队信息
        SeckillStatus seckillStatus = (SeckillStatus) redisUtil.rPop(RedisConstans.SECKILL_ORDER_QUEUE);
        if ( null == seckillStatus ){
            log.info("[多线程抢单]--MultiThreadCreateOrder 暂无排队信息");
            return;
        }
        String time = seckillStatus.getTime();
        String commodityCode = seckillStatus.getCommodityCode();
        String username = seckillStatus.getUsername();

        // 前置商品校验
        RMap<String, SeckillGoods> goodsMap = redissonClient.getMap(RedisConstans.SECKILL_GOODS_KEY + time);
        SeckillGoods goods = goodsMap.get(commodityCode);
        if (null == goods){
            log.info("[多线程抢单]--MultiThreadCreateOrder  username={},time={},commodityCode={},对应的商品信息不存在,返回下单失败", username,time, commodityCode);
            return;
        }

        // 前置预减redis库存
        if (!secKillOrderService.preReduceGoodsStock(commodityCode)){
            return;
        }

        //消息体: 秒杀订单
        String uuid = UUID.randomUUID().toString();
        Order order = new Order();
        order.setId(idWorker.nextId());
        System.out.println("orderId: "+order.getId());
        order.setCommodityCode(commodityCode);
        order.setUserId(username);
        order.setTxNum(uuid);
        order.setMoney(goods.getCostPrice());
        order.setStatus(PayStatus.NO_PAID); //待支付
        order.setCount(1);

        // 更新状态
        seckillStatus.setMoney(goods.getCostPrice());
        seckillStatus.setOrderId(order.getId());
        seckillStatus.setStatus(PayStatus.IN_THE_LINE); // 排队中
        secKillOrderService.putSeckillStatus(username,seckillStatus);

        //发送消息(保证消息生产者对于消息的不丢失实现)
        secKillOrderService.sendOrder(order);
    }
}
