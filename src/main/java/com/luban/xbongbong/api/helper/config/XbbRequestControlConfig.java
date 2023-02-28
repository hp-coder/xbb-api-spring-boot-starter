package com.luban.xbongbong.api.helper.config;

import cn.hutool.extra.spring.SpringUtil;
import com.luban.xbongbong.api.helper.enums.api.ApiType;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RedissonClient;
import org.springframework.util.Assert;

/**
 * @author HP 2023/1/11
 */

public class XbbRequestControlConfig {

    public static boolean proceed(ApiType apiType) {
        synchronized (XbbRequestControlConfig.class) {
            final ApiType localType = apiType == null ? ApiType.READ : apiType;
            final RedissonClient redissonClient = SpringUtil.getBean(RedissonClient.class);
            final RRateLimiter requestPerDayLimiter = redissonClient.getRateLimiter("xbb-api-request-per-day-limitor");
            final RRateLimiter requestPerMinuteLimiter = redissonClient.getRateLimiter("xbb-api-request-per-minute-limitor");
            final RRateLimiter writePerSecondLimiter = redissonClient.getRateLimiter("xbb-api-write-per-second-limitor");
            if (requestPerDayLimiter == null || requestPerMinuteLimiter == null || writePerSecondLimiter == null) {
                return true;
            }
            final boolean read = localType.equals(ApiType.READ);
            final boolean write = localType.equals(ApiType.WRITE);
            if (read) {
                Assert.isTrue(requestPerDayLimiter.tryAcquire(1), () -> {
                    throw new IllegalStateException("尝试获取每日请求数量失败，当日应终止调用");
                });
                return requestPerMinuteLimiter.tryAcquire(1);
            }
            if (write) {
                Assert.isTrue(requestPerDayLimiter.tryAcquire(1), () -> {
                    throw new IllegalStateException("尝试获取每日请求数量失败，当日应终止调用");
                });
                final boolean second = requestPerMinuteLimiter.tryAcquire(1);
                if (!second) {
                    return false;
                }
                return writePerSecondLimiter.tryAcquire(1);
            }
        }
        throw new IllegalStateException("无对应接口类型");
    }
}
