package com.luban.xbongbong.api.helper.config;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSONObject;
import com.luban.xbongbong.api.helper.enums.api.ApiType;
import com.luban.xbongbong.api.helper.exception.XbbException;
import com.luban.xbongbong.api.helper.utils.DigestUtil;
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

    /**
     * 销帮帮接口根域名
     */
    @NonNull
    public static String XBB_API_ROOT;

    /**
     * 本公司corpid。接口基础参数，接口请求必传
     * 管理员账号登录销帮帮WEB版后台后，访问 https://pfweb.xbongbong.com/#/apiToken/index 查看
     */
    @NonNull
    public static String CORP_ID;

    /**
     * 本公司访问接口的token,该值相当于密钥，请妥善保管，不要泄露，可以下列url中重置token
     * 管理员账号登录销帮帮WEB版后台后，访问 https://pfweb.xbongbong.com/#/apiToken/index 查看
     */
    @NonNull
    public static String TOKEN;

    /**
     * webhook的校验token*
     * 管理员账号登录销帮帮WEB版后台后，访问 https://pfweb.xbongbong.com/#/apiToken/index 查看*
     */
    @NonNull
    public static String WEBHOOK_TOKEN;

    /**
     * 接口操作人userId,接口基础参数。不传默认取超管角色
     */
    @NonNull
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

    /**
     * 调用xbb api，生成签名 ，发起HTTP POST请求
     *
     * @param url,xbb api的接口url，不包含域名,从/开始，例如"/pro/v2/api/form/list"
     * @param data    请求参数(JSON格式)
     * @return 接口回参
     */
    public static String xbbApi(String url, JSONObject data, ApiType apiType) throws XbbException {
        log.debug("Xbb API Request Payload: {}", data.toJSONString());
        if (ENABLE_REQUEST_CONTROL && !XbbRequestControlConfig.proceed(apiType)) {
            try {
                Thread.sleep(200);
                xbbApi(url, data, apiType);
            } catch (InterruptedException ignore) {
                log.error(ignore.getLocalizedMessage());
            }
        }
        try {
            data.put("corpid", ConfigConstant.CORP_ID);
            data.put("userId", ConfigConstant.USER_ID);
            String sign = ConfigConstant.getDataSign(data, TOKEN);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
            headers.set("sign", sign);
            HttpEntity<String> entity = new HttpEntity<>(data.toJSONString(), headers);

            final String response =  SpringUtil.getBean(RestTemplate.class).postForObject(ConfigConstant.getApiUrl(url), entity, String.class);
            log.debug("response: {}", response);
            return response;
        } catch (Exception e) {
            throw new XbbException(-1, "http post访问出错");
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
    }
}
