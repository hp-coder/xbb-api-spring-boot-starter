package com.luban.xbongbong.api.helper.enums;

/**
 * 回调事件操作类型*
 * @author HP 2022/12/27
 */

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
public enum XbbOperateType implements BaseEnum<XbbOperateType, String> {
    /***/
    NEW("new", "新增"),
    EDIT("edit", "编辑"),
    DELETE("delete", "删除"),
    ARCHIVED("archived", "归档"),
    CANCEL_ARCHIVED("cancelArchived", "取消归档"),
    ;
    private final String code;
    private final String name;

    public static Optional<XbbOperateType> of(String code) {
        return Optional.ofNullable(BaseEnum.parseByCode(XbbOperateType.class, code));
    }

    public static Optional<XbbOperateType> ofName(String name) {
        return Arrays.stream(values())
                .filter(i -> Objects.equals(name, i.getName()))
                .findFirst();
    }
}
