package com.bnz.miaosha.seckill.controller;

import com.bnz.miaosha.entity.CodeMsg;
import com.bnz.miaosha.entity.Result;
import com.bnz.miaosha.entity.SeckillStatus;
import com.bnz.miaosha.seckill.aspect.AccessLimit;
import com.bnz.miaosha.seckill.config.TokenDecode;
import com.bnz.miaosha.seckill.service.SecKillOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seckillorder")
@Slf4j
public class SecKillOrderController {

    @Autowired
    private TokenDecode tokenDecode;

    @Autowired
    private SecKillOrderService secKillOrderService;

    @AccessLimit
    @RequestMapping("/add/{time}/{commodityCode}")
    public Result add(
            @PathVariable("time") String time,
            @PathVariable("commodityCode") String commodityCode
    ) throws Exception {


        //1.动态获取到当前的登录人
        String userId = tokenDecode.getUserInfo().get("id");

        //2.基于业务层进行秒杀下单
        Result result = secKillOrderService.add(commodityCode, time, userId);
        log.info("[秒杀订单控制类]-SecKillOrderController 秒杀商品返回：{}",result);
        //3.返回结果
        return result;

    }

    /**
     * 查询抢单状态
     * @return
     */
    @GetMapping("/queryStatus")
    public Result queryStatus() {
        String userId = tokenDecode.getUserInfo().get("id");
        SeckillStatus seckillStatus = secKillOrderService.getSeckillStatus(userId);
        if (null != seckillStatus){
            return Result.success(CodeMsg.QUERY_SUCCESS,seckillStatus);
        }
        return Result.error(CodeMsg.NOT_FOUND_ERROR);
    }

    /**
     * 删除抢单状态
     * @return
     */
    @GetMapping("/delStatus")
    public Result delStatus() {
        String userId = tokenDecode.getUserInfo().get("id");
        boolean flag =  secKillOrderService.delSeckillStatus(userId);
        if (flag){

            return Result.success(CodeMsg.OK);
        }
        return new Result(CodeMsg.ERROR);
    }

}
