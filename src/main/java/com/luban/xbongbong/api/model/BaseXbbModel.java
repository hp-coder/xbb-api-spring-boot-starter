package com.luban.xbongbong.api.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @author HP 2023/2/2
 */
@Data
public abstract class BaseXbbModel implements Request {

    public JSONObject json(){
        return JSON.parseObject(JSON.toJSONString(this));
    }
}
