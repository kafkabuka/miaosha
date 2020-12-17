package com.bnz.miaosha.seckill;

import com.bnz.miaosha.interceptor.FeignInterceptor;
import com.bnz.miaosha.seckill.config.MyConfig;
import com.bnz.miaosha.seckill.config.TokenDecode;
import com.bnz.miaosha.utils.IdWorker;
import com.github.wxpay.sdk.WXPay;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = {"com.bnz.miaosha.seckill.dao"})
@EnableScheduling
@EnableConfigurationProperties
public class SecKillApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecKillApplication.class,args);
    }

    //idwork
    @Bean
    public IdWorker idWorker(){
        return new IdWorker(1,1);
    }

//    //设置redistemplate的序列化
//    @Bean
//    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        // 1.创建 redisTemplate 模版
//        RedisTemplate<Object, Object> template = new RedisTemplate<>();
//        // 2.关联 redisConnectionFactory
//        template.setConnectionFactory(redisConnectionFactory);
//        // 3.创建 序列化类
//        GenericToStringSerializer genericToStringSerializer = new GenericToStringSerializer(Object.class);
//        // 6.序列化类，对象映射设置
//        // 7.设置 value 的转化格式和 key 的转化格式
//        template.setValueSerializer(genericToStringSerializer);
//        template.setKeySerializer(new StringRedisSerializer());
//        template.afterPropertiesSet();
//        return template;
//    }

    @Bean
    public TokenDecode tokenDecode(){
        return new TokenDecode();
    }

    @Bean
    public FeignInterceptor feignInterceptor(){
        return  new FeignInterceptor();
    }
    @Bean
    public WXPay wxPay(){
        try {
            return new WXPay(new MyConfig());
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }
}
