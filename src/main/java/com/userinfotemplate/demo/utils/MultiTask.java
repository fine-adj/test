package com.userinfotemplate.demo.utils;


import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import org.slf4j.Logger;

@Configuration
@EnableAsync         //开启异步，可以在线程池的配置类中，也可以在启动类上加该注解
public class MultiTask {

    private static final Logger logger = LoggerFactory.getLogger(MultiTask.class);

    @Bean("myTaskThreadPool")  //创建一个线程池，并指定线程池在容器中的名字
    public ThreadPoolTaskExecutor createExecutor(){
        //Spring提供的创建线程池的类，底层仍然是juc并发包下ThreadPoolExecutor实现的
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(3);
        threadPoolTaskExecutor.setMaxPoolSize(5);
        threadPoolTaskExecutor.setQueueCapacity(10);
        threadPoolTaskExecutor.setKeepAliveSeconds(60);
        threadPoolTaskExecutor.initialize();

        return threadPoolTaskExecutor;
    }
}
