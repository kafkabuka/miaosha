package com.bnz.miaosha.seckill.config;

import org.apache.commons.lang.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class RedissonConfig {
    @Resource
    private RedissonProperties redissonProperties;

    /**
     * 单例模式
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "redisson",name = "single-is",havingValue = "true")
    public RedissonClient getSingleRedisson(){
        Config config = new Config();
        String singlePassword = redissonProperties.getSinglePassword();
        SingleServerConfig singleServerConfig = config.useSingleServer().setAddress("redis://" + redissonProperties.getSingleAddress());

        System.out.println("reids:======"+singleServerConfig.getAddress());
        if (StringUtils.isNotBlank(singlePassword)){
            singleServerConfig.setPassword(singlePassword);
        }
        return Redisson.create(config);
    }

    /**
     * 哨兵模式自动装配
     * @return
     */
//    @Bean
//    @ConditionalOnProperty(prefix ="redisson",name="sentinel-is",havingValue="true")
//    public RedissonClient getSentinelRedisson(){
//        Config config = new Config();
//        SentinelServersConfig serverConfig = config.useSentinelServers().addSentinelAddress(redissonProperties.getSentinelAddresses()).setMasterName(redissonProperties.getSentinelMasterName());
//        String sentinelPassword = redissonProperties.getSentinelPassword();
//        if(StringUtils.isNotBlank(sentinelPassword)) {
//            serverConfig.setPassword(sentinelPassword);
//        }
//        return Redisson.create(config);
//    }

}
