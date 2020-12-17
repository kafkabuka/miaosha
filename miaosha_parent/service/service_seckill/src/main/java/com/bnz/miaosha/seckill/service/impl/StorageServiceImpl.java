package com.bnz.miaosha.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bnz.miaosha.seckill.dao.StorageMapper;
import com.bnz.miaosha.seckill.dao.TxLogMapper;
import com.bnz.miaosha.seckill.pojo.Storage;
import com.bnz.miaosha.seckill.pojo.TxLog;
import com.bnz.miaosha.seckill.service.StorageService;
import com.bnz.miaosha.seckill.utils.RedisConstans;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class StorageServiceImpl implements StorageService {

    @Resource
    private StorageMapper storageMapper;

    @Resource
    private TxLogMapper txLogMapper;

    @Resource
    private RedissonClient redissonClient;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deduct(String commodityCode, int count, String txNum) throws ExecutionException, InterruptedException, Exception {
        log.info("deduct 减库存");
        RLock lock = redissonClient.getLock("placeOrder:" + commodityCode);
        Future<Boolean> res = lock.tryLockAsync(100, 10, TimeUnit.SECONDS);

        try {
            if (res.get()){
                log.info("扣减库存，商品编码：{}，数量：{}",commodityCode,count);
                TxLog txLog = txLogMapper.selectById(txNum);
                if ( null != txLog ){
                    return;
                }

                // 扣减库存

                QueryWrapper<Storage> wrapper = new QueryWrapper<>();
                wrapper.setEntity(new Storage().setCommodityCode(commodityCode));
                Storage storage = storageMapper.selectOne(wrapper);
                if (null == storage){
                    throw new RuntimeException("商品"+commodityCode+"不存在");
                }
                if (storage.getCount()<=0){
                    throw new RuntimeException("商品"+commodityCode+"库存不足");
                }

                // 扣减MySQL中的库存
                log.info("扣减MySQL中的库存");
                storage.setCount(storage.getCount() - count);
                log.info("剩余库存数量："+storage.getCount());
                storageMapper.updateById(storage);

                //添加事务记录，用户幂等
                TxLog tLog = new TxLog();
                tLog.setTxNum(txNum);
                tLog.setCreateTime(new Date());
                txLogMapper.insert(tLog);
            }
        }finally {
            lock.unlock();
        }
    }
    public void rollback(String commodityCode){
        RLock lock = redissonClient.getLock("placeOrder:" + commodityCode);
        Future<Boolean> res = lock.tryLockAsync(100, 10, TimeUnit.SECONDS);

        try {
            if (res.get()){
                log.info("[库存服务]--StorageServiceImpl 回滚库存，商品编码：{}，数量：{}",commodityCode,1);
                // 回滚redis库存
                RBucket<Storage> storageRBucket = redissonClient.getBucket(RedisConstans.SECKILL_GOODS_STOCK_COUNT_KEY + commodityCode);
                Storage redisStorage = storageRBucket.get();
                Integer count = redisStorage.getCount();
                redisStorage.setCount(count+1);
                storageRBucket.set(redisStorage);

                // 查询 MySQL 库存
                QueryWrapper<Storage> wrapper = new QueryWrapper<>();
                wrapper.setEntity(new Storage().setCommodityCode(commodityCode));
                Storage storage = storageMapper.selectOne(wrapper);
                if (null == storage){
                    log.info("[库存服务]--StorageServiceImpl 回滚库存，商品编码：{} 不存在",commodityCode);
                    throw new RuntimeException("商品"+commodityCode+"不存在");
                }

                // 扣减 MySQL 中的库存
                log.info("[库存服务]--StorageServiceImpl 回滚库存 回滚MySQL中的库存");
                storage.setCount(storage.getCount() +1);
                log.info("[库存服务]--StorageServiceImpl 回滚库存 剩余库存数量："+storage.getCount());
                storageMapper.updateById(storage);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Storage getStorage(String commodityCode) {
        QueryWrapper<Storage> wrapper = new QueryWrapper<>();
        wrapper.eq("commodity_code",commodityCode);
        Storage storage = storageMapper.selectOne(wrapper);
        return storage;
    }

    @Override
    public boolean preReduceGoodsStock(String commodityCode) {
        RLock lock = redissonClient.getLock("placeOrder:" + commodityCode);
        Future<Boolean> res = lock.tryLockAsync(100, 10, TimeUnit.SECONDS);
        Preconditions.checkNotNull(commodityCode, "请确保commodityCode非空!");
        return false;
    }
}
