package com.bnz.miaosha.seckill.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bnz.miaosha.entity.CodeMsg;
import com.bnz.miaosha.entity.Result;
import com.bnz.miaosha.seckill.pojo.SeckillGoods;
import com.bnz.miaosha.seckill.service.SecKillGoodsService;
import com.bnz.miaosha.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/seckillgoods")
public class SecKillGoodsController {


    @Autowired
    private SecKillGoodsService secKillGoodsService;

    @RequestMapping("/list/{current}/{limit}/{time}")
    public Result<IPage<SeckillGoods>> list(
            @PathVariable("current") Integer current,
            @PathVariable("limit") Integer limit,
            @PathVariable("time") String time) {

        IPage<SeckillGoods> page = secKillGoodsService.list(current, limit, DateUtil.formatStr(time));
        return Result.success(CodeMsg.OK,page);
    }

    @GetMapping("/{selectTime}/{commodityCode}")
    public Result<SeckillGoods> selectGoods(
            @PathVariable("selectTime") String selectTime,
            @PathVariable("commodityCode") String commodityCode
    ) {
        SeckillGoods seckillGoods = secKillGoodsService.getSeckillGoods(DateUtil.formatStr(selectTime),commodityCode);
        return Result.success(CodeMsg.OK, seckillGoods);
    }

    @GetMapping("/nowTime")
    public Result<Date> nowTime() {
        return Result.success(CodeMsg.OK,new Date());
    }

    //获取秒杀时间段集合信息
    @RequestMapping("/timeMenus")
    public List<String> dateMenus(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getPrincipal());
        System.out.println(authentication.getPrincipal());
        System.out.println(authentication.isAuthenticated());
        //获取当前时间段相关的信息集合
        List<Date> dateMenus = DateUtil.getDateMenus();
        List<String> result = new ArrayList<>();
        SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Date dateMenu : dateMenus) {
            String format = simpleDateFormat.format(dateMenu);
            result.add(format);
        }
        return  result;
    }

}
