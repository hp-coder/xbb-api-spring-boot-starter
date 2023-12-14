package com.luban.xbongbong.api.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.luban.common.base.model.Request;
import lombok.Data;

/**
 * @author HP 2023/2/2
 */
@Data
public abstract class AbstractXbbBaseModel implements Request {
    public JSONObject json() {
        return JSON.parseObject(JSON.toJSONString(this, SerializerFeature.PrettyFormat));
    }
}
