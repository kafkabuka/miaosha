package com.bnz.miaosha.seckill.utils;

public class RedisConstans {

    // 用户排队
    public static final String SECKILL_ORDER_QUEUE = "SeckillOrderQueue";
    // 用户抢单状态
    public static final String SECKILL_USER_STATUS_QUEUE = "UserStatusQueue";

    // 秒杀商品
    public static final String SECKILL_GOODS_KEY = "seckill_goods_";
    // 秒杀商品库存
    public static final String SECKILL_GOODS_STOCK_COUNT_KEY = "seckill_goods_stock_count_";
}
