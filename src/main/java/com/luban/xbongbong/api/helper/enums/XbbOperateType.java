package com.luban.xbongbong.api.helper.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * 回调事件操作类型*
 * @author HP 2022/12/27
 */

@Getter
@AllArgsConstructor
public enum XbbOperateType {

    NEW("new", "新增"),
    EDIT("edit", "编辑"),
    DELETE("delete", "删除"),
    ARCHIVED("archived", "归档"),
    CANCEL_ARCHIVED("cancelArchived", "取消归档"),
    ;

    private String code;
    private String name;

    public static Optional<XbbOperateType> of(String operate){
        return Arrays.stream(values()).filter(i -> Objects.equals(operate, i.getCode())).findFirst();
    }
}
