package com.luban.xbongbong.api.sdk.label;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.luban.xbongbong.api.helper.config.ConfigConstant;
import com.luban.xbongbong.api.helper.enums.XbbBizType;
import com.luban.xbongbong.api.helper.exception.XbbException;
import com.luban.xbongbong.api.model.XbbResponse;
import com.luban.xbongbong.api.model.label.XbbAddLabelModel;
import com.luban.xbongbong.api.model.label.XbbFormLabelResponse;
import lombok.NonNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author HP 2023/2/2
 */
public class XbbLabelApi {
    private XbbLabelApi() {
        throw new AssertionError();
    }

    /**
     * 获取表单上的标签组
     *
     * @param formId       表单id
     * @param businessType 业务类型
     * @param enable       是否在回收站中
     * @param name         名称模糊查询
     * @param isRelabel    是否展示回收站的标签, 批量移标签需要展示出回收站中的标签 传1
     * @return 表单上的标签组
     */
    public static List<XbbFormLabelResponse.LabelGroup> formLabels(@NonNull long formId, @NonNull XbbBizType businessType, boolean enable, String name, Integer isRelabel) throws Exception {
        //创建请求参数data
        JSONObject data = new JSONObject();
        data.put("formId", formId);
        data.put("businessType", businessType.getCode());
        data.put("enable", enable ? 1 : 0);
        Optional.ofNullable(name).ifPresent(i -> data.put("name", i));
        Optional.ofNullable(isRelabel).ifPresent(i -> data.put("isRelabel", i));
        //调用xbbApi方法，发起API请求
        String response = ConfigConstant.xbbApi(ConfigConstant.LABEL.FORM_LABEL_LIST, data);
        //对返回值进行解析
        XbbResponse<XbbFormLabelResponse> xbbResponse;
        try {
            xbbResponse = JSON.parseObject(response, new TypeReference<XbbResponse<XbbFormLabelResponse>>() {
            });
        } catch (Exception e) {
            throw new Exception("json解析出错", e);
        }
        List<XbbFormLabelResponse.LabelGroup> retArray = null;
        if (Objects.equals(xbbResponse.getCode(), 1)) {
            final XbbFormLabelResponse result = xbbResponse.getResult();
            if (result != null) {
                retArray = result.getLabelTree();
            }
            return retArray;
        } else {
            throw new XbbException(xbbResponse);
        }
    }

    /**
     * 打标签
     *
     * @param model 参数
     * @return 是否添加成功
     * @throws Exception 异常
     */
    public static boolean add(@NonNull XbbAddLabelModel model) throws Exception {
        String response = ConfigConstant.xbbApi(ConfigConstant.LABEL.ADD, model.json());
        //对返回值进行解析
        XbbResponse<?> xbbResponse;
        try {
            xbbResponse = JSON.parseObject(response, new TypeReference<XbbResponse<?>>() {
            });

        } catch (Exception e) {
            throw new Exception("json解析出错", e);
        }
        if (Objects.equals(xbbResponse.getCode(), 1)) {
            return true;
        } else {
            throw new XbbException(xbbResponse);
        }
    }
}
