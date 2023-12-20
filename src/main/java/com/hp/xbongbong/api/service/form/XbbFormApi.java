package com.hp.xbongbong.api.service.form;

import com.alibaba.fastjson.JSONObject;
import com.hp.xbongbong.api.helper.utils.XbbApiCaller;
import com.hp.xbongbong.api.model.form.XbbFormListResponse;
import com.hp.xbongbong.api.helper.XbbUrl.Form;
import com.hp.xbongbong.api.helper.enums.XbbFormBizType;
import com.hp.xbongbong.api.helper.enums.XbbFormType;
import com.hp.xbongbong.api.model.XbbResponse;
import com.hp.xbongbong.api.model.form.XbbFormFieldResponse;
import lombok.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 表单模板
 *
 * @author hp
 */
public class XbbFormApi {

    private XbbFormApi() {
        throw new AssertionError();
    }

    /**
     * 表单模板列表接口
     *
     * @param name         表单模板名称
     * @param businessType 业务类型
     * @return 接口回参-表单模板列表
     */
    public static List<XbbFormListResponse.XbbFormListModel> list(String name, @NonNull XbbFormType formType, XbbFormBizType businessType) {
        final JSONObject data = new JSONObject();
        data.put("saasMark", formType.getCode());
        Optional.ofNullable(name).ifPresent(i -> data.put("name", i));
        Optional.ofNullable(businessType).ifPresent(i -> data.put("businessType", i.getCode()));

        final XbbResponse<XbbFormListResponse> xbbResponse = XbbApiCaller.call(Form.LIST, data);
        if (xbbResponse.isNullResult()) {
            return Collections.emptyList();
        }
        return xbbResponse.getResult(XbbFormListResponse.class).getFormList();
    }

    /**
     * 表单模板字段解释列表接口
     *
     * @param formId 表单id
     * @return 接口回参-表单模板字段解释
     */
    public static List<XbbFormFieldResponse.XbbFormFieldModel> get(@NonNull Long formId) {
        JSONObject data = new JSONObject();
        data.put("formId", formId);

        final XbbResponse<XbbFormFieldResponse> responseJson = XbbApiCaller.call(Form.GET, data);
        if (responseJson.isNullResult()) {
            return Collections.emptyList();
        }
        return responseJson.getResult(XbbFormFieldResponse.class).getExplainList();
    }
}
