package com.luban.xbongbong.api.helper.config;

import cn.hutool.extra.spring.SpringUtil;
import com.luban.xbongbong.api.XbbApiAutoConfiguration;
import com.luban.xbongbong.api.helper.enums.api.ApiType;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RedissonClient;

/**
 * @author HP 2023/1/11
 */
@Slf4j
public class XbbRequestControlConfig {

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
                try {
                    requestPerDayLimiter.acquire();
                } catch (Exception e) {
                    log.error("requestPerDayLimiter error", e);
                    throw new IllegalStateException("尝试获取每日请求数量失败，当日应终止调用");
                }
                try {
                    requestPerMinuteLimiter.acquire();
                } catch (Exception e) {
                    log.error("尝试获取请求数量失败, 这里的异常为分钟限流未获取到ticket, 返回false递归重试");
                    return false;
                }
                return true;
            }
            if (write) {
                try {
                    requestPerDayLimiter.acquire();
                } catch (Exception e) {
                    log.error("requestPerDayLimiter error", e);
                    throw new IllegalStateException("尝试获取每日请求数量失败，当日应终止调用");
                }

                try {
                    requestPerMinuteLimiter.acquire();
                    writePerSecondLimiter.acquire();
                } catch (Exception e) {
                    log.error("尝试获取请求数量失败, 这里的异常为分钟或秒级限流未获取到ticket, 返回false递归重试");
                    return false;
                }
                return true;
            }
        }
        throw new IllegalStateException("无对应接口类型");
    }
}
