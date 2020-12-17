package com.bnz.miaosha.seckill.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "redisson")
public class RedissonProperties {
    private String singleAddress;
    private String singlePassword;
    private String sentinelMasterName;
    private String sentinelAddresses;
    private String sentinelPassword;
}
