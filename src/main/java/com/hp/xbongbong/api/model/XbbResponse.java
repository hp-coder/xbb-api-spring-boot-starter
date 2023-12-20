package com.hp.xbongbong.api.model;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.luban.common.base.model.Response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * @author HP 2022/12/30
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class XbbResponse<E> implements Response {

    private Integer code;

    private String msg;

    private Boolean success;

    private String result;

    public E getResult(Class<E> eClass) {
        if (StrUtil.isEmpty(result)) {
            return null;
        }
        return JSON.parseObject(result, eClass);
    }

    public boolean succeed() {
        return 1 == code && success;
    }

    public boolean isNullResult() {
        return Objects.isNull(result);
    }

    public String getLogInfo() {
        return String.format("response={\"code\":%s,\"msg\":\"%s\",\"success\":%s}", code, msg, success);
    }
}
