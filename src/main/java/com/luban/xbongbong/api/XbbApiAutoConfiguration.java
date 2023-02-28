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
import org.springframework.web.client.RestTemplate;

/**
 * @author HP 2022/12/27
 */
@EnableSpringUtil
@Configuration
@EnableConfigurationProperties
@Import({XbbBizConfig.class, ConfigConstant.class})
public class XbbApiAutoConfiguration implements SmartInitializingSingleton {

    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private ConfigConstant configConstant;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    @Override
    public void afterSingletonsInstantiated() {
        final RRateLimiter second = redissonClient.getRateLimiter("xbb-api-write-per-second-limitor");
        second.trySetRate(RateType.OVERALL, configConstant.getWritePerSecond(), 1, RateIntervalUnit.SECONDS);

        final RRateLimiter minute = redissonClient.getRateLimiter("xbb-api-request-per-minute-limitor");
        minute.trySetRate(RateType.OVERALL, configConstant.getRequestPerMinute(), 1, RateIntervalUnit.MINUTES);

        final RRateLimiter day = redissonClient.getRateLimiter("xbb-api-request-per-day-limitor");
        day.trySetRate(RateType.OVERALL, configConstant.getRequestPerDay(), 1, RateIntervalUnit.DAYS);
    }
}
