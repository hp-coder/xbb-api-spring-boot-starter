package com.luban.xbongbong.api.helper.config;

import cn.hutool.extra.spring.SpringUtil;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author HP 2023/1/11
 */

public class XbbRequestControlConfig {

    private static StringRedisTemplate redisTemplate;

    public static final List<String> NON_WRITE_SUFFIX = Arrays.asList("get", "list", "detail");

    /**
     * TODO 这里只在consumer里判断是否可以调用
     * @param url
     * @return
     */
    public static boolean proceed(String url) {
        redisTemplate = SpringUtil.getBean(StringRedisTemplate.class);
        final Long day = redisTemplate.opsForValue().increment(ConfigConstant.REDIS_REQUEST_PER_DAY);
        setCache(day, ConfigConstant.REDIS_REQUEST_PER_DAY, getRestOfTheDayToMill(), TimeUnit.MILLISECONDS);
        if (day >= ConfigConstant.REQUEST_PER_DAY) {
            return false;
        }
        //TODO 要调整成生产消费者模式
//        final Long minute = redisTemplate.opsForValue().increment(ConfigConstant.REDIS_REQUEST_PER_MINUTE);
//        setCache(minute, ConfigConstant.REDIS_REQUEST_PER_MINUTE, 1L, TimeUnit.MINUTES);
//        if (minute >= ConfigConstant.REQUEST_PER_MINUTE) {
//            return false;
//        }
//        if (!NON_WRITE_SUFFIX.contains(url.substring(url.lastIndexOf("/")))) {
//            final Long second = redisTemplate.opsForValue().increment(ConfigConstant.REDIS_WRITE_PER_SECOND);
//            setCache(second, ConfigConstant.REDIS_WRITE_PER_SECOND, 1L, TimeUnit.SECONDS);
//            if (second >= ConfigConstant.WRITE_PER_SECOND) {
//                return false;
//            }
//        }
        return true;
    }

    protected static void setCache(Long count, String key, Long time, TimeUnit unit) {
        if (count == 1) {
            redisTemplate.expire(key, time, unit);
        }
    }

    protected static Long getRestOfTheDayToMill() {
        return  LocalDateTime.of(LocalDate.now(), LocalTime.MAX).atZone(ZoneId.of("Asia/Shanghai")).toInstant().toEpochMilli()
                -
                LocalDateTime.now().atZone(ZoneId.of("Asia/Shanghai")).toInstant().toEpochMilli();
    }

}
