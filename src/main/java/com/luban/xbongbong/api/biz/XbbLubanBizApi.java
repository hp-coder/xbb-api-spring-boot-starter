package com.luban.xbongbong.api.biz;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.luban.xbongbong.api.biz.config.XbbBizConfig;
import com.luban.xbongbong.api.biz.model.Xbb中标记录;
import com.luban.xbongbong.api.biz.model.Xbb开标记录;
import com.luban.xbongbong.api.helper.enums.XbbFormConditionSymbol;
import com.luban.xbongbong.api.model.XbbFormCondition;
import com.luban.xbongbong.api.model.custom_form.XbbCustomFormAlterResponse;
import com.luban.xbongbong.api.model.customer.XbbCustomerListResponse;
import com.luban.xbongbong.api.sdk.custom_form.XbbCustomFormApi;
import com.luban.xbongbong.api.sdk.customer.XbbCustomerApi;
import lombok.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 对鲁班的业务接口 *
 *
 * @author HP 2023/1/6
 */
@Component
public class XbbLubanBizApi implements ApplicationContextAware {

    private static XbbBizConfig XBB_BIZ_CONFIG;


    /**
     * 根据qyId查询所有用户*
     *
     * @param qyIds 企业ids
     * @return Optional Map key：企业id；value：销帮帮用户id集合
     * @throws Exception 请求异常
     */
    public static Optional<Map<Long, List<Long>>> getCustomerIdsByQyIds(List<Long> qyIds) throws Exception {
        List<XbbCustomerListResponse.XbbCustomer> total = new ArrayList<>();
        List<XbbCustomerListResponse.XbbCustomer> list = null;
        int page = 1;
        do {
            final XbbFormCondition xbbFormCondition = new XbbFormCondition();
            xbbFormCondition.setValue(qyIds.stream().map(String::valueOf).collect(Collectors.toList()));
            xbbFormCondition.setAttr(XBB_BIZ_CONFIG.getCustomerFormCorpIdFieldName());
            xbbFormCondition.setSymbol(XbbFormConditionSymbol.in);
            final XbbCustomerListResponse response = XbbCustomerApi.list(XBB_BIZ_CONFIG.getCustomerFormId(), Collections.singletonList(xbbFormCondition), null, null, page++, 100);
            if (response != null) {
                list = response.getList();
                total.addAll(list);
            }
        } while (CollUtil.isNotEmpty(list));
        if (CollUtil.isNotEmpty(total)) {
            final HashMap<Long, List<Long>> result =
                    total.stream()
                            .collect(
                                    Collectors.groupingBy(
                                            customer -> customer.getData().getLong(XBB_BIZ_CONFIG.getCustomerFormCorpIdFieldName()),
                                            HashMap::new,
                                            Collectors.mapping(XbbCustomerListResponse.XbbCustomer::getDataId, Collectors.toList())
                                    )
                            );
            return Optional.of(result);
        }
        return Optional.empty();
    }

    /**
     * 添加开标记录*
     *
     * @param openBid 开标记录对象
     * @return 是否成功
     * @throws RuntimeException 异常
     */
    public static boolean addOpenBid(@NonNull Xbb开标记录 openBid) throws RuntimeException {
        final Long formId = XbbCustomFormApi.checkIfCustomFormExists(XBB_BIZ_CONFIG.getOpenBidFormName());
        //TODO 查询企业id
        try {
            final XbbCustomFormAlterResponse add = XbbCustomFormApi.add(formId, JSON.parseObject(JSON.toJSONString(openBid)));
            if (add != null && add.getDataId() != null) {
                return true;
            }
        } catch (Exception e) {
            throw new RuntimeException("添加企业开标记录失败", e);
        }
        return false;
    }

    /**
     * 添加中标记录*
     *
     * @param bidWinning 中标记录对象
     * @return 返回
     * @throws RuntimeException 异常*
     */
    public static boolean addBidWinning(@NonNull Xbb中标记录 bidWinning) throws RuntimeException {
        final Long formId = XbbCustomFormApi.checkIfCustomFormExists(XBB_BIZ_CONFIG.getBidWinningName());
        try {
            final XbbCustomFormAlterResponse add = XbbCustomFormApi.add(formId, JSONObject.parseObject(JSON.toJSONString(bidWinning)));
            if (add != null && add.getDataId() != null) {
                return true;
            }
        } catch (Exception e) {
            throw new RuntimeException("添加企业中标记失败", e);
        }
        return false;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        XBB_BIZ_CONFIG = applicationContext.getBean(XbbBizConfig.class);
    }
}
