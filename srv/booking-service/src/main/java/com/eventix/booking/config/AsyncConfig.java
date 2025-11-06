package com.eventix.booking.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Value("${app.async.core-pool-size:5}")
    private int corePoolSize;

    @Value("${app.async.max-pool-size:20}")
    private int maxPoolSize;

    @Value("${app.async.queue-capacity:100}")
    private int queueCapacity;

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor t = new ThreadPoolTaskExecutor();
        t.setCorePoolSize(corePoolSize);
        t.setMaxPoolSize(maxPoolSize);
        t.setQueueCapacity(queueCapacity);
        t.setThreadNamePrefix("BookingExecutor-");
        t.initialize();
        return t;
    }
}

