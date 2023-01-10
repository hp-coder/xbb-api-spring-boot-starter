package com.luban.xbongbong.api.biz.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
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
public class XbbBizConfig {

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
}
