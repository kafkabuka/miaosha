package com.bnz.miaosha.seckill.transaction.listener;

import com.alibaba.fastjson.JSON;
import com.bnz.miaosha.seckill.pojo.Order;
import com.bnz.miaosha.seckill.service.SecKillOrderService;
import com.bnz.miaosha.seckill.service.StorageService;
import com.bnz.miaosha.seckill.utils.PayStatus;
import com.bnz.miaosha.seckill.utils.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Component
public class SeckillChargeOrderListenerImpl implements MessageListenerConcurrently {

    @Resource
    private SecKillOrderService secKillOrderService;
    @Resource
    private StorageService storageService;
    @Resource
    private WebSocketServer webSocketServer;

    /**
     * 秒杀核心消费逻辑
     * @param msgs
     * @param consumeConcurrentlyContext
     * @return
     */
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
            try {
                for (MessageExt msg : msgs) {
                    // 消息解码
                    int reconsumeTimes = msg.getReconsumeTimes();
                    String msgId = msg.getMsgId();
                    String logSuffix = ",msgId=" + msgId + ",reconsumeTimes=" + reconsumeTimes;
                    log.info("[秒杀订单取消-消费者]-SecKillChargeOrderConsumer-接收到消息,msg={},{}", msg, logSuffix);

                    // 查询数据库
                    Order order = JSON.parseObject(new String(msg.getBody(),"UTF8"), Order.class);
                    Order mysqlOrder = secKillOrderService.getOrder(order.getId());
                    if (mysqlOrder == null){
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }
                    log.info("[秒杀订单取消-消费者]-SecKillChargeOrderConsumer-接收到消息,orderId={},order={}",order.getId(), mysqlOrder);
                    if (mysqlOrder.getStatus()== PayStatus.NO_PAID){
                        //支付超时
                        mysqlOrder.setStatus(PayStatus.OVER_TIME);

                        secKillOrderService.setOrder(mysqlOrder);
                        // 回滚库存
                        storageService.rollback(mysqlOrder.getCommodityCode());
                        webSocketServer.sendInfo(order.getUserId(),"订单支付超时");
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
    }
}
