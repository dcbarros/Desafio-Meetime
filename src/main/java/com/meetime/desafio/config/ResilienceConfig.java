package com.meetime.desafio.config;

import java.time.Duration;

import io.github.resilience4j.ratelimiter.RateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.ratelimiter.RateLimiterConfig;

@Configuration
public class ResilienceConfig {

    @Bean
    public RateLimiterConfig customRateLimiterConfig() {
        return RateLimiterConfig.custom()
            .limitRefreshPeriod(Duration.ofSeconds(10))
            .limitForPeriod(100)
            .timeoutDuration(Duration.ofMillis(500))
            .build();
    }

    @Bean
    public RateLimiter hubspotRateLimiter(RateLimiterConfig customRateLimiterConfig) {
        return RateLimiter.of("hubspotLimiter", customRateLimiterConfig);
    }
}
