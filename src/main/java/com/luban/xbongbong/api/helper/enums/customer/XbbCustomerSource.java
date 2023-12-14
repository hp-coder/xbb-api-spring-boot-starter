package com.luban.xbongbong.api.helper.enums.customer;

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
public enum XbbCustomerSource implements BaseEnum<XbbCustomerSource, String> {
    /***/
    SEO("1", "网络推广"),
    TELESALES("2", "电话销售"),
    CLIENT_("3", "客户转介"),
    MO_BAI("6a4567fa-ab71-39e9-a086-16fd68701354", "陌拜开发"),
    LEBIAO_REGISTER("c91b7616-ffab-c8a9-107e-c24e06b3796a", "乐标注册用户"),
    COMPANY_RESOURCES("e64d5caa-611e-c994-d400-7ddfacf91cdb", "公司资源"),
    ;
    private final String code;
    private final String name;

    public static Optional<XbbCustomerSource> of(String code) {
        return Optional.ofNullable(BaseEnum.parseByCode(XbbCustomerSource.class, code));
    }

    public static Optional<XbbCustomerSource> ofName(String name) {
        return Arrays.stream(values())
                .filter(i -> Objects.equals(name, i.getName()))
                .findFirst();
    }
}
