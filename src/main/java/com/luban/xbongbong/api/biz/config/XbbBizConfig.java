package com.luban.xbongbong.api.biz.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 业务配置
 *
 * @author HP 2023/1/9
 */
@Slf4j
@Getter
@Setter
@ConfigurationProperties(prefix = "xbb.biz")
@Component
public class XbbBizConfig implements SmartInitializingSingleton {

    public static String CUSTOMER_FORM_CORP_ID_FIELD_NAME;
    public static String OPEN_BID_FORM_NAME;
    public static String BID_WINNING_NAME;
    public static Long CUSTOMER_FORM_ID;

    /**
     * 企业客户表单企业id字段名称*
     */
    private String customerFormCorpIdFieldName;

    /**
     * 企业客户表单id*
     */
    private Long customerFormId;

    /**
     * 企业开标记录表名*
     */
    private String openBidFormName;
    /**
     * 企业中标记录表名*
     */
    private String bidWinningName;

    @Override
    public void afterSingletonsInstantiated() {
        CUSTOMER_FORM_CORP_ID_FIELD_NAME = this.getCustomerFormCorpIdFieldName();
        CUSTOMER_FORM_ID = this.getCustomerFormId();
        OPEN_BID_FORM_NAME = this.getOpenBidFormName();
        BID_WINNING_NAME = this.getBidWinningName();
    }
}
