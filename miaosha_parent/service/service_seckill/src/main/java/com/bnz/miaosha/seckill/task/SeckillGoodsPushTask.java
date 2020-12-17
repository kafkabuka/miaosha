package com.bnz.miaosha.seckill.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bnz.miaosha.seckill.dao.SeckillGoodsMapper;
import com.bnz.miaosha.seckill.pojo.SeckillGoods;
import com.bnz.miaosha.seckill.pojo.Storage;
import com.bnz.miaosha.seckill.service.StorageService;
import com.bnz.miaosha.seckill.utils.RedisConstans;
import com.bnz.miaosha.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class SeckillGoodsPushTask {

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Resource
    private RedissonClient redissonClient;

    @Autowired
    private StorageService storageService;


    @Scheduled(cron = "0/30 * * * * ?")
    public void loadSecKillGoodsToRedis() throws ParseException {
        /**
         * 1.查询所有符合条件的秒杀商品
         * 	1) 获取时间段集合并循环遍历出每一个时间段
         * 	2) 获取每一个时间段名称,用于后续redis中key的设置
         * 	3) 状态必须为审核通过 status=1
         * 	4) 商品库存个数>0
         * 	5) 秒杀商品开始时间>=当前时间段
         * 	6) 秒杀商品结束<当前时间段+2小时
         * 	7) 排除之前已经加载到Redis缓存中的商品数据
         * 	8) 执行查询获取对应的结果集
         * 2.将秒杀商品存入缓存
         */

        List<Date> dateMenus = DateUtil.getDateMenus(); // 5个
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (Date dateMenu : dateMenus) {
            // 时间转成 yyyyMMddHH
            String redisExtName = DateUtil.date2Str(dateMenu);

            QueryWrapper<SeckillGoods> queryWrapper = new QueryWrapper<>();

            queryWrapper.eq("status", "1");
            queryWrapper.gt("stock_count", 0);
            queryWrapper.ge("start_time", dateMenu);
            queryWrapper.le("end_time", DateUtil.addDateHour(dateMenu, 2));
            //queryWrapper.apply("UNIX_TIMESTAMP(start_time) >= UNIX_TIMESTAMP('" + simpleDateFormat.format(dateMenu) + "')");
            //queryWrapper.apply("UNIX_TIMESTAMP(start_time) >= UNIX_TIMESTAMP('" + simpleDateFormat.format(dateMenu) + "')");
            //queryWrapper.apply("UNIX_TIMESTAMP(end_time) >= UNIX_TIMESTAMP('" + simpleDateFormat.format(DateUtil.addDateHour(dateMenu, 2)) + "')");
            //queryWrapper.apply("UNIX_TIMESTAMP(end_time) >= UNIX_TIMESTAMP('" + simpleDateFormat.format(DateUtil.addDateHour(dateMenu, 2)) + "')");

           // Set keys = redisUtil.hsgetKeys(RedisConstans.SECKILL_GOODS_KEY + redisExtName);//key field value
            RMap<String, SeckillGoods> map1 = redissonClient.getMap(RedisConstans.SECKILL_GOODS_KEY + redisExtName);
            Set<String> keys = map1.keySet();
            if (keys != null && keys.size() > 0) {
                queryWrapper.notIn("commodity_code", keys);
            }

            List<SeckillGoods> seckillGoodsList = seckillGoodsMapper.selectList(queryWrapper);

            //添加到缓存中
            for (SeckillGoods seckillGoods : seckillGoodsList) {
                System.out.println("添加商品到redis缓存中去" + seckillGoods);
                // redisUtil.hset(SECKILL_GOODS_KEY + redisExtName, seckillGoods.getId().toString(), seckillGoods);
                RMap<String, SeckillGoods> map = redissonClient.getMap(RedisConstans.SECKILL_GOODS_KEY+redisExtName);
                map.put(seckillGoods.getCommodityCode(),seckillGoods);

                //RBucket<SeckillGoods> bucket = redissonClient.getBucket(RedisConstans.SECKILL_GOODS_KEY + redisExtName + seckillGoods.getCommodityCode());
                //bucket.set(seckillGoods);
                //加载秒杀商品的库存
                Storage storage = storageService.getStorage(seckillGoods.getCommodityCode());
                RBucket<Storage> storageRBucket = redissonClient.getBucket(RedisConstans.SECKILL_GOODS_STOCK_COUNT_KEY + seckillGoods.getCommodityCode());
                storageRBucket.set(storage);
                // redisUtil.set(RedisConstans.SECKILL_GOODS_STOCK_COUNT_KEY + seckillGoods.getId(), seckillGoods.getStockCount());
            }
        }

    }
}
