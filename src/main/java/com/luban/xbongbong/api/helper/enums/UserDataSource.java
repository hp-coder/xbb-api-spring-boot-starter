package com.luban.xbongbong.api.helper.enums;

import com.luban.common.base.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

/**
 * @author hp
 */
@Getter
@AllArgsConstructor
public enum UserDataSource implements BaseEnum<UserDataSource, String> {
    /***/
    TEST("测试数据", ""),
    XBB("销帮帮自建", ""),
    LEBIAO_CMS("乐标后台", ""),
    DIAN_XIAO("电销平台", ""),
    APP("app后台", ""),
    ;
    private final String code;
    private final String name;

    public static Optional<UserDataSource> of(String code) {
        return Optional.ofNullable(BaseEnum.parseByCode(UserDataSource.class, code));
    }
}
