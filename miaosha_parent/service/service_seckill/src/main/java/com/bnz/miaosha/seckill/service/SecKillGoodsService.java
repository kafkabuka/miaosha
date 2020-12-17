package com.bnz.miaosha.seckill.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bnz.miaosha.seckill.pojo.SeckillGoods;

public interface SecKillGoodsService {

    // 获取秒杀商品列表
    IPage<SeckillGoods> list(Integer current, Integer limit, String time);

    // 获取秒杀商品详情
    SeckillGoods getSeckillGoods(String selectTime,String commodityCode);
}
