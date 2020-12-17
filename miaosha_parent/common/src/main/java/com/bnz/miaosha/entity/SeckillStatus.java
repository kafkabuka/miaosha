package com.bnz.miaosha.entity;

import java.io.Serializable;
import java.util.Date;


public class SeckillStatus implements Serializable {

    private String username;// 用户
    private Date createTime;// 创建时间
    private Integer status; // 秒杀状态 1：排队中 2：未支付 3：支付超时 4：秒杀失败 5：支付完成
    private String commodityCode;   // 商品编号 用户超时订单库存回滚
    private Double money;   // 应付金额
    private Long orderId;   // 订单ID
    private String time;    // 时间段

    public Date getCountDown() {
        return countDown;
    }

    public void setCountDown(Date countDown) {
        this.countDown = countDown;
    }

    private Date countDown; // 倒计时
    public SeckillStatus(String username, Date createTime, Integer status, String commodityCode, Double money, Long orderId, String time) {
        this.username = username;
        this.createTime = createTime;
        this.status = status;
        this.commodityCode = commodityCode;
        this.money = money;
        this.orderId = orderId;
        this.time = time;
    }

    public SeckillStatus(String username, Date createTime, Integer status, String commodityCode, String time) {
        this(username,createTime,status,commodityCode,null,null,time);
    }

    public SeckillStatus() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCommodityCode() {
        return commodityCode;
    }

    public void setCommodityCode(String commodityCode) {
        this.commodityCode = commodityCode;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
