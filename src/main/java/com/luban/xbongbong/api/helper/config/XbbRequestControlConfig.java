package com.luban.xbongbong.api.helper.config;

import cn.hutool.extra.spring.SpringUtil;
import com.luban.xbongbong.api.XbbApiAutoConfiguration;
import com.luban.xbongbong.api.helper.enums.api.ApiType;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RedissonClient;
import org.springframework.util.Assert;

import java.util.concurrent.TimeUnit;

/**
 * @author HP 2023/1/11
 */
@Slf4j
public class XbbRequestControlConfig {

    private static final int MAX_TIMEOUT = 200;

    public static boolean proceed(ApiType apiType) {
        synchronized (XbbRequestControlConfig.class) {
            final ApiType localType = apiType == null ? ApiType.READ : apiType;
            final RedissonClient redissonClient = SpringUtil.getBean(RedissonClient.class);
            final RRateLimiter requestPerDayLimiter = redissonClient.getRateLimiter(XbbApiAutoConfiguration.REQUEST_PER_DAY_LIMITER);
            final RRateLimiter requestPerMinuteLimiter = redissonClient.getRateLimiter(XbbApiAutoConfiguration.REQUEST_PER_MINUTE_LIMITER);
            final RRateLimiter writePerSecondLimiter = redissonClient.getRateLimiter(XbbApiAutoConfiguration.WRITE_PER_SECOND_LIMITER);
            if (requestPerDayLimiter == null || requestPerMinuteLimiter == null || writePerSecondLimiter == null) {
                return true;
            }
            final boolean read = localType.equals(ApiType.READ);
            final boolean write = localType.equals(ApiType.WRITE);
            if (read) {
                Assert.isTrue(requestPerDayLimiter.tryAcquire(MAX_TIMEOUT, TimeUnit.MILLISECONDS), "read:尝试获取每日请求数量失败, daily限流未获取到ticket, 当日应终止调用");
                if (!requestPerMinuteLimiter.tryAcquire(MAX_TIMEOUT, TimeUnit.MILLISECONDS)) {
                    log.error("write:尝试获取请求数量失败, per-minute限流未获取到ticket, 返回false递归重试");
                    return false;
                }
                return true;
            }
            if (write) {
                Assert.isTrue(requestPerDayLimiter.tryAcquire(MAX_TIMEOUT, TimeUnit.MILLISECONDS), "write:尝试获取每日请求数量失败, daily限流未获取到ticket, 当日应终止调用");
                if (!requestPerMinuteLimiter.tryAcquire(MAX_TIMEOUT, TimeUnit.MILLISECONDS)) {
                    log.error("write:尝试获取请求数量失败, per-minute限流未获取到ticket, 返回false递归重试");
                    return false;
                }
                if (!writePerSecondLimiter.tryAcquire(MAX_TIMEOUT, TimeUnit.MILLISECONDS)) {
                    log.error("write:尝试获取请求数量失败, per-second限流未获取到ticket, 返回false递归重试");
                    return false;
                }
                return true;
            }
        }
        throw new IllegalArgumentException("none:无对应接口类型");
    }
}
