package com.luban.xbongbong.api.sdk.customform;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.luban.xbongbong.api.helper.config.ConfigConstant;
import com.luban.xbongbong.api.helper.enums.XbbFormType;
import com.luban.xbongbong.api.helper.enums.api.ApiType;
import com.luban.xbongbong.api.model.XbbFormCondition;
import com.luban.xbongbong.api.model.XbbResponse;
import com.luban.xbongbong.api.model.common.detail.XbbDetailModel;
import com.luban.xbongbong.api.model.customform.XbbCustomFormAlterResponse;
import com.luban.xbongbong.api.model.customform.XbbCustomFormDeleteResponse;
import com.luban.xbongbong.api.model.customform.XbbCustomFormListResponse;
import com.luban.xbongbong.api.model.form.XbbFormListResponse;
import com.luban.xbongbong.api.sdk.form.XbbFormApi;
import lombok.NonNull;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
                throw new RuntimeException("未查询到同名自定义表单： " + formName);
            }
            return formId;
        } catch (Exception e) {
            throw new RuntimeException("获取自定义表单" + formName + "失败", e);
        }
    }

    /**
     * 根据表单名称查询表单id*
     *
     * @param formName 表单名称
     * @return 表单formId
     * @throws Exception 请求异常
     */
    public static Long getCustomFormId(@NonNull String formName) throws Exception {
        final List<XbbFormListResponse.FormList> list = XbbFormApi.list(formName, XbbFormType.CUSTOM, null);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        final Optional<XbbFormListResponse.FormList> optional = list.stream().filter(form -> Objects.equals(formName, form.getName())).findFirst();
        return optional.map(XbbFormListResponse.FormList::getFormId).orElse(null);
    }

    /**
     * 添加自定义表单的数据*
     *
     * @param formId   自定义表单id
     * @param dataList 数据体，需要根据表单字段构建
     * @return 响应
     * @throws Exception 请求异常
     */
    public static XbbCustomFormAlterResponse add(@NonNull Long formId, @NonNull JSONObject dataList) throws Exception {
        JSONObject data = new JSONObject();
        data.put("formId", formId);
        data.put("dataList", dataList);
        String response = ConfigConstant.xbbApi(ConfigConstant.CUSTOM_FORM.ADD, data, ApiType.WRITE);
        return getXbbCustomFormAlterResponse(response);
    }

    /**
     * 修改自定义表单的数据*
     *
     * @param formId   自定义表单id
     * @param dataId   数据id
     * @param dataList 数据体
     * @return 响应
     * @throws Exception 请求异常
     */
    public static XbbCustomFormAlterResponse edit(@NonNull Long formId, @NonNull Long dataId, @NonNull JSONObject dataList) throws Exception {
        JSONObject data = new JSONObject();
        data.put("formId", formId);
        data.put("dataId", dataId);
        data.put("dataList", dataList);
        String response = ConfigConstant.xbbApi(ConfigConstant.CUSTOM_FORM.EDIT, data, ApiType.WRITE);
        return getXbbCustomFormAlterResponse(response);
    }

    private static XbbCustomFormAlterResponse getXbbCustomFormAlterResponse(String response) throws Exception {
        XbbResponse<XbbCustomFormAlterResponse> xbbResponse;
        try {
            xbbResponse = JSON.parseObject(response, new TypeReference<>() {
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
     * 获取自定义表单数据列表*
     *
     * @param formId     表单id
     * @param conditions 查询条件
     * @param page       页码
     * @param pageSize   每页记录数
     * @return XbbCustomFormListResponse 结果集
     * @throws Exception 请求异常
     */
    public static XbbCustomFormListResponse list(@NonNull Long formId, List<XbbFormCondition> conditions, Integer page, Integer pageSize) throws Exception {
        JSONObject data = new JSONObject();
        data.put("formId", formId);
        if (CollUtil.isNotEmpty(conditions)) {
            data.put("conditions", conditions);
        }
        Optional.ofNullable(page).ifPresent(p -> data.put("page", p));
        Optional.ofNullable(pageSize).ifPresent(p -> data.put("pageSize", p));
        String response = ConfigConstant.xbbApi(ConfigConstant.CUSTOM_FORM.LIST, data, ApiType.READ);
        XbbResponse<XbbCustomFormListResponse> xbbResponse;
        try {
            xbbResponse = JSON.parseObject(response, new TypeReference<>() {
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
     * 获取自定义表单数据详情*
     *
     * @param dataId 数据id
     * @return XbbCustomFormDetailResponse 详情
     * @throws Exception 请求异常
     */
    public static XbbDetailModel get(@NonNull Long dataId) throws Exception {
        return ConfigConstant.get(dataId, ConfigConstant.CUSTOM_FORM.GET);
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
        String response = ConfigConstant.xbbApi(ConfigConstant.CUSTOM_FORM.DELETE, data, ApiType.WRITE);
        XbbResponse<XbbCustomFormDeleteResponse> xbbResponse;
        try {
            xbbResponse = JSON.parseObject(response, new TypeReference<>() {
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