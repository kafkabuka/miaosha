package com.bnz.miaosha.seckill.transaction;

import com.alibaba.fastjson.JSON;
import com.bnz.miaosha.entity.SeckillStatus;
import com.bnz.miaosha.seckill.pojo.Order;
import com.bnz.miaosha.seckill.service.SecKillOrderService;
import com.bnz.miaosha.seckill.service.StorageService;
import com.bnz.miaosha.seckill.utils.PayStatus;
import com.bnz.miaosha.seckill.utils.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "consumer_group_tx2", topic = "topic_tx")
public class ConsumerTransactionListener implements RocketMQListener<String> {

    @Resource
    private WebSocketServer webSocketServer;
    @Resource
    private StorageService storageService;
    @Resource
    private RocketMQTemplate rocketMQTemplate;
    @Resource
    private SecKillOrderService secKillOrderService;
    @Override
    public void onMessage(String msg) {
        log.info("[秒杀下单消费者]--ConsumerTransactionListener 开始消费消息：{}", msg);

        // 解析消息
        Order order = JSON.parseObject(msg, Order.class);

        if (order != null) {
            // 扣减库存
            try {
                storageService.deduct(order.getCommodityCode(), order.getCount(), order.getTxNum());

                log.info("[秒杀下单消费者]--ConsumerTransactionListener webSocketServer发送消息! 用户ID：{}，msg：{}",order.getUserId(),"下单成功，请尽快支付");
                webSocketServer.sendInfo(order.getUserId(), "下单成功，请尽快支付");

                //在redis中秒杀状态 添加倒计时间
                SeckillStatus seckillStatus = secKillOrderService.getSeckillStatus(order.getUserId());
                seckillStatus.setStatus(PayStatus.NO_PAID);
                seckillStatus.setCountDown(new Date());
                secKillOrderService.putSeckillStatus(order.getUserId(),seckillStatus);

                Message message = new Message("SECKILL_CHARGE_ORDER_TOPIC", JSON.toJSONString(order).getBytes());

                message.setDelayTimeLevel(16);
                log.info("[秒杀下单消费者]--ConsumerTransactionListener 发送延迟消息，进行取消订单!");
                rocketMQTemplate.getProducer().send(message);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("[秒杀下单消费者]--ConsumerTransactionListener MySQL扣减库存失败,order={}",order.toString());
                throw new RuntimeException("MySQL扣减库存失败");
            }
        }
    }
}
