package com.luban.xbongbong.api.helper.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * @author HP 2022/12/27
 */
@Getter
@AllArgsConstructor
public enum XbbFormType {
    SYSTEM(1, "销帮帮系统模板数据"),
    CUSTOM(2, "自定义表单数据"),
    ;
    private Integer code;
    private String name;

    public static Optional<XbbFormType> of(Integer code){
        return Arrays.stream(values()).filter(i -> Objects.equals(i.getCode(), code)).findFirst();
    }
}
