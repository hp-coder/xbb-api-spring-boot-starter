package com.luban.xbongbong.api.helper.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author HP 2023/1/5
 */
@Getter
@AllArgsConstructor
public enum UserDataSource {

    TEST("测试数据"),
    XBB("销帮帮自建"),
    LEBIAO_CMS("乐标后台"),
    DIAO_XIAO("电销平台"),
    APP("app后台"),
    ;

    private String name;
}
