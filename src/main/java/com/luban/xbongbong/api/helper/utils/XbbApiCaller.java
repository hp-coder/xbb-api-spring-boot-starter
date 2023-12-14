package com.luban.xbongbong.api.helper.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.luban.xbongbong.api.XbbApiAutoConfiguration;
import com.luban.xbongbong.api.helper.XbbUrl;
import com.luban.xbongbong.api.helper.config.XbbConfiguration;
import com.luban.xbongbong.api.helper.enums.api.ApiType;
import com.luban.xbongbong.api.helper.exception.XbbException;
import com.luban.xbongbong.api.model.XbbResponse;
import com.luban.xbongbong.api.model.common.detail.XbbDetailModel;
import com.luban.xbongbong.api.model.ratelimiter.XbbApiRequest;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.luban.xbongbong.api.helper.config.XbbConfiguration.TOKEN;

/**
 * @author hp
 */
@Slf4j
@Component
public class XbbApiCaller implements SmartInitializingSingleton {
    private static final int MAX_TIMEOUT = 200;
    private static RestTemplate restTemplate;

    public static XbbDetailModel get(@NonNull Long dataId, @NonNull XbbUrl url) throws Exception {
        final JSONObject data = new JSONObject();
        data.put("dataId", dataId);
        final String response = call(url, data);
        Assert.isTrue(StrUtil.isNotEmpty(response), String.format("销帮帮API响应为空, URL:%s", url));
        final XbbResponse<XbbDetailModel> xbbResponse = JSON.parseObject(response, new TypeReference<>() {
        });
        if (Objects.equals(xbbResponse.getCode(), 1)) {
            return xbbResponse.getResult();
        } else {
            throw new XbbException(xbbResponse);
        }
    }

    public static String call(XbbUrl url, JSONObject data) throws XbbException {
        log.debug("Xbb API Request Payload: {}", data.toJSONString());
        try {
            return call(new XbbApiRequest(url, data, XbbConfiguration.MAX_RETRY));
        } catch (IllegalArgumentException | XbbException e) {
            return JSONObject.toJSONString(new XbbResponse<>(-1, e.getLocalizedMessage(), false, null));
        }
    }

    private static String call(XbbApiRequest request) throws XbbException, IllegalArgumentException {
        if (XbbConfiguration.ENABLE_REQUEST_CONTROL && !proceed(request.getUrl().getType()) && request.retryable()) {
            log.warn("销帮帮API{}请求重试{}次", request.getRequestUrl(), request.getRetry());
            try {
                Thread.sleep(300);
                call(request);
            } catch (InterruptedException e) {
                log.error("销帮帮API请求异常: interrupted,{}", e.getLocalizedMessage());
            }
        }
        try {
            final JSONObject data = request.getData();
            data.put("corpid", XbbConfiguration.CORP_ID);
            if (StrUtil.isNotEmpty(XbbConfiguration.USER_ID)) {
                data.put("userId", XbbConfiguration.USER_ID);
            }

            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
            headers.set("sign", XbbConfiguration.getDataSign(data, TOKEN));

            if (log.isDebugEnabled()) {
                final String requestData = data.toJSONString();
                log.debug("request:{}", requestData);
                HttpEntity<String> entity = new HttpEntity<>(requestData, headers);
                final String response = restTemplate.postForObject(request.getRequestUrl(), entity, String.class);
                log.debug("response:{}", response);
                return response;
            } else {
                HttpEntity<String> entity = new HttpEntity<>(data.toJSONString(), headers);
                return restTemplate.postForObject(request.getRequestUrl(), entity, String.class);
            }
        } catch (Exception e) {
            log.error("销帮帮API请求异常", e);
            throw new XbbException(-1, "销帮帮API请求异常");
        }
    }

    private static boolean proceed(ApiType apiType) {
        synchronized (XbbApiCaller.class) {
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
                    log.error("write:尝试获取请求数量失败, per-minute限流未获取到ticket, 递归重试");
                    return false;
                }
                return true;
            }
            if (write) {
                Assert.isTrue(requestPerDayLimiter.tryAcquire(MAX_TIMEOUT, TimeUnit.MILLISECONDS), "write:尝试获取每日请求数量失败, daily限流未获取到ticket, 当日应终止调用");
                if (!requestPerMinuteLimiter.tryAcquire(MAX_TIMEOUT, TimeUnit.MILLISECONDS)) {
                    log.error("write:尝试获取请求数量失败, per-minute限流未获取到ticket, 递归重试");
                    return false;
                }
                if (!writePerSecondLimiter.tryAcquire(MAX_TIMEOUT, TimeUnit.MILLISECONDS)) {
                    log.error("write:尝试获取请求数量失败, per-second限流未获取到ticket, 递归重试");
                    return false;
                }
                return true;
            }
        }
        throw new IllegalArgumentException("none:销帮帮API无对应接口类型");
    }

    @Override
    public void afterSingletonsInstantiated() {
        restTemplate = SpringUtil.getBean(RestTemplate.class);
    }
}
