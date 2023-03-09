package com.luban.xbongbong.api;

import cn.hutool.extra.spring.EnableSpringUtil;
import com.luban.xbongbong.api.biz.config.XbbBizConfig;
import com.luban.xbongbong.api.helper.config.ConfigConstant;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

/**
 * @author HP 2022/12/27
 */
@EnableSpringUtil
@EnableScheduling
@Configuration
@EnableConfigurationProperties
@Import({XbbBizConfig.class, ConfigConstant.class})
public class XbbApiAutoConfiguration implements SmartInitializingSingleton {

    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private ConfigConstant configConstant;

    public static final String WRITE_PER_SECOND_LIMITER = "xbb-api-write-per-second-limiter";
    public static final String REQUEST_PER_MINUTE_LIMITER = "xbb-api-request-per-minute-limiter";
    public static final String REQUEST_PER_DAY_LIMITER = "xbb-api-request-per-day-limiter";

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Override
    public void afterSingletonsInstantiated() {
        final RRateLimiter second = redissonClient.getRateLimiter(WRITE_PER_SECOND_LIMITER);
        second.trySetRate(RateType.OVERALL, configConstant.getWritePerSecond(), 1, RateIntervalUnit.SECONDS);

        final RRateLimiter minute = redissonClient.getRateLimiter(REQUEST_PER_MINUTE_LIMITER);
        minute.trySetRate(RateType.OVERALL, configConstant.getRequestPerMinute(), 60, RateIntervalUnit.SECONDS);

        final RRateLimiter day = redissonClient.getRateLimiter(REQUEST_PER_DAY_LIMITER);
        day.trySetRate(RateType.OVERALL, configConstant.getRequestPerDay(), 1, RateIntervalUnit.DAYS);
    }

    @Scheduled(cron = "20 0 0 * * ?")
    public void resetLimiter() {
        final RRateLimiter day = redissonClient.getRateLimiter(REQUEST_PER_DAY_LIMITER);
        day.trySetRate(RateType.OVERALL, configConstant.getRequestPerDay(), 1, RateIntervalUnit.DAYS);
    }
}
