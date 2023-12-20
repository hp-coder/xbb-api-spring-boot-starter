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
public enum XbbFormType implements BaseEnum<XbbFormType, Integer> {
    /***/
    SYSTEM(1, "销帮帮系统模板数据"),
    CUSTOM(2, "自定义表单数据"),
    ;
    private final Integer code;
    private final String name;

    public static Optional<XbbFormType> of(Integer code) {
        return Optional.ofNullable(BaseEnum.parseByCode(XbbFormType.class, code));
    }

    public static Optional<XbbFormType> ofName(String name) {
        return Arrays.stream(values())
                .filter(i -> Objects.equals(name, i.getName()))
                .findFirst();
    }
}
