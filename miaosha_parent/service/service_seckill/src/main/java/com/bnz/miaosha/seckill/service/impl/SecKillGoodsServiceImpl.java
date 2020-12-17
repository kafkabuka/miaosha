package com.bnz.miaosha.seckill.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bnz.miaosha.seckill.pojo.SeckillGoods;
import com.bnz.miaosha.seckill.pojo.Storage;
import com.bnz.miaosha.seckill.service.SecKillGoodsService;
import com.bnz.miaosha.seckill.utils.RedisConstans;
import com.bnz.miaosha.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.bnz.miaosha.seckill.utils.RedisConstans.SECKILL_GOODS_KEY;

@Service
@Slf4j
public class SecKillGoodsServiceImpl implements SecKillGoodsService {

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public IPage<SeckillGoods> list(Integer current, Integer limit, String time) {
        if (time == null) {
            time = DateUtil.currentTime;
        }
        IPage<SeckillGoods> page = new Page<>();

        RMap<String, SeckillGoods> map = redissonClient.getMap(SECKILL_GOODS_KEY + time);
        List<SeckillGoods> list = new ArrayList<>(map.values());
        log.info("[商品服务]--SecKillGoodsServiceImpl 秒杀商品数量:{}",list.size());
        if (list==null){
            return null;
        }
        int from = (current - 1) * limit;
        int to = from + limit - 1;
        int size = list.size();

        if (size > to) {
            page.setRecords(list.subList(from, to));
        }
        if (size < from ) {
            page.setRecords(list);
        }
        list = list.subList(from, size);
        page.setRecords(list);

        // 刷新库存
        for (SeckillGoods goods : list) {
            RBucket<Storage> storageRBucket = redissonClient.getBucket(RedisConstans.SECKILL_GOODS_STOCK_COUNT_KEY + goods.getCommodityCode());
            Storage storage = storageRBucket.get();
            goods.setStockCount(storage.getCount());
        }
        page.setTotal(list.size());

        return page;
    }

    @Override
    public SeckillGoods getSeckillGoods(String selectTime,String commodityCode) {
        RMap<String, SeckillGoods> map = redissonClient.getMap(SECKILL_GOODS_KEY + selectTime);
        SeckillGoods goods = map.get(commodityCode);
        if (goods != null){
            RBucket<Storage> storageRBucket = redissonClient.getBucket(RedisConstans.SECKILL_GOODS_STOCK_COUNT_KEY + goods.getCommodityCode());
            Storage storage = storageRBucket.get();
            goods.setStockCount(storage.getCount());
        }
        return goods;
    }
}
