package com.bnz.miaosha.entity;

/**
 * 返回码
 */
public class CodeMsg {
    private Integer code;
    private String msg;

    public static CodeMsg OK = new CodeMsg(20000, "成功");
    public static CodeMsg ERROR = new CodeMsg(20001, "失败");
    public static CodeMsg LOGIN_ERROR = new CodeMsg(20002, "用户名或密码错误");
    public static CodeMsg LOGIN_SUCCESS = new CodeMsg(20003, "登录成功");
    public static CodeMsg ACCESS_ERROR = new CodeMsg(20003, "权限不足");
    public static CodeMsg REMOTE_ERROR = new CodeMsg(20004, "远程调用失败");
    public static CodeMsg DELETE_SUCCESS = new CodeMsg(20007, "删除成功");
    public static CodeMsg REP_ERROR = new CodeMsg(20005, "重复操作");
    public static CodeMsg NOT_FOUND_ERROR = new CodeMsg(20006, "未找到");
    public static CodeMsg GOODS_NOT_EXIST = new CodeMsg(40007, "秒杀商品不存在");
    public static CodeMsg GOODS_STOCK_NOT_ENOUGH = new CodeMsg(40008, "秒杀商品库存不足");
    public static CodeMsg QUEUE_NOT_EXIST = new CodeMsg(40008, "用户排队信息不存在");
    public static CodeMsg ORDER_INLINE = new CodeMsg(10000, "订单排队中");
    public static CodeMsg ORDER_NOT_PAY = new CodeMsg(10001, "订单待支付");
    public static CodeMsg ORDER_PAY_OVERTIME = new CodeMsg(10002, "订单支付超时");
    public static CodeMsg ORDER_SECKILL_FAIL = new CodeMsg(10003, "秒杀失败");
    public static CodeMsg QUERY_SUCCESS = new CodeMsg(10004, "查询成功");

    public CodeMsg( ) {
    }

    public CodeMsg(Integer code, String msg ) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public CodeMsg setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public CodeMsg setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public CodeMsg fillArgs(Object ... args) {
        Integer code = this.code;
        String message = String.format(this.msg, args);
        return new CodeMsg(code, message);
    }

    @Override
    public String toString() {
        return "CodeMsg [code=" + code + ", msg=" + msg + "]";
    }
}
