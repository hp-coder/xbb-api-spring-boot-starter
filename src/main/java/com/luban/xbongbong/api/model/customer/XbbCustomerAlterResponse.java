package com.luban.xbongbong.api.model.customer;

import com.luban.common.base.model.Response;
import lombok.Data;

/**
 * @author HP 2022/12/30
 */
@Data
public class XbbCustomerAlterResponse implements Response {
    private Integer code;
    private Long dataId;
    private String msg;
}
