package com.hp.xbongbong.api;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.hp.xbongbong.api.helper.utils.XbbDigestUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * 销帮帮接口全局配置与常量
 * <p>
 * 销帮帮API配置页
 * <a href="https://pfweb.xbongbong.com/#/apiToken/index">API Token</a>
 *
 * @author hp
 */
@Slf4j
@Data
@ConfigurationProperties(prefix = "xbb")
@Component
public class XbbProperties implements SmartInitializingSingleton {
    /**
     * API网关, 钉钉版默认: <a href="https://proapi.xbongbong.com">https://proapi.xbongbong.com</a>
     */
    private String gateway = "https://proapi.xbongbong.com";

    /**
     * API前缀, 钉钉版默认:pro
     */
    private String apiPrefix = "pro";

    /**
     * API版本, 钉钉版默认:v2
     */
    private String apiVersion = "v2";

    /**
     * API后缀, 钉钉版默认:api
     */
    private String apiSuffix = "api";

    /**
     * 企业id(必须)
     */
    private String corpId;

    /**
     * 操作API的用户id(必须)[钉钉版为钉钉用户userId]
     */
    private String userId;

    /**
     * API token(必须)
     */
    private String token;

    /**
     * webhook的校验token(必须)
     */
    private String webhookToken;

    /**
     * 是否开启客户端限流
     */
    private boolean enableRequestControl = false;

    /**
     * API每日最大调用次数, 最大 10w/d
     */
    private Integer requestPerDay = MAX_TOTAL_REQUEST_PER_DAY;

    /**
     * API每分钟最大调用次数, 最大 1000/m
     */
    private Integer requestPerMinute = MAX_TOTAL_REQUEST_PER_MINUTE;

    /**
     * 写接口的每秒最大调用次数, 最大 3/s
     */
    private Integer writePerSecond = MAX_WRITE_REQUEST_PER_SECOND;

    /**
     * 因限流未获取到ticket后重试次数, 默认3次
     */
    private Integer maxRetry = 3;

    /*
     * ===============================================
     *              配置项静态属性映射
     * ===============================================
     */

    public static String API_GATEWAY;

    public static String CORP_ID;

    public static String TOKEN;

    public static String WEBHOOK_TOKEN;

    public static String USER_ID;

    public static boolean ENABLE_REQUEST_CONTROL;

    public static int REQUEST_PER_DAY;

    public static int REQUEST_PER_MINUTE;

    public static int WRITE_PER_SECOND;

    public static int MAX_RETRY;

    /*
     * ===============================================
     *                限流默认配置项
     * ===============================================
     */

    public static final String WRITE_PER_SECOND_LIMITER = "xbb-api-write-per-second-limiter";

    public static final String REQUEST_PER_MINUTE_LIMITER = "xbb-api-request-per-minute-limiter";

    public static final String REQUEST_PER_DAY_LIMITER = "xbb-api-request-per-day-limiter";

    private static final String LIMITER_KEY_PATTERN = "{%s}:value";

    public static final int MAX_TIMEOUT = 200;

    public static final Integer MAX_TOTAL_REQUEST_PER_DAY = 100_000;

    public static final Integer MAX_TOTAL_REQUEST_PER_MINUTE = 1_000;

    public static final Integer MAX_WRITE_REQUEST_PER_SECOND = 3;

    public static final String REQUEST_HEADER_SIGN = "sign";

    /**
     * 获取redisson定义的Rate limiter的key名称
     */
    public static String getRateLimiterInstanceKey(String rateLimiterName) {
        Preconditions.checkArgument(StrUtil.isNotEmpty(rateLimiterName));
        return String.format(LIMITER_KEY_PATTERN, rateLimiterName);
    }

    /**
     * 获取签名
     * <p>
     * 规则:
     * 将访问接口所需的参数集data + token字符串拼接后进行SHA256运算得到最后的签名,然后将签名参数sign(参数名为sign)放入http header中;
     * 将访问接口所需的参数集data(参数名为data)放入http body。
     * 算法为 sha-256 ( data+token ),使用utf-8编码
     *
     * @param data  请求参数(JSON格式)
     * @param token 令牌
     * @return 接口回参
     */
    public static String getDataSign(JSONObject data, String token) {
        return getDataSign(data.toJSONString(), token);
    }

    /**
     * 获取签名
     * <p>
     * 规则:
     * 将访问接口所需的参数集data + token字符串拼接后进行SHA256运算得到最后的签名,然后将签名参数sign(参数名为sign)放入http header中;
     * 将访问接口所需的参数集data(参数名为data)放入http body。
     * 算法为 sha-256 ( data+token ),使用utf-8编码
     *
     * @param data  请求参数(JSON格式)
     * @param token 令牌
     * @return 接口回参
     */
    public static String getDataSign(String data, String token) {
        return XbbDigestUtil.encrypt(data + token, "SHA-256");
    }

    @Override
    public void afterSingletonsInstantiated() {
        Preconditions.checkArgument(StrUtil.isNotEmpty(gateway));
        Preconditions.checkArgument(StrUtil.isNotEmpty(apiPrefix));
        Preconditions.checkArgument(StrUtil.isNotEmpty(apiVersion));
        Preconditions.checkArgument(StrUtil.isNotEmpty(apiSuffix));
        Preconditions.checkArgument(StrUtil.isNotEmpty(corpId));
        Preconditions.checkArgument(StrUtil.isNotEmpty(token));
        Preconditions.checkArgument(StrUtil.isNotEmpty(webhookToken));

        API_GATEWAY = gateway + StrUtil.SLASH + apiPrefix + StrUtil.SLASH + apiVersion + StrUtil.SLASH + apiSuffix;
        CORP_ID = corpId;
        USER_ID = userId;
        TOKEN = token;
        WEBHOOK_TOKEN = webhookToken;
        ENABLE_REQUEST_CONTROL = enableRequestControl;

        if (enableRequestControl) {
            Optional.ofNullable(requestPerDay).ifPresent(i -> {
                Assert.isTrue(i >= 0, "不能为负");
                Assert.isTrue(i <= MAX_TOTAL_REQUEST_PER_DAY, () -> {
                    throw new RuntimeException("每天所有请求最大不能超过" + MAX_TOTAL_REQUEST_PER_DAY + "次");
                });
                REQUEST_PER_DAY = requestPerDay;
            });
            Optional.ofNullable(requestPerMinute).ifPresent(i -> {
                Assert.isTrue(i >= 0, "不能为负");
                Assert.isTrue(i <= MAX_TOTAL_REQUEST_PER_MINUTE, () -> {
                    throw new RuntimeException("每分钟所有请求最大不能超过" + MAX_TOTAL_REQUEST_PER_MINUTE + "次");
                });
                REQUEST_PER_MINUTE = requestPerMinute;
            });
            Optional.ofNullable(writePerSecond).ifPresent(i -> {
                Assert.isTrue(i >= 0, "不能为负");
                Assert.isTrue(i <= MAX_WRITE_REQUEST_PER_SECOND, () -> {
                    throw new RuntimeException("每秒写请求最大不能超过" + MAX_WRITE_REQUEST_PER_SECOND + "次");
                });
                WRITE_PER_SECOND = writePerSecond;
            });
            Optional.ofNullable(maxRetry).ifPresent(i -> {
                Assert.isTrue(i >= 0, "不能为负");
                Assert.isTrue(i <= 10, () -> {
                    throw new RuntimeException("最大重试次数不能大于10" + MAX_WRITE_REQUEST_PER_SECOND + "次");
                });
                MAX_RETRY = maxRetry;
            });
        }
    }
}
