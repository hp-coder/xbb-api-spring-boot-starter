package com.hp.xbongbong.api.service.customform;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.hp.xbongbong.api.helper.exception.XbbException;
import com.hp.xbongbong.api.helper.utils.XbbApiCaller;
import com.hp.xbongbong.api.model.customform.XbbCustomFormAlterResponse;
import com.hp.xbongbong.api.model.customform.XbbCustomFormDeleteResponse;
import com.hp.xbongbong.api.model.customform.XbbCustomFormListResponse;
import com.hp.xbongbong.api.model.form.XbbFormListResponse;
import com.hp.xbongbong.api.service.form.XbbFormApi;
import com.hp.xbongbong.api.helper.enums.XbbFormType;
import com.hp.xbongbong.api.model.XbbFormCondition;
import com.hp.xbongbong.api.model.XbbResponse;
import com.hp.xbongbong.api.model.common.detail.XbbDetailModel;
import lombok.NonNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.hp.xbongbong.api.helper.XbbUrl.CustomForm;

/**
 * @author HP 2023/1/5
 */

public class XbbCustomFormApi {

    private XbbCustomFormApi() {
        throw new AssertionError();
    }

    /**
     * 查询是否有该名称的自定义表单*
     *
     * @param formName 自定义表单名称
     * @return formId 表单id
     */
    public static Long checkIfCustomFormExists(@NonNull String formName) {
        Long formId;
        try {
            formId = XbbCustomFormApi.getCustomFormId(formName);
            if (formId == null) {
                throw new XbbException("未查询到同名自定义表单： " + formName);
            }
            return formId;
        } catch (Exception e) {
            throw new XbbException("获取自定义表单" + formName + "失败", e);
        }
    }

    /**
     * 根据表单名称查询表单id*
     *
     * @param formName 表单名称
     * @return 表单formId
     */
    public static Long getCustomFormId(@NonNull String formName) {
        final List<XbbFormListResponse.XbbFormListModel> list = XbbFormApi.list(formName, XbbFormType.CUSTOM, null);
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        final Optional<XbbFormListResponse.XbbFormListModel> optional = list.stream()
                .filter(form -> Objects.equals(formName, form.getName()))
                .findFirst();
        return optional.map(XbbFormListResponse.XbbFormListModel::getFormId)
                .orElse(null);
    }

    /**
     * 添加自定义表单的数据*
     *
     * @param formId   自定义表单id
     * @param dataList 数据体，需要根据表单字段构建
     * @return 响应
     */
    public static XbbCustomFormAlterResponse add(@NonNull Long formId, @NonNull JSONObject dataList) {
        JSONObject data = new JSONObject();
        data.put("formId", formId);
        data.put("dataList", dataList);

        final XbbResponse<XbbCustomFormAlterResponse> xbbResponse = XbbApiCaller.call(CustomForm.ADD, data);
        return xbbResponse.getResult(XbbCustomFormAlterResponse.class);
    }

    /**
     * 修改自定义表单的数据*
     *
     * @param formId   自定义表单id
     * @param dataId   数据id
     * @param dataList 数据体
     * @return 响应
     */
    public static XbbCustomFormAlterResponse edit(@NonNull Long formId, @NonNull Long dataId, @NonNull JSONObject dataList) {
        JSONObject data = new JSONObject();
        data.put("formId", formId);
        data.put("dataId", dataId);
        data.put("dataList", dataList);

        final XbbResponse<XbbCustomFormAlterResponse> xbbResponse = XbbApiCaller.call(CustomForm.EDIT, data);
        return xbbResponse.getResult(XbbCustomFormAlterResponse.class);
    }

    /**
     * 获取自定义表单数据列表*
     *
     * @param formId     表单id
     * @param conditions 查询条件
     * @param page       页码
     * @param pageSize   每页记录数
     * @return XbbCustomFormListResponse 结果集
     */
    public static XbbCustomFormListResponse list(
            @NonNull Long formId,
            List<XbbFormCondition> conditions,
            Integer page,
            Integer pageSize
    ) {
        JSONObject data = new JSONObject();
        data.put("formId", formId);
        if (CollUtil.isNotEmpty(conditions)) {
            data.put("conditions", conditions);
        }
        Optional.ofNullable(page).ifPresent(p -> data.put("page", p));
        Optional.ofNullable(pageSize).ifPresent(p -> data.put("pageSize", p));

        final XbbResponse<XbbCustomFormListResponse> response = XbbApiCaller.call(CustomForm.LIST, data);
        return response.getResult(XbbCustomFormListResponse.class);
    }

    /**
     * 获取自定义表单数据详情*
     *
     * @param dataId 数据id
     * @return XbbCustomFormDetailResponse 详情
     */
    public static XbbDetailModel get(@NonNull Long dataId) {
        return XbbApiCaller.get(dataId, CustomForm.GET);
    }

    /**
     * 删除自定义表单数据*
     *
     * @param dataId 数据主键id
     * @return 响应
     */
    public static XbbCustomFormDeleteResponse delete(@NonNull Long dataId) {
        JSONObject data = new JSONObject();
        data.put("dataId", dataId);

        final XbbResponse<XbbCustomFormDeleteResponse> response = XbbApiCaller.call(CustomForm.DELETE, data);
        return response.getResult(XbbCustomFormDeleteResponse.class);
    }
}
