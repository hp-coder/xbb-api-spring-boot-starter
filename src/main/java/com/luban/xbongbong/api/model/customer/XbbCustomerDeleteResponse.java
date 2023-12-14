package com.luban.xbongbong.api.model.customer;

import cn.hutool.core.util.StrUtil;
import com.luban.common.base.model.Response;
import lombok.Data;

/**
 * @author HP 2022/12/30
 */
@Data
public class XbbCustomerDeleteResponse implements Response {
    private String errorDataMemo;

    public boolean succeed() {
        return StrUtil.isEmpty(getErrorDataMemo());
    }
}
