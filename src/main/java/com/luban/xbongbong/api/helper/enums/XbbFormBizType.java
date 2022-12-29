package com.luban.xbongbong.api.helper.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 * @author HP 2022/12/29
 */
@Getter
@AllArgsConstructor
public enum XbbFormBizType {

    客户(100, "客户"),
    合同订单(201, "合同订单"),
    退货退款(202, "退货退款"),
    销售机会(301, "销售机会"),
    联系人(401, " 联系人"),
    跟进记录(501, "跟进记录"),
    回款计划(701, "回款计划"),
    回款单(702, "回款单"),
    销项发票(901, "销项发票"),
    供应商(1001, "供应商"),
    采购合同(1101, "采购合同"),
    采购入库单(1404, "采购入库单"),
    其他入库单(1406, "其他入库单"),
    销售出库单(1504, "销售出库单"),
    其他出库单(1506, "其他出库单"),
    调拨单(1601, "调拨单"),
    盘点单(1701, "盘点单"),
    产品(401, "产品"),
    报价单(4700, "报价单"),
    线索(000, "线索"),
    市场活动(8100, "市场活动"),
    仓库(801, "仓库"),
    工作报告(2101, "工作报告"),
    日报(102, "日报"),
    周报(103, "周报"),
    月报(104, "月报"),
    访客计划(601, "访客计划"),
    自定义表单(null, "自定义表单"),
    ;

    private Integer code;
    private String name;

    public static Optional<XbbFormBizType> of(Integer code) {
        return Arrays.stream(values()).filter(_0 -> Objects.equals(code, _0.getCode())).findFirst();
    }
}
