package com.luban.xbongbong.api.controller;

import com.luban.xbongbong.api.helper.config.ConfigConstant;
import com.luban.xbongbong.api.model.WebhookPayload;
import com.luban.xbongbong.api.processor.XbbWebhookEventProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

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
    /**
     * TODO 因为回调响应有时间限制，需要考虑业务处理速度问题，是否需要异步还需要设计一下
     */
    private final ExecutorService executor;

    @PostMapping("/event/listener")
    public void listener(@RequestBody WebhookPayload payload, @RequestHeader(name = ConfigConstant.REQUEST_HEADER_SIGN) String sign) {
        log.info("Xbb Webhook Request Payload : {}", payload.toString());
        log.info("Xbb Webhook Request Sign : {}", sign);
        Assert.isTrue(Objects.equals(ConfigConstant.getDataSign(payload.toString(), ConfigConstant.WEBHOOK_TOKEN), sign), () -> {
            throw new RuntimeException("验签失败，非法请求");
        });
        log.info("Xbb Webhook Request Sign Is Valid");
        XbbWebhookEventProcessor.PROCESSORS.stream().filter(processor -> processor.proceed().test(payload)).forEach(processor -> executor.execute(() -> processor.process().accept(payload)));
    }
}
