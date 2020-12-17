package com.bnz.miaosha.seckill.transaction;

import com.alibaba.fastjson.JSON;
import com.bnz.miaosha.entity.SeckillStatus;
import com.bnz.miaosha.seckill.dao.TxLogMapper;
import com.bnz.miaosha.seckill.pojo.Order;
import com.bnz.miaosha.seckill.pojo.Storage;
import com.bnz.miaosha.seckill.pojo.TxLog;
import com.bnz.miaosha.seckill.service.SecKillOrderService;
import com.bnz.miaosha.seckill.utils.RedisConstans;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RocketMQTransactionListener(txProducerGroup = "producer_group_tx1")
public class ProducerTransactionListener implements RocketMQLocalTransactionListener {

    @Resource
    private SecKillOrderService secKillOrderService;
    @Resource
    private TxLogMapper txLogMapper;
    @Resource
    private RedissonClient redissonClient;


    /**
     * 事务消息发送mq成功后的回调方法
     *
     * @param msg
     * @param o
     * @return
     */
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object o) {
        log.info("[秒杀下单生产者]--ProducerTransactionListener 事务消息发送mq成功后的回调方法");
        String str = new String((byte[]) msg.getPayload());
        Order order = JSON.parseObject(str, Order.class);
        try {

            // 下单
            log.info("下单");
            secKillOrderService.insertOrder(order);

            // mq 可消费该消息
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {
            e.printStackTrace();
            // 更新用户秒杀信息
            SeckillStatus seckillStatus = secKillOrderService.getSeckillStatus(order.getUserId());
            seckillStatus.setStatus(4);
            secKillOrderService.putSeckillStatus(order.getUserId(),seckillStatus);

            //redis库存回滚
            RLock lock = redissonClient.getLock("storageBucket:" + order.getCommodityCode());
            Future<Boolean> res = lock.tryLockAsync(100, 10, TimeUnit.SECONDS);
            try {
                if (res.get()){
                    RBucket<Storage> storageBucket = redissonClient.getBucket(RedisConstans.SECKILL_GOODS_STOCK_COUNT_KEY + order.getCommodityCode());
                    Storage storage = storageBucket.get();
                    int count = storage.getCount()+order.getCount();
                    log.info("[秒杀下单生产者]--ProducerTransactionListener 下单失败，库存回滚：{}，剩余：{}",1,count);
                    storage.setCount(count);
                    storageBucket.set(storage);
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } catch (ExecutionException ex) {
                ex.printStackTrace();
            } finally {
                lock.unlock();
            }
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    /**
     * 事务状态回查
     *
     * @param message
     * @return
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        log.info("[秒杀下单生产者]--ProducerTransactionListener 事务状态回查");
        String str = new String((byte[]) message.getPayload());
        Order order = JSON.parseObject(str, Order.class);
        // 事务ID
        String txNo = order.getTxNum();
        TxLog txLog = txLogMapper.selectById(txNo);
        if (txLog != null) {
            return RocketMQLocalTransactionState.COMMIT;
        }
        return RocketMQLocalTransactionState.UNKNOWN;
    }
}
