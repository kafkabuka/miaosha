package com.bnz.miaosha.seckill.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bnz.miaosha.seckill.pojo.SeckillOrder;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


public interface SeckillOrderMapper extends BaseMapper<SeckillOrder> {

    @Select("select * from tb_seckill_order where user_id=#{username} and seckill_id=#{id}")
    SeckillOrder getOrderInfoByUserNameAndGoodsId(@Param("username") String username, @Param("id") Long id);
}
