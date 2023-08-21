package com.luban.xbongbong.api.helper.config;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.luban.xbongbong.api.helper.enums.api.ApiType;
import com.luban.xbongbong.api.helper.exception.XbbException;
import com.luban.xbongbong.api.helper.utils.DigestUtil;
import com.luban.xbongbong.api.model.XbbResponse;
import com.luban.xbongbong.api.model.common.detail.XbbDetailModel;
import com.luban.xbongbong.api.model.ratelimiter.XbbApiRequestModel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

/**
 * 销帮帮接口全局配置与常量
 *
 * @author hp
 */
@Slf4j
@Getter
@Setter
@ConfigurationProperties(prefix = "xbb")
@Component
public class ConfigConstant implements SmartInitializingSingleton {

    private String gateway;
    private String corpId;
    private String userId;
    private String token;
    private String webhookToken;
    private boolean enableRequestControl;
    private Long requestPerDay;
    private Long requestPerMinute;
    private Long writePerSecond;
    private Integer maxRetry;

    private static RestTemplate restTemplate;

    /**
     * 销帮帮接口根域名
     */
    public static String XBB_API_ROOT;

    /**
     * 本公司corpid。接口基础参数，接口请求必传
     * 管理员账号登录销帮帮WEB版后台后，访问 <a href="https://pfweb.xbongbong.com/#/apiToken/index">...</a> 查看
     */
    public static String CORP_ID;

    /**
     * 本公司访问接口的token,该值相当于密钥，请妥善保管，不要泄露，可以下列url中重置token
     * 管理员账号登录销帮帮WEB版后台后，访问 <a href="https://pfweb.xbongbong.com/#/apiToken/index">...</a> 查看
     */
    public static String TOKEN;

    /**
     * webhook的校验token*
     * 管理员账号登录销帮帮WEB版后台后，访问 <a href="https://pfweb.xbongbong.com/#/apiToken/index">...</a> 查看*
     */
    public static String WEBHOOK_TOKEN;

    /**
     * 接口操作人userId,接口基础参数。不传默认取超管角色
     */
    public static String USER_ID;

    /**
     * 是否使用配置的请求限制*
     */
    public static boolean ENABLE_REQUEST_CONTROL = false;

    /**
     * 每天最多调用次数，必须小于10万*
     */
    public static Long REQUEST_PER_DAY = 0L;

    /**
     * 每分钟最多调用次数，每个平台单独配置必须小于1000*
     */
    public static Long REQUEST_PER_MINUTE = 0L;

    /**
     * 每秒最多写次数，必须小于3*
     */
    public static Long WRITE_PER_SECOND = 0L;

    public static Integer MAX_RETRY = 3;

    /**
     * 每天调用接口次数限制的缓存key*
     */
    public static final String REDIS_REQUEST_PER_DAY = "xbb_api_request_per_day:count";
    /**
     * 每分钟调用次数限制的缓存key*
     */
    public static final String REDIS_REQUEST_PER_MINUTE = "xbb_api_request_per_minute:count";

    /**
     * 没秒钟写接口次数限制的缓存key*
     */
    public static final String REDIS_WRITE_PER_SECOND = "xbb_api_write_per_second:count";

    /**
     * 一天最大调用十万次*
     */
    public static final Integer MAX_TOTAL_REQUEST_PER_DAY = 100000;

    /**
     * 一分钟最大调用一千次*
     */
    public static final Integer MAX_TOTAL_REQUEST_PER_MINUTE = 1000;

    /**
     * 写接口每秒3次*
     */
    public static final Integer MAX_WRITE_REQUEST_PER_SECOND = 3;

    /**
     * 接口校验值
     */
    public static final String REQUEST_HEADER_SIGN = "sign";


    public enum PAYMENT_SHEET {
        ;
        /**
         * 回款单列表
         */
        public static final String LIST = "/pro/v2/api/paymentSheet/list";

        /**
         * 回款单详情
         */
        public static final String GET = "/pro/v2/api/paymentSheet/detail";
    }

    /**
     * 表单模块接口地址
     */
    public enum FORM {
        ;
        /**
         * 表单模板列表接口
         */
        public static final String LIST = "/pro/v2/api/form/list";

        /**
         * 表单模板字段解释接口
         */
        public static final String GET = "/pro/v2/api/form/get";

    }

    public enum LABEL {
        ;
        /**
         * 添加标签
         */
        public static final String ADD = "/pro/v2/api/label/batch/add";
        /**
         * 表单标签组
         */
        public static final String FORM_LABEL_LIST = "/pro/v2/api/label/allList";

        /**
         * 移除标签
         */
        public static final String REMOVE = "/pro/v2/api/label/batch/remove";
    }


    /**
     * 客户模块接口地址*
     */
    public enum CUSTOMER {
        ;
        /**
         * 客户列表接口
         */
        public static final String LIST = "/pro/v2/api/customer/list";

        /**
         * 客户新建接口
         */
        public static final String ADD = "/pro/v2/api/customer/add";

        /**
         * 客户分配接口*
         */
        public static final String DISTRIBUTION = "/pro/v2/api/customer/distribution";

        /**
         * 客户详情*
         */
        public static final String GET = "/pro/v2/api/customer/detail";

        /**
         * 客户退回公海池*
         */
        public static final String BACK_TO_PUBLIC_SEA = "/pro/v2/api/customer/back";

        /**
         * 删除客户负责人*
         */
        public static final String OWNER_REMOVE = "/pro/v2/api/customer/deleteMainUser";

        /**
         * 客户移交*
         */
        public static final String HANDOVER = "/pro/v2/api/customer/handover";

        /**
         * 删除客户*
         */
        public static final String DELETE = "/pro/v2/api/customer/del";

        /**
         * 编辑用户*
         */
        public static final String EDIT = "/pro/v2/api/customer/edit";

    }

    /**
     * 用户模块接口地址*
     */
    public enum USER {
        ;
        /**
         * 用户列表（钉钉用户)*
         */
        public static final String LIST = "/pro/v2/api/user/list";

    }

    /**
     * 自定义表单*
     */
    public enum CUSTOM_FORM {
        ;
        /**
         * 添加自定义表单的数据*
         */
        public static final String ADD = "/pro/v2/api/paas/add";
        /**
         * 删除自定义表单的数据*
         */
        public static final String DELETE = "/pro/v2/api/paas/del";
        /**
         * 自定义表单的数据列表*
         */
        public static final String LIST = "/pro/v2/api/paas/list";

        /**
         * 编辑自定义表单的数据*
         */
        public static final String EDIT = "/pro/v2/api/paas/edit";

        /**
         * 自定义表单的数据详情*
         */
        public static final String GET = "/pro/v2/api/paas/detail";
    }

    public enum CONTRACT {
        ;
        /**
         * 合同订单详情
         */
        public static final String GET = "/pro/v2/api/contract/detail";
    }

    /**
     * 获取接口地址
     *
     * @param restApiUrl 请求url
     * @return 请求回参
     */
    public static String getApiUrl(String restApiUrl) {
        return XBB_API_ROOT + restApiUrl;
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
        return DigestUtil.Encrypt(data + token, "SHA-256");
    }


    public static XbbDetailModel get(@NonNull Long dataId, @NonNull String url) throws Exception {
        JSONObject data = new JSONObject();
        data.put("dataId", dataId);
        String response = ConfigConstant.xbbApi(url, data, ApiType.READ);
        XbbResponse<XbbDetailModel> xbbResponse;
        try {
            xbbResponse = JSON.parseObject(response, new TypeReference<>() {
            });
        } catch (Exception e) {
            throw new Exception("json解析出错", e);
        }
        if (Objects.equals(xbbResponse.getCode(), 1)) {
            return xbbResponse.getResult();
        } else {
            throw new Exception(xbbResponse.getMsg());
        }
    }


    /**
     * 调用xbb api，生成签名 ，发起HTTP POST请求
     *
     * @param url,xbb api的接口url，不包含域名,从/开始，例如"/pro/v2/api/form/list"
     * @param data    请求参数(JSON格式)
     * @return 接口回参
     */
    public static String xbbApi(String url, JSONObject data, ApiType apiType) throws XbbException {
        log.debug("Xbb API Request Payload: {}", data.toJSONString());
        try {
            return xbbApi(new XbbApiRequestModel(url, data, apiType, MAX_RETRY));
        } catch (IllegalArgumentException | XbbException e) {
            return JSONObject.toJSONString(new XbbResponse<>(-1, e.getLocalizedMessage(), false, null));
        }
    }

    private static String xbbApi(XbbApiRequestModel requestModel) throws XbbException, IllegalArgumentException {
        if (ENABLE_REQUEST_CONTROL && !XbbRequestControlConfig.proceed(requestModel.getApiType()) && requestModel.retry()) {
            log.info("销帮帮请求重试：{}", requestModel.getRetry());
            try {
                Thread.sleep(300);
                xbbApi(requestModel);
            } catch (InterruptedException e) {
                log.error("销帮帮请求异常: interrupted,{}", e.getLocalizedMessage());
            }
        }
        try {
            final JSONObject data = requestModel.getData();
            data.put("corpid", ConfigConstant.CORP_ID);
            //暂时去掉
//            data.put("userId", ConfigConstant.USER_ID);
            String sign = ConfigConstant.getDataSign(data, TOKEN);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
            headers.set("sign", sign);
            final String requestData = data.toJSONString();
            log.debug("request payload: {}", requestData);
            HttpEntity<String> entity = new HttpEntity<>(requestData, headers);
            final String response = restTemplate.postForObject(ConfigConstant.getApiUrl(requestModel.getUrl()), entity, String.class);
            log.debug("response: {}", response);
            return response;
        } catch (Exception e) {
            log.error("请求异常", e);
            throw new XbbException(-1, "请求异常");
        }
    }

    @Override
    public void afterSingletonsInstantiated() {
        XBB_API_ROOT = gateway;
        CORP_ID = corpId;
        USER_ID = userId;
        TOKEN = token;
        WEBHOOK_TOKEN = webhookToken;
        ENABLE_REQUEST_CONTROL = enableRequestControl;
        Optional.ofNullable(requestPerDay).ifPresent(i -> {
            Assert.isTrue(i <= MAX_TOTAL_REQUEST_PER_DAY, () -> {
                throw new RuntimeException("每天所有请求最大不能超过" + MAX_TOTAL_REQUEST_PER_DAY + "次");
            });
            REQUEST_PER_DAY = requestPerDay;
        });
        Optional.ofNullable(requestPerMinute).ifPresent(i -> {
            Assert.isTrue(i <= MAX_TOTAL_REQUEST_PER_MINUTE, () -> {
                throw new RuntimeException("每分钟所有请求最大不能超过" + MAX_TOTAL_REQUEST_PER_MINUTE + "次");
            });
            REQUEST_PER_MINUTE = requestPerMinute;
        });
        Optional.ofNullable(writePerSecond).ifPresent(i -> {
            Assert.isTrue(i <= MAX_WRITE_REQUEST_PER_SECOND, () -> {
                throw new RuntimeException("每秒写请求最大不能超过" + MAX_WRITE_REQUEST_PER_SECOND + "次");
            });
            WRITE_PER_SECOND = writePerSecond;
        });
        Optional.ofNullable(maxRetry).ifPresent(i -> {
            Assert.isTrue(i <= 10, () -> {
                throw new RuntimeException("最大重试次数不能大于10" + MAX_WRITE_REQUEST_PER_SECOND + "次");
            });
            MAX_RETRY = maxRetry;
        });

        restTemplate = SpringUtil.getBean(RestTemplate.class);
    }
}
