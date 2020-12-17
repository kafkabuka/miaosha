package com.bnz.miaosha.entity;

/**
 * 返回结果实体类
 */
public class Result<T> {

    private boolean flag;//是否成功
    private Integer code;//返回码
    private String message;//返回消息

    private T data;//返回数据

    /**
     * 失败时候的调用
     */
    public static <T> Result<T> error(CodeMsg CodeMsg) {
        return new Result<T>(false,CodeMsg);
    }

    public static <T> Result<T> error(CodeMsg CodeMsg,T data) {
        return new Result<T>(false,CodeMsg,data);
    }

    public static <T> Result<T> success(CodeMsg CodeMsg) {
        return new Result<T>(true,CodeMsg.getCode(), CodeMsg.getMsg());
    }
    public static <T> Result<T> success(CodeMsg CodeMsg, T t) {
        return new Result<T>(true,CodeMsg.getCode(), CodeMsg.getMsg(), t);
    }
    public Result(T data) {
        this.data = data;
    }

    public Result(boolean flag, CodeMsg msg) {
        this.flag = flag;
        this.code = msg.getCode();
        this.message = msg.getMsg();
    }
    public Result(boolean flag, CodeMsg msg,T data) {
        this.flag = flag;
        this.code = msg.getCode();
        this.message = msg.getMsg();
        this.data = data;
    }
    public Result(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result(boolean flag,Integer code, String message, T t) {
        this.flag = flag;
        this.code = code;
        this.message = message;
        this.data = t;
    }
    public Result(boolean flag,Integer code, String message) {
       this(flag,code,message,null);
    }

    private Result(CodeMsg CodeMsg) {
        if (CodeMsg != null) {
            this.code = CodeMsg.getCode();
            this.message = CodeMsg.getMsg();
            this.data = null;
        }
    }

    public Integer getCode() {
        return code;
    }

    public Result<T> setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getmessage() {
        return message;
    }

    public Result<T> setmessage(String message) {
        this.message = message;
        return this;
    }

    public T getData() {
        return data;
    }

    public Result<T> setData(T data) {
        this.data = data;
        return this;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
