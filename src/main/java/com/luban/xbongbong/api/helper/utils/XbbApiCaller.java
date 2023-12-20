package com.luban.xbongbong.api.helper.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.google.common.base.Preconditions;
import com.luban.xbongbong.api.XbbProperties;
import com.luban.xbongbong.api.helper.XbbUrl;
import com.luban.xbongbong.api.helper.exception.XbbException;
import com.luban.xbongbong.api.model.XbbResponse;
import com.luban.xbongbong.api.model.common.detail.XbbDetailModel;
import com.luban.xbongbong.api.ratelimiter.XbbApiRequest;
import com.luban.xbongbong.api.ratelimiter.XbbRateLimiter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Objects;

import static com.luban.xbongbong.api.XbbProperties.TOKEN;

/**
 * @author hp
 */
@Slf4j
@Component
public class XbbApiCaller implements SmartInitializingSingleton {
    private static RestTemplate restTemplate;
    private static XbbRateLimiter xbbRateLimiter;

    public static XbbDetailModel get(@NonNull Long dataId, @NonNull XbbUrl url) {
        final JSONObject data = new JSONObject();
        data.put("dataId", dataId);
        final XbbResponse<XbbDetailModel> call = call(url, data);
        return call.getResult(XbbDetailModel.class);
    }

    public static <E> XbbResponse<E> call(XbbUrl url, JSONObject data) throws XbbException {
        log.debug("Xbb API Request Payload: {}", data.toJSONString());
        try {
            final XbbApiRequest request = new XbbApiRequest(url, data, XbbProperties.MAX_RETRY);
            final XbbResponse<E> response = call(request);
            if (response.succeed()) {
                return response;
            }
            throw new XbbException(request, response);
        } catch (XbbException e) {
            throw e;
        } catch (Exception e) {
            throw new XbbException("销帮帮API请求异常", e);
        }
    }

    private static <E> XbbResponse<E> call(XbbApiRequest request) throws XbbException, IllegalArgumentException {
        if (XbbProperties.ENABLE_REQUEST_CONTROL && !xbbRateLimiter.tryAndGetTicket(request) && request.retryable()) {
            log.warn("销帮帮API{}请求重试{}次", request.getRequestUrl(), request.getRetry());
            try {
                Thread.sleep(300);
                return call(request);
            } catch (InterruptedException e) {
                log.error("销帮帮API请求异常: interrupted,{}", e.getLocalizedMessage());
            }
        }
        final JSONObject data = request.getData();
        data.put("corpid", XbbProperties.CORP_ID);
        if (StrUtil.isNotEmpty(XbbProperties.USER_ID)) {
            data.put("userId", XbbProperties.USER_ID);
        }

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
        headers.set("sign", XbbProperties.getDataSign(data, TOKEN));

        String response;
        if (log.isDebugEnabled()) {
            final String requestData = data.toJSONString();
            log.debug("request:{}", requestData);
            HttpEntity<String> entity = new HttpEntity<>(requestData, headers);
            response = restTemplate.postForObject(request.getRequestUrl(), entity, String.class);
            log.debug("response:{}", response);
        } else {
            HttpEntity<String> entity = new HttpEntity<>(data.toJSONString(), headers);
            response = restTemplate.postForObject(request.getRequestUrl(), entity, String.class);
        }
        final JSONObject parse = JSON.parseObject(response, Feature.SupportAutoType, Feature.IgnoreNotMatch, Feature.TrimStringFieldValue);
        Preconditions.checkArgument(Objects.nonNull(parse), "response json is null");
        if (!parse.containsKey("result")) {
            parse.put("result", null);
        }
        return parse.toJavaObject(new TypeReference<>() {
        });
    }

    @Override
    public void afterSingletonsInstantiated() {
        restTemplate = SpringUtil.getBean(RestTemplate.class);
        xbbRateLimiter = SpringUtil.getBean(XbbRateLimiter.class);
        Preconditions.checkArgument(Objects.nonNull(restTemplate));
        Preconditions.checkArgument(Objects.nonNull(xbbRateLimiter));
    }
}
