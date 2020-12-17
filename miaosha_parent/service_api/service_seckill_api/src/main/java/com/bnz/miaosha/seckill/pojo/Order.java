package com.bnz.miaosha.seckill.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TableName("order_tbl")
public class Order {
    @TableId(type = IdType.INPUT)
    private Long id;
    private String userId;
    private String commodityCode;
    private Integer count;
    private Double money;
    private Integer status;
    @TableField(exist = false)
    private String txNum;
}
