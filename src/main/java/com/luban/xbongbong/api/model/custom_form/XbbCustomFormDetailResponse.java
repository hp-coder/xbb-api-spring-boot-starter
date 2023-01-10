
package com.luban.xbongbong.api.model.custom_form;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.List;

/**
 * @author HP 2023/1/9
 */
@Data
public class XbbCustomFormDetailResponse {
    private Long addTime;
    private JSONObject data;
    private Long dataId;
    private Long formId;
    private Long updateTime;
}