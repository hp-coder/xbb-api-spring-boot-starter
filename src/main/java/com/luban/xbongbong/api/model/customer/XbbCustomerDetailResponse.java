package com.luban.xbongbong.api.model.customer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import jdk.jfr.internal.OldObjectSample;
import lombok.Data;

/**
 * @author HP 2022/12/30
 */
@Data
public class XbbCustomerDetailResponse {
    private Long addTime;
    private Long updateTime;
    private JSONObject data;
    private Long dataId;
    private Long formId;
}
