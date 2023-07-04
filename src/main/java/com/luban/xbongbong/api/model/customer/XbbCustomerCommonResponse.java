package com.luban.xbongbong.api.model.customer;

import com.luban.xbongbong.api.model.Response;
import lombok.Data;

/**
 * TODO 后续可能回改成全通用的类
 *
 * @author HP 2022/12/30
 */
@Data
public class XbbCustomerCommonResponse implements Response {
    private String resultMsg;
    private String resultType;
}
