package com.bnz.miaosha.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bnz.miaosha.user.pojo.PointLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PointLogMapper extends BaseMapper<PointLog> {

    @Select("select * from tb_point_log where order_id =#{orderId}")
    PointLog findPointLogByOrderId(@Param("orderId") String orderId);
}
