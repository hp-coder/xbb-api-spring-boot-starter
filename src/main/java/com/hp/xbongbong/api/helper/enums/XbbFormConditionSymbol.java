package com.hp.xbongbong.api.helper.enums;

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
public enum XbbFormConditionSymbol implements BaseEnum<XbbFormConditionSymbol, String> {
    /***/
    eq("equal", "等于"),
    in("in", "包括"),
    like("like", "相似"),
    ;
    private final String code;
    private final String name;

    public static Optional<XbbFormConditionSymbol> of(String code) {
        return Optional.ofNullable(BaseEnum.parseByCode(XbbFormConditionSymbol.class, code));
    }

    public static Optional<XbbFormConditionSymbol> ofName(String name) {
        return Arrays.stream(values())
                .filter(i -> Objects.equals(name, i.getName()))
                .findFirst();
    }
}
