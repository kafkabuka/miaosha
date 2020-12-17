package com.bnz.miaosha.seckill.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bnz.miaosha.seckill.pojo.SeckillGoods;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;


public interface SeckillGoodsMapper extends BaseMapper<SeckillGoods> {

    @Select("select * from tb_seckill_goods where status=1 and stock_count > 0 and start_time >= to_date(#{startTime},'yyyy-MM-dd HH:mm:ss') and end_time <= to_date(#{endTime},'yyyy-MM-dd HH:mm:ss')")
    List<SeckillGoods> selectGoods(@Param("startTime")Date startTime,@Param("endTime") Date endTime);
}
