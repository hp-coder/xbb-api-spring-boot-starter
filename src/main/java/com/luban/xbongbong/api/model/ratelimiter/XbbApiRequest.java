package com.luban.xbongbong.api.model.ratelimiter;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Preconditions;
import com.luban.common.base.model.Request;
import com.luban.xbongbong.api.helper.XbbUrl;
import com.luban.xbongbong.api.helper.exception.XbbException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @author hp 2023/3/9
 */
@Slf4j
@Getter
public class XbbApiRequest implements Request {

    private final XbbUrl url;
    private final JSONObject data;
    private int retry;

    public XbbApiRequest(XbbUrl url, JSONObject data, int retry) {
        Preconditions.checkArgument(Objects.nonNull(url));
        Preconditions.checkArgument(Objects.nonNull(data));
        this.url = url;
        this.data = data;
        this.retry = retry;
    }

    public String getRequestUrl() {
        return url.getRequestUrl();
    }

    public boolean retryable() {
        if (this.retry > 0) {
            this.retry -= 1;
            return true;
        }
        log.error(this.toString());
        throw new XbbException(-1, "销帮帮API请求以达到重试上限: " + this.url);
    }

    public String getLogInfo() {
        if (Objects.isNull(data)) {
            return "request=null" + "||" + url.getLogInfo();
        }
        return "request=" + data.toJSONString() + "||" + url.getLogInfo();
    }
}
