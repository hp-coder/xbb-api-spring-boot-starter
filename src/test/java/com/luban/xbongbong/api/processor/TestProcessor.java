package com.luban.xbongbong.api.processor;

import com.alibaba.fastjson.JSONObject;
import com.luban.xbongbong.api.helper.enums.XbbBizType;
import com.luban.xbongbong.api.helper.enums.XbbOperateType;
import com.luban.xbongbong.api.model.WebhookPayload;
import com.luban.xbongbong.api.sdk.customer.CustomerApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * 测试处理器
 *
 * @author HP 2022/12/28
 */
@Slf4j
@Component
public class TestProcessor implements XbbWebhookEventProcessor {
    @Override
    public Consumer<WebhookPayload> process() {
        return payload -> {
            try {
                final JSONObject object = CustomerApi.get(payload.getDataId());
                final JSONObject data = object.getJSONObject("data");
                final Integer num_3 = data.getInteger("num_3");
                final boolean undistributed = Objects.equals(1, num_3);
                if (undistributed) {
                    log.info("退回公海用户");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    @Override
    public Predicate<WebhookPayload> proceed() {
        return payload -> Objects.equals(XbbBizType.customer, payload.getType()) &&
                Objects.equals(XbbOperateType.EDIT, payload.getOperate()) &&
                Objects.equals(7174754L, payload.getFormId());
    }
}
