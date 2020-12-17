package com.bnz.miaosha.seckill.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@TableName("tx_log")
public class TxLog {
    @TableId
    private String txNum;
    private Date createTime;
}
