package com.bnz.miaosha.seckill.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@ToString
@Accessors(chain = true)
@TableName("storage_tbl")
public class Storage implements Serializable {
    private Long id;
    private String commodityCode;
    private Integer count;
}
