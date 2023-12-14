package com.luban.xbongbong.api.model.customer;

import com.luban.common.base.model.Response;
import lombok.Data;

import java.util.Objects;

/**
 * TODO 后续可能回改成全通用的类
 *
 * @author HP 2022/12/30
 */
@Data
public class XbbCustomerCommonResponse implements Response {
    private String resultMsg;
    private String resultType;

    public boolean succeed() {
        return Objects.equals("success", getResultType());
    }
}
