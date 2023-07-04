package com.luban.xbongbong.api.model.common.list;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.luban.xbongbong.api.model.Response;
import lombok.Data;

import java.util.Optional;

/**
 * @author hp
 */
@Data
public class XbbListModel implements Response {
    private Long addTime;
    private JSONObject data;
    private Long dataId;
    private Long formId;
    private Long updateTime;

    public <T> T getData(Class<T> clz) {
        if (Optional.ofNullable(data).isPresent()) {
            return JSON.parseObject(data.toJSONString(), clz);
        }
        return null;
    }
}
