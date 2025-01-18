package com.ttdat.authservice.infrastructure.config.executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutorService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ExecutorConfig {

    @Bean
    public ExecutorService executorService() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        return new DelegatingSecurityContextExecutorService(executorService);
    }
}
