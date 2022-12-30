package com.luban.xbongbong.api.helper.config;

import com.alibaba.fastjson.JSONObject;
import com.luban.xbongbong.api.helper.exception.XbbException;
import com.luban.xbongbong.api.helper.utils.DigestUtil;
import com.luban.xbongbong.api.helper.utils.HttpRequestUtils;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

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

    /* ---------------------------------------------表单模块接口地址------------------------------------------------*/

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



    /* ---------------------------------------------客户模块接口地址------------------------------------------------*/

    public enum  CUSTOMER {
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

    }

    /* ---------------------------------------------用户模块接口地址------------------------------------------------*/

    public enum USER{
        ;
        /**
         * 用户列表（钉钉用户)*
         */
        public static final String LIST = "/pro/v2/api/user/list";

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
    public static String xbbApi(String url, JSONObject data) throws XbbException {
        log.debug("Xbb API Request Payload: {}", data.toJSONString());
        String absoluteUrl = ConfigConstant.getApiUrl(url);
        //签名规则:将访问接口所需的参数集data + token字符串拼接后进行SHA256运算得到最后的签名,然后将签名参数sign(参数名为sign)放入http header中;
        // 			将访问接口所需的参数集data(参数名为data)放入http body。
        // 			算法为 sha-256 ( data+token ),使用utf-8编码
        String sign = ConfigConstant.getDataSign(data, TOKEN);
        String response;
        try {
            //发起post请求，data作为 request body，sign在 http-header中传输
            response = HttpRequestUtils.post(absoluteUrl, data.toJSONString(), sign);
        } catch (Exception e) {
            throw new XbbException(-1, "http post访问出错");
        }
        return response;
    }

    @Override
    public void afterSingletonsInstantiated() {
        XBB_API_ROOT = gateway;
        CORP_ID = corpId;
        USER_ID = userId;
        TOKEN = token;
        WEBHOOK_TOKEN = webhookToken;
    }
}
