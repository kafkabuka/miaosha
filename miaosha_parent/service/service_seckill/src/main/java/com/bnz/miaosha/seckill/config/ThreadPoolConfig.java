package com.bnz.miaosha.seckill.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;

@Configuration
@EnableAsync  // 启用异步任务
public class ThreadPoolConfig  implements AsyncConfigurer {

    // 声明一个线程池(并指定线程池的名字)
    @Bean("taskExecutor")
    public Executor getAsyncExecutor(){

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心线程数
        executor.setCorePoolSize(2);
        //最大线程数
        executor.setMaxPoolSize(Runtime.getRuntime().availableProcessors());
        //线程池维护线程所允许的空闲时间
        executor.setKeepAliveSeconds(30);
        //队列最大长度
        executor.setQueueCapacity(300);
        executor.setThreadFactory( Executors.defaultThreadFactory());
        //程池对拒绝任务(无线程可用)的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
        executor.setThreadNamePrefix("UserQueueAsync-");
        executor.initialize();
        return executor;
    }
}
