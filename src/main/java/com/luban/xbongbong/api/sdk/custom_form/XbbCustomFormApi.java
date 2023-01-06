package com.luban.xbongbong.api.sdk.custom_form;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.luban.xbongbong.api.helper.config.ConfigConstant;
import com.luban.xbongbong.api.model.XbbResponse;
import com.luban.xbongbong.api.model.custom_form.XbbCustomFormAddResponse;
import com.luban.xbongbong.api.model.custom_form.XbbCustomFormDeleteResponse;
import lombok.NonNull;

import java.util.Objects;

/**
 * @author HP 2023/1/5
 */

public class XbbCustomFormApi {

    /**
     * 添加自定义表单的数据*
     *
     * @param formId   自定义表单id
     * @param dataList 数据体，需要根据表单字段构建
     * @return 响应
     * @throws Exception 请求异常
     */
    public static XbbCustomFormAddResponse add(@NonNull Long formId, @NonNull JSONObject dataList) throws Exception {
        JSONObject data = new JSONObject();
        data.put("formId", formId);
        data.put("dataList", dataList);
        data.put("corpid", ConfigConstant.CORP_ID);
        data.put("userId", ConfigConstant.USER_ID);
        String response = ConfigConstant.xbbApi(ConfigConstant.CUSTOM_FORM.ADD, data);
        XbbResponse<XbbCustomFormAddResponse> xbbResponse;
        try {
            xbbResponse = JSON.parseObject(response, new TypeReference<XbbResponse<XbbCustomFormAddResponse>>() {
            });
        } catch (Exception e) {
            throw new Exception("json解析出错", e);
        }
        if (Objects.equals(xbbResponse.getCode(), 1)) {
            return xbbResponse.getResult();
        } else {
            throw new Exception(xbbResponse.getMsg());
        }
    }

    /**
     * 删除自定义表单数据*
     *
     * @param dataId 数据主键id
     * @return 响应
     * @throws Exception 请求异常
     */
    public static XbbCustomFormDeleteResponse delete(@NonNull Long dataId) throws Exception {
        JSONObject data = new JSONObject();
        data.put("dataId", dataId);
        data.put("corpid", ConfigConstant.CORP_ID);
        data.put("userId", ConfigConstant.USER_ID);
        String response = ConfigConstant.xbbApi(ConfigConstant.CUSTOM_FORM.DELETE, data);
        XbbResponse<XbbCustomFormDeleteResponse> xbbResponse;
        try {
            xbbResponse = JSON.parseObject(response, new TypeReference<XbbResponse<XbbCustomFormDeleteResponse>>() {
            });
        } catch (Exception e) {
            throw new Exception("json解析出错", e);
        }
        if (Objects.equals(xbbResponse.getCode(), 1)) {
            return xbbResponse.getResult();
        } else {
            throw new Exception(xbbResponse.getMsg());
        }
    }
}
