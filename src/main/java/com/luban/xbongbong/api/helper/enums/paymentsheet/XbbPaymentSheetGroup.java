package com.luban.xbongbong.api.helper.enums.paymentsheet;

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
public enum XbbPaymentSheetGroup implements BaseEnum<XbbPaymentSheetGroup, Integer> {
    /***/
    REFUND(110, "红冲退款"),
    BAD_RECEIVE(111, "坏账回款"),
    ;
    private final Integer code;
    private final String name;

    public static Optional<XbbPaymentSheetGroup> of(Integer code) {
        return Optional.ofNullable(BaseEnum.parseByCode(XbbPaymentSheetGroup.class, code));
    }

    public static Optional<XbbPaymentSheetGroup> ofName(String name) {
        return Arrays.stream(values())
                .filter(i -> Objects.equals(name, i.getName()))
                .findFirst();
    }
}
