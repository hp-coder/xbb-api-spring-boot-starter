package com.luban.xbongbong.api.ratelimiter;

import com.google.common.base.Preconditions;
import com.luban.xbongbong.api.XbbProperties;
import com.luban.xbongbong.api.helper.enums.api.ApiType;
import com.luban.xbongbong.api.helper.utils.XbbApiCaller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RedissonClient;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author hp
 */
@Slf4j
@RequiredArgsConstructor
public class RedissonBasedXbbRateLimiter implements XbbRateLimiter {

    private final RedissonClient redissonClient;

    @Override
    public boolean tryAndGetTicket(XbbApiRequest request) {
        Preconditions.checkArgument(Objects.nonNull(request));
        Preconditions.checkArgument(Objects.nonNull(request.getUrl()));
        final ApiType apiType = request.getUrl().getType();
        synchronized (XbbApiCaller.class) {
            final ApiType localType = apiType == null ? ApiType.READ : apiType;
            final RRateLimiter requestPerDayLimiter = redissonClient.getRateLimiter(XbbProperties.REQUEST_PER_DAY_LIMITER);
            final RRateLimiter requestPerMinuteLimiter = redissonClient.getRateLimiter(XbbProperties.REQUEST_PER_MINUTE_LIMITER);
            final RRateLimiter writePerSecondLimiter = redissonClient.getRateLimiter(XbbProperties.WRITE_PER_SECOND_LIMITER);
            if (requestPerDayLimiter == null || requestPerMinuteLimiter == null || writePerSecondLimiter == null) {
                return true;
            }
            if (localType.equals(ApiType.READ)) {
                Assert.isTrue(requestPerDayLimiter.tryAcquire(XbbProperties.MAX_TIMEOUT, TimeUnit.MILLISECONDS), "read:尝试获取每日请求数量失败, daily限流未获取到ticket, 当日应终止调用");
                if (!requestPerMinuteLimiter.tryAcquire(XbbProperties.MAX_TIMEOUT, TimeUnit.MILLISECONDS)) {
                    log.error("write:尝试获取请求数量失败, per-minute限流未获取到ticket, 递归重试");
                    return false;
                }
                return true;
            }
            if (localType.equals(ApiType.WRITE)) {
                Assert.isTrue(requestPerDayLimiter.tryAcquire(XbbProperties.MAX_TIMEOUT, TimeUnit.MILLISECONDS), "write:尝试获取每日请求数量失败, daily限流未获取到ticket, 当日应终止调用");
                if (!requestPerMinuteLimiter.tryAcquire(XbbProperties.MAX_TIMEOUT, TimeUnit.MILLISECONDS)) {
                    log.error("write:尝试获取请求数量失败, per-minute限流未获取到ticket, 递归重试");
                    return false;
                }
                if (!writePerSecondLimiter.tryAcquire(XbbProperties.MAX_TIMEOUT, TimeUnit.MILLISECONDS)) {
                    log.error("write:尝试获取请求数量失败, per-second限流未获取到ticket, 递归重试");
                    return false;
                }
                return true;
            }
        }
        throw new IllegalArgumentException("none:销帮帮API无对应接口类型");
    }
}
