package com.luban.xbongbong.api.model.common.detail;

import com.alibaba.fastjson.JSON;
import com.luban.common.base.model.Response;
import lombok.Data;

import java.util.Optional;

/**
 * @author hp
 */
@Data
public class XbbDetailModel implements Response {
    private Long addTime;
    private String data;
    private Long dataId;
    private Long formId;
    private Long updateTime;

    public <T> T getData(Class<T> clz) {
        if (Optional.ofNullable(data).isPresent()) {
            return JSON.parseObject(data, clz);
        }
        return null;
    }
}
