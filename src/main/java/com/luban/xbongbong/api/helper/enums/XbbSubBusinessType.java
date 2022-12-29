package com.luban.xbongbong.api.helper.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 子业务类型*
 *
 * @author HP 2022/12/28
 */
@Getter
@AllArgsConstructor
public enum XbbSubBusinessType {

    DISTRIBUTED_CUSTOMER(101, "非公海客户"),
    UNDISTRIBUTED_CUSTOMER(105, "公海客户"),
    ;
    private Integer code;
    private String name;
}
