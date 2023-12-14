package com.luban.xbongbong.api.helper.enums.label;

import com.luban.common.base.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author hp
 */
@Getter
@AllArgsConstructor
public enum XbbCustomerLabel implements BaseEnum<XbbCustomerLabel, Long> {
    /***/
    VIP(120352L, "VIP会员"),
    EXPIRED_VIP(120353L, "过期VIP会员"),
    CUSTOMER(120354L, "注册会员"),
    ;
    private final Long code;
    private final String name;

    public static Optional<XbbCustomerLabel> of(Long code) {
        return Optional.ofNullable(BaseEnum.parseByCode(XbbCustomerLabel.class, code));
    }

    public static List<XbbCustomerLabel> besidesThis(String name) throws Exception {
        return Arrays.stream(XbbCustomerLabel.values()).filter(i -> !Objects.equals(i.getName(), name)).collect(Collectors.toList());
    }

    public static Optional<XbbCustomerLabel> ofName(String name) {
        return Arrays.stream(values())
                .filter(i -> Objects.equals(name, i.getName()))
                .findFirst();
    }
}
