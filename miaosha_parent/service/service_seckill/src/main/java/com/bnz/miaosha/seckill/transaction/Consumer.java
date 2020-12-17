package com.bnz.miaosha.seckill.transaction;

import com.bnz.miaosha.seckill.utils.LogExceptionWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Slf4j
@Component
public class Consumer {

    @Value("rocketmq.name-server")
    private String namesrv;

    private DefaultMQPushConsumer defaultMQPushConsumer;

    @Resource(name = "seckillChargeOrderListenerImpl")
    private MessageListenerConcurrently messageListener;


    @PostConstruct
    public void init() {
        defaultMQPushConsumer =
                new DefaultMQPushConsumer("SECKILL_CHARGE_ORDER_CONSUMER_GROUP");
        defaultMQPushConsumer.setNamesrvAddr(namesrv);
        // 从头开始消费
        defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        // 消费模式:集群模式
        // 集群：同一条消息 只会被一个消费者节点消费到
        // 广播：同一条消息 每个消费者都会消费到
        defaultMQPushConsumer.setMessageModel(MessageModel.CLUSTERING);
        // 注册监听器
        defaultMQPushConsumer.registerMessageListener(messageListener);
        // 设置每次拉取的消息量，默认为1
        defaultMQPushConsumer.setConsumeMessageBatchMaxSize(1);
        // 订阅所有消息
        try {
            defaultMQPushConsumer.subscribe("SECKILL_CHARGE_ORDER_TOPIC", "*");
            // 启动消费者
            defaultMQPushConsumer.start();
        } catch (MQClientException e) {
            log.error("[秒杀下单消费者]--SecKillChargeOrderConsumer加载异常!e={}", LogExceptionWrapper.getStackTrace(e));
            throw new RuntimeException("[秒杀下单消费者]--SecKillChargeOrderConsumer加载异常!", e);
        }
        log.info("[秒杀下单消费者]--SecKillChargeOrderConsumer加载完成!");
    }
}
