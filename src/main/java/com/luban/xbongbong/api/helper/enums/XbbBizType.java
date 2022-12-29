package com.luban.xbongbong.api.helper.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * @author HP 2022/12/27
 */
@Getter
@AllArgsConstructor
public enum XbbBizType {
    customer("customer", "客户"),
    contract("contract", "合同订单"),
    salesOpportunity("salesOpportunity", "销售机会"),
    system("system", "自定义 "),
    payment("payment ", "应收款 "),
    paymentSheet("paymentSheet", "回款单 "),
    product(" product", "产品"),
    clue("clue", "销售线索"),
    customerCommunicate("customerCommunicate ", "跟进记录"),
    warehouse("warehouse ", "仓库"),
    contact("contact ", "联系人 "),
    purchase("purchase", "采购合同"),
    quotation("quotation ", "报价单 "),
    ;

    private String code;
    private String name;

    public static Optional<XbbBizType> of(String code) {
        return Arrays.stream(values()).filter(i-> Objects.equals(i.getCode(),code)).findFirst();
    }
}
