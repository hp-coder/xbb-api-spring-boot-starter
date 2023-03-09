package com.luban.xbongbong.api.model.request;

import com.alibaba.fastjson.JSONObject;
import com.luban.xbongbong.api.helper.enums.api.ApiType;
import com.luban.xbongbong.api.helper.exception.XbbException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hp 2023/3/9
 */
@Slf4j
@Getter
public class XbbApiRequestModel {
    private final String url;
    private final JSONObject data;
    private final ApiType apiType;
    private int retry;

    public XbbApiRequestModel(String url, JSONObject data, ApiType apiType, int retry) {
        this.url = url;
        this.data = data;
        this.apiType = apiType;
        this.retry = retry;
    }

    public boolean retry() {
        if (this.retry > 0) {
            this.retry -= 1;
            return true;
        }
        log.error(this.toString());
        throw new XbbException(-1, "请求以达到重试上限: " + this.url);
    }
}
