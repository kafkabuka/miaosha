package com.bnz.miaosha.seckill.feign;

import com.bnz.miaosha.entity.Result;
import com.bnz.miaosha.seckill.pojo.SeckillGoods;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "seckill")
public interface SecKillGoodsFeign {

    @RequestMapping("/seckillgoods/list")
    public Result<List<SeckillGoods>> list(@RequestParam("time") String time);
}
