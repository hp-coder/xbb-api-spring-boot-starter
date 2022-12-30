package com.luban.xbongbong.api.helper.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * @author HP 2022/12/30
 */
@Getter
@AllArgsConstructor
public enum XbbFormConditionSymbol {

    eq("equal"),
    in("in"),
    like("like"),
    ;
    private String code;

    public static Optional<XbbFormConditionSymbol> of(String code) {
        return Arrays.stream(values()).filter(i -> Objects.equals(code, i.getCode())).findFirst();
    }
}
