package com.cn.tfe.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class ThreadPoolConfig {

    @Value("${custom-config.thread.core-size}")
    private int coreSize;

    @Value("${custom-config.thread.max-pool-size}")
    private int maxPoolSize;

    @Value("${custom-config.thread.queue-capacity}")
    private int queueCapacity;

    @Value("${custom-config.thread.keep-alive-seconds}")
    private int keepAliveSeconds;

    @Bean
    @Qualifier("taskExecutor")
    public Executor taskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(coreSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("customExecutor--");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }

}
