package com.luban.xbongbong.api.helper.enums.paymentsheet;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author hp
 */
@Getter
@AllArgsConstructor
public enum XbbPaymentSheetGroup {
    REFUND(110, "红冲退款"),
    BAD_RECEIVE(111, "坏账回款"),
    ;
    private final int code;
    private final String name;
}
