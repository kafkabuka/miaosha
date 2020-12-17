package com.bnz.miaosha.seckill.service;

import com.bnz.miaosha.seckill.pojo.Storage;

import java.util.concurrent.ExecutionException;

public interface StorageService {
    /**
     * 扣减库存
     *
     * @param commodityCode
     * @param count
     * @param txNum         事务id
     */
    void deduct(String commodityCode, int count, String txNum) throws ExecutionException, InterruptedException, Exception;

    /**
     * 获取商品信息
     *
     * @param commodityCode
     * @return
     */
    Storage getStorage(String commodityCode);

    /**
     * 前置预减库存
     * @param commodityCode
     * @return
     */
    boolean preReduceGoodsStock(String commodityCode);

    /**
     * 回滚库存
     * @param commodityCode
     */
    void rollback(String commodityCode);
}
