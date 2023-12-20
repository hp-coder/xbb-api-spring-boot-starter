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
public enum XbbSubBusinessType implements BaseEnum<XbbSubBusinessType, Integer> {
    /***/
    DISTRIBUTED_CUSTOMER(101, "非公海客户"),
    UNDISTRIBUTED_CUSTOMER(105, "公海客户"),
    ;
    private final Integer code;
    private final String name;

    public static Optional<XbbSubBusinessType> of(Integer code) {
        return Optional.ofNullable(BaseEnum.parseByCode(XbbSubBusinessType.class, code));
    }

    public static Optional<XbbSubBusinessType> ofName(String name) {
        return Arrays.stream(values())
                .filter(i -> Objects.equals(name, i.getName()))
                .findFirst();
    }
}
