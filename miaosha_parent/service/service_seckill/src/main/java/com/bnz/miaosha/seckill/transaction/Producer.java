package com.bnz.miaosha.seckill.transaction;


import com.bnz.miaosha.seckill.utils.LogExceptionWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class Producer {

    private DefaultMQProducer defaultMQProducer;
    @Value("rocketmq.name-server")
    private String namesrv;

    @PostConstruct
    public void init() {
        defaultMQProducer =
                new DefaultMQProducer
                        ("SECKILL_CHARGE_ORDER_PRODUCTOR_GROUP");
        defaultMQProducer.setNamesrvAddr(namesrv);
        // 发送失败重试次数
        defaultMQProducer.setRetryTimesWhenSendFailed(3);
        try {
            defaultMQProducer.start();
        } catch (MQClientException e) {
            log.error("[秒杀订单生产者]--ProducerDelay加载异常!e={}", LogExceptionWrapper.getStackTrace(e));
            throw new RuntimeException("[秒杀订单生产者]--ProducerDelay加载异常!", e);
        }
        log.info("[秒杀订单生产者]--ProducerDelay加载完成!");
    }

    public DefaultMQProducer getProducer() {
        return defaultMQProducer;
    }
}
