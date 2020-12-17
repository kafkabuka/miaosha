package com.bnz.miaosha.seckill.service.impl;

import com.alibaba.fastjson.JSON;
import com.bnz.miaosha.entity.CodeMsg;
import com.bnz.miaosha.entity.Result;
import com.bnz.miaosha.entity.SeckillStatus;
import com.bnz.miaosha.seckill.dao.OrderMapper;
import com.bnz.miaosha.seckill.pojo.Order;
import com.bnz.miaosha.seckill.pojo.SeckillGoods;
import com.bnz.miaosha.seckill.pojo.Storage;
import com.bnz.miaosha.seckill.service.SecKillOrderService;
import com.bnz.miaosha.seckill.service.StorageService;
import com.bnz.miaosha.seckill.task.MultiThreadCreateOrder;
import com.bnz.miaosha.seckill.utils.PayStatus;
import com.bnz.miaosha.seckill.utils.RedisConstans;
import com.bnz.miaosha.seckill.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SecKillOrderServiceImpl implements SecKillOrderService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private MultiThreadCreateOrder multiThreadCreateOrder;

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private StorageService storageService;

    @Override
    public Result add(String commodityCode, String time, String userId) throws Exception {
        /**
         *  1.获取redis中的商品信息与库存信息,并进行判断
         *  2.执行redis的预扣减库存操作,并获取扣减之后的库存值
         * 3.如果扣减之后的库存值<=0,则删除redis中响应的商品信息与库存信息
         * 4.基于mq完成mysql的数据同步,进行异步下单并扣减库存(mysql)*/

        /**
         * 查询用户订单状态
         *  秒杀状态 1：排队中 2：秒杀等待支付 3：支付超时 4：秒杀失败 5：支付完成
         */
        SeckillStatus seckillStatus = getSeckillStatus(userId);
        if (seckillStatus != null) {
            switch (seckillStatus.getStatus()) {
                case 1:
                    return Result.error(CodeMsg.ORDER_INLINE, seckillStatus);
                case 2:
                    return Result.error(CodeMsg.ORDER_NOT_PAY, seckillStatus);
                case 3:
                    return Result.error(CodeMsg.ORDER_PAY_OVERTIME, seckillStatus);
                case 4:
                    return Result.error(CodeMsg.ORDER_SECKILL_FAIL, seckillStatus);
            }
        }

        // 创建用户抢购排队信息
        seckillStatus = new SeckillStatus(userId, new Date(), 1, commodityCode, time);

        // 用户排队
        redisUtil.lPush(RedisConstans.SECKILL_ORDER_QUEUE, seckillStatus);

        // 用户抢单状态 --> 用于查询
        redisUtil.hset(RedisConstans.SECKILL_USER_STATUS_QUEUE, userId, seckillStatus);

        // 多线程下单
        multiThreadCreateOrder.createOrder();
        return Result.success(CodeMsg.ORDER_INLINE, seckillStatus);
    }

    @Override
    public boolean checkSeckillGoodsBeforeKill(String username, String commodityCode, String time) {
        // 商品校验
        RMap<String, SeckillGoods> goodsMap = redissonClient.getMap(RedisConstans.SECKILL_GOODS_KEY + time);
        SeckillGoods goods = goodsMap.get(commodityCode);
        if (null == goods) {
            log.info("username={},time={},commodityCode={},对应的商品信息不存在,返回下单失败", username, time, commodityCode);
            return false;
        }
        return true;
    }

    @Override
    public void sendOrder(Order order) {
        String str = JSON.toJSONString(order);
        Message<String> message = MessageBuilder.withPayload(str).build();
        // 发送一条事务消息
        rocketMQTemplate.sendMessageInTransaction("producer_group_tx1", "topic_tx", message, null);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertOrder(Order order) throws Exception {
        //创建锁对象
        RLock lock = redissonClient.getLock("insertOrder:" + order.getUserId() + order.getId());
        try {

            //尝试去获取锁
            RFuture<Boolean> booleanRFuture = lock.tryLockAsync(3, 10, TimeUnit.SECONDS);
            Boolean aBoolean = booleanRFuture.get();

            // 如果得到了锁
            if (aBoolean) {

                // 用事务id幂等处理
                if (null != orderMapper.selectById(order.getId())) {
                    log.info("[秒杀订单生产者]-当前订单已入库,不需要重复消费!,orderId={}", order.getId());
                    return;
                }

                int insert = orderMapper.insert(order);
                if (insert != 1) {
                    log.error("[orderMapper.insert(order)]-orderId:{},秒杀订单入库[失败],事务回滚", order.getId());
                    String message =
                            String.format("[orderMapper.insert(order)]-orderId=%s,秒杀订单入库[失败],事务回滚", order.getId());
                    throw new RuntimeException(message);
                }
            }
        } finally {
            // 释放锁
            lock.unlock();
        }
    }

    @Override
    public void updateOrder(String orderId, Integer status) {
        Order order = orderMapper.selectById(orderId);
        order.setStatus(status);
        orderMapper.updateById(order);
    }

    @Override
    public boolean preReduceGoodsStock(String commodityCode) throws Exception {
        RLock lock = redissonClient.getLock("storageBucket:" + commodityCode);
        Future<Boolean> res = lock.tryLockAsync(100, 10, TimeUnit.SECONDS);
        try {
            if (res.get()) {
                RBucket<Storage> storageBucket = redissonClient.getBucket(RedisConstans.SECKILL_GOODS_STOCK_COUNT_KEY + commodityCode);
                Storage storage = storageBucket.get();
                if (storage.getCount() <= 0) {
                    log.info("商品编号：{}，库存：{}", commodityCode, storage.getCount());
                    return false;
                }
                int count = storage.getCount() - 1;
                log.info("库存扣减库：{}，剩余：{}", 1, count);
                storage.setCount(count);
                storageBucket.set(storage);
            }
            return true;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void putSeckillStatus(String userId, SeckillStatus seckillStatus) {
        RMap<String, SeckillStatus> map = redissonClient.getMap(RedisConstans.SECKILL_USER_STATUS_QUEUE);
        map.put(userId, seckillStatus);
    }

    @Override
    public SeckillStatus getSeckillStatus(String userId) {
        RMap<String, SeckillStatus> map = redissonClient.getMap(RedisConstans.SECKILL_USER_STATUS_QUEUE);
        return map.get(userId);
    }

    @Override
    public boolean delSeckillStatus(String userId) {
        RMap<String, SeckillStatus> map = redissonClient.getMap(RedisConstans.SECKILL_USER_STATUS_QUEUE);
        SeckillStatus seckillStatus = map.remove(userId);
        log.info("[秒杀订单服务]--SecKillOrderServiceImpl 取消排队 seckillStatus={}",seckillStatus);
        if (seckillStatus != null) {
            // 未支付订单
            if (seckillStatus.getStatus() == PayStatus.NO_PAID) {
                storageService.rollback(seckillStatus.getCommodityCode());
            }
            return true;
        }
        return false;
    }

    @Override
    public Order getOrder(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        return order;
    }

    @Override
    public int setOrder(Order order) {
        return orderMapper.updateById(order);
    }
}
