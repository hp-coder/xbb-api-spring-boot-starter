package com.luban.xbongbong.api.processor;

import com.luban.xbongbong.api.model.WebhookPayload;
import org.springframework.beans.factory.SmartInitializingSingleton;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author HP 2022/12/27
 */
public interface XbbWebhookEventProcessor extends SmartInitializingSingleton {

    List<XbbWebhookEventProcessor> PROCESSORS = new ArrayList<>();

    Consumer<WebhookPayload> process();

    Predicate<WebhookPayload> proceed();

    @Override
    default void afterSingletonsInstantiated() {
        PROCESSORS.add(this);
    }
}
