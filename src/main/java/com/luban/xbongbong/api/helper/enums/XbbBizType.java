package com.luban.xbongbong.api.helper.enums;

import com.luban.common.base.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * @author hp
 */
@Getter
@AllArgsConstructor
public enum XbbBizType implements BaseEnum<XbbBizType, String> {
    /***/
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
    private final String code;
    private final String name;

    public static Optional<XbbBizType> of(String code) {
        return Optional.ofNullable(BaseEnum.parseByCode(XbbBizType.class, code));
    }

    public static Optional<XbbBizType> ofName(String name) {
        return Arrays.stream(values())
                .filter(i -> Objects.equals(name, i.getName()))
                .findFirst();
    }
}
