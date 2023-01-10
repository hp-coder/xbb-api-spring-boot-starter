package com.luban.xbongbong.api.sdk.form;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.luban.xbongbong.api.helper.config.ConfigConstant;
import com.luban.xbongbong.api.helper.enums.XbbFormBizType;
import com.luban.xbongbong.api.helper.enums.XbbFormType;
import com.luban.xbongbong.api.model.XbbResponse;
import com.luban.xbongbong.api.model.form.XbbFormListResponse;
import lombok.NonNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 表单模板
 */
public class XbbFormApi {

    /**
     * 表单模板列表接口
     *
     * @param name         表单模板名称
     * @param businessType 业务类型
     * @return 接口回参-表单模板列表
     * @throws Exception 异常
     */
    public static List<XbbFormListResponse.FormList> list(String name, @NonNull XbbFormType formType, XbbFormBizType businessType) throws Exception {
        //创建请求参数data
        JSONObject data = new JSONObject();
        data.put("saasMark", formType.getCode());
        Optional.ofNullable(name).ifPresent(_0 -> data.put("name", _0));
        Optional.ofNullable(businessType).ifPresent(_0 -> data.put("businessType", _0.getCode()));
        //调用xbbApi方法，发起API请求
        String response = ConfigConstant.xbbApi(ConfigConstant.FORM.LIST, data);
        //对返回值进行解析
        XbbResponse<XbbFormListResponse> xbbResponse;
        try {
            xbbResponse = JSON.parseObject(response, new TypeReference<XbbResponse<XbbFormListResponse>>() {
            });
        } catch (Exception e) {
            throw new Exception("json解析出错", e);
        }
        List<XbbFormListResponse.FormList> retArray = null;
        if (Objects.equals(xbbResponse.getCode(), 1)) {
            final XbbFormListResponse result = xbbResponse.getResult();
            if (result != null) {
                retArray = result.getFormList();
            }
            return retArray;
        } else {
            throw new Exception(xbbResponse.getMsg());
        }
    }

    /**
     * 表单模板字段解释列表接口
     *
     * @param formId 表单id
     * @return 接口回参-表单模板字段解释
     * @throws Exception 异常
     */
    public static JSONArray get(@NonNull Long formId) throws Exception {
        //创建请求参数data
        JSONObject data = new JSONObject();
        data.put("formId", formId);
        //调用xbbApi方法， 发起API请求
        String response = ConfigConstant.xbbApi(ConfigConstant.FORM.GET, data);
        //对返回值进行解析
        JSONObject responseJson;
        try {
            responseJson = JSON.parseObject(response);
        } catch (Exception e) {
            throw new Exception("json解析出错", e);
        }
        JSONArray retArray = null;
        if (responseJson.containsKey("code") && responseJson.getInteger("code").equals(1)) {
            JSONObject result = responseJson.getJSONObject("result");
            if (result != null) {
                retArray = result.getJSONArray("explainList");
            }
            return retArray;
        } else {
            throw new Exception(responseJson.getString("msg"));
        }
    }
}
