package com.luban.xbongbong.api.ratelimiter;

/**
 * @author hp
 */
public interface XbbRateLimiter {

    /**
     * Not knowing how many requests have been called is unsafe.
     * <p>
     * XBB has API calling limitations: three writes per second,
     * 1_000 calls per minute, and 100_000 calls per day.
     * <p>
     * If clients surpass any rate limitations, requests will fail
     * and be discarded. To prevent such incidents, a client-embedded
     * rate limiter and a retry mechanism are needed.
     *
     * @param request xbb API request
     * @return whether the request is safe to call.
     */
    boolean tryAndGetTicket(XbbApiRequest request);
}
