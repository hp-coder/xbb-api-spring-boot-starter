package com.luban.xbongbong.api.model.label;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 固定的用户表单标签
 *
 * @author HP 2023/2/2
 */
@Getter
@AllArgsConstructor
public enum XbbCustomerLabel {
    /***/
    VIP(120352, "VIP会员"), EXPIRED_VIP(120353, "过期VIP会员"), CUSTOMER(120354, "注册会员"),
    ;
    private final long code;
    private final String name;

    public static XbbCustomerLabel getByName(String name) throws Exception {
        return Arrays.stream(XbbCustomerLabel.values()).filter(i -> Objects.equals(i.getName(), name)).findFirst().orElse(null);
    }
}
