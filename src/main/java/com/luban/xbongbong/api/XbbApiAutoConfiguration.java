package com.luban.xbongbong.api;

import cn.hutool.extra.spring.EnableSpringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author HP 2022/12/27
 */
@Slf4j
@EnableSpringUtil
@EnableScheduling
@Configuration
@EnableConfigurationProperties
@RequiredArgsConstructor
@Import({XbbProperties.class})
public class XbbApiAutoConfiguration implements SmartInitializingSingleton {

    private final RedissonClient redissonClient;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Override
    public void afterSingletonsInstantiated() {
        setIfAbsent();
    }

    private void setIfAbsent() {
        final RRateLimiter second = redissonClient.getRateLimiter(XbbProperties.WRITE_PER_SECOND_LIMITER);
        if (!second.isExists()) {
            second.trySetRate(RateType.OVERALL, XbbProperties.WRITE_PER_SECOND, 1, RateIntervalUnit.SECONDS);
        }

        final RRateLimiter minute = redissonClient.getRateLimiter(XbbProperties.REQUEST_PER_MINUTE_LIMITER);
        if (!minute.isExists()) {
            minute.trySetRate(RateType.OVERALL, XbbProperties.REQUEST_PER_MINUTE, 60, RateIntervalUnit.SECONDS);
        }

        final RRateLimiter day = redissonClient.getRateLimiter(XbbProperties.REQUEST_PER_DAY_LIMITER);
        if (!day.isExists()) {
            day.trySetRate(RateType.OVERALL, XbbProperties.REQUEST_PER_DAY, 1, RateIntervalUnit.DAYS);
        }
    }

    @Async
    @Scheduled(cron = "15 0 0 * * ?")
    public void resetLimiter() {
        log.info("xbb request limiter resets now...");
        final RRateLimiter day = redissonClient.getRateLimiter(XbbProperties.REQUEST_PER_DAY_LIMITER);
        if (!day.isExists()) {
            setIfAbsent();
        }
        redissonClient.getKeys().delete(XbbProperties.getRateLimiterInstanceKey(XbbProperties.REQUEST_PER_DAY_LIMITER));
        int maxRetry = 10;
        for (boolean acquired = false; !acquired && maxRetry > 0; maxRetry--) {
            acquired = day.tryAcquire(1, XbbProperties.MAX_TIMEOUT, TimeUnit.MILLISECONDS);
        }
        log.info("xbb request limiter's instance has been reset");
    }
}
