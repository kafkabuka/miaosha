package com.bnz.miaosha.seckill.utils;

public class PayStatus {

    // 1：排队中 2：未支付 3：支付超时 4：秒杀失败 5：支付完成
    public static final Integer IN_THE_LINE = 1;    //排队中
    public static final Integer NO_PAID = 2;       //未支付
    public static final Integer OVER_TIME = 3;       //支付失败
    public static final Integer FAIL = 4;       //支付失败
    public static final Integer FINISHED = 5;       //支付失败
}
