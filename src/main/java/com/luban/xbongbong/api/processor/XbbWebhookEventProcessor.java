package com.luban.xbongbong.api.processor;

import com.luban.xbongbong.api.model.XbbWebhookPayload;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * When {@code @ConditionalOnProperty(prefix = "xbb", name = {"token", "corp-id", "user-id", "webhook-token"})}
 * are met,
 * the endpoint to receive events from XBB is activated.
 * <p>
 * To process these events, clients should implement
 * this interface and make sure they are registered
 * as beans in the Spring container.
 *
 * @author HP
 */
public interface XbbWebhookEventProcessor {

    /**
     * Define how to process an event when proceed() returns TRUE.
     *
     * @return process method.
     */
    Consumer<XbbWebhookPayload> process();

    /**
     * Whether the implementation of this interface supports
     * processing an event.
     *
     * @return boolean operation.
     */
    Predicate<XbbWebhookPayload> proceed();
}
