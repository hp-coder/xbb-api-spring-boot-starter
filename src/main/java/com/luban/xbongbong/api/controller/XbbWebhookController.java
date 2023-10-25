package com.luban.xbongbong.api.controller;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.luban.xbongbong.api.helper.config.ConfigConstant;
import com.luban.xbongbong.api.helper.exception.XbbException;
import com.luban.xbongbong.api.model.WebhookPayload;
import com.luban.xbongbong.api.processor.XbbWebhookEventProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

/**
 * 回调接收端须3秒内返回状态码为2XX的HTTP响应
 * 当回调地址请求失败时会重发，最多重发两次，第一次重发间隔1秒，第二次重发间隔2秒
 * 15分钟内同一个回调地址连续失败50次，接口回调会被限制，当15分钟的缓存失效后可重新回调。*
 *
 * @author HP 2022/12/27
 */
@Slf4j
@RestController
@ConditionalOnProperty(prefix = "xbb", name = {"token", "gateway", "corp-id", "user-id", "webhook-token"})
@RequiredArgsConstructor
@RequestMapping("/xbb/webhook")
public class XbbWebhookController {

    private final ExecutorService executor;
    private final List<XbbWebhookEventProcessor> processors;

    @PostMapping("/event/listener")
    public void listener(
                         @RequestBody Map<String, Object> originalPayload,
                         @RequestHeader(name = ConfigConstant.REQUEST_HEADER_SIGN) String sign
    ) {
        final String originalJson = JSONObject.toJSONString(originalPayload);
        log.info("Xbb Webhook Request Original Payload : {}", originalJson);
        final WebhookPayload payload = JSONObject.parseObject(originalJson, WebhookPayload.class);
        log.info("Xbb Webhook Request Converted Payload : {}", payload.toString());
        log.debug("Xbb Webhook Request Sign : {}", sign);
        Assert.isTrue(
                Objects.equals(ConfigConstant.getDataSign(payload.toString(), ConfigConstant.WEBHOOK_TOKEN), sign),
                () -> {
                    throw new XbbException(-1, "验签失败，非法请求");
                }
        );
        log.debug("Xbb Webhook Request Sign Is Valid");
        if (CollUtil.isEmpty(processors)) {
            log.warn("No Xbb Webhook Event Processor Was Found, Though An Event Was Received.");
        }
        processors
                .stream()
                .filter(processor -> processor.proceed().test(payload))
                .forEach(processor -> executor.execute(() -> processor.process().accept(payload)));
    }
}
