package com.hp.xbongbong.api.service.label;

import com.alibaba.fastjson.JSONObject;
import com.hp.xbongbong.api.helper.XbbUrl;
import com.hp.xbongbong.api.helper.enums.XbbBizType;
import com.hp.xbongbong.api.helper.utils.XbbApiCaller;
import com.hp.xbongbong.api.model.XbbResponse;
import com.hp.xbongbong.api.model.label.XbbAddLabelModel;
import com.hp.xbongbong.api.model.label.XbbFormLabelResponse;
import com.hp.xbongbong.api.model.label.XbbRemoveLabelModel;
import lombok.NonNull;

import java.util.Collections;
import java.util.List;
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
    public static List<XbbFormLabelResponse.LabelGroup> formLabels(
            long formId,
            @NonNull XbbBizType businessType,
            boolean enable,
            String name,
            Integer isRelabel
    ) {
        final JSONObject data = new JSONObject();
        data.put("formId", formId);
        data.put("businessType", businessType.getCode());
        data.put("enable", enable ? 1 : 0);
        Optional.ofNullable(name).ifPresent(i -> data.put("name", i));
        Optional.ofNullable(isRelabel).ifPresent(i -> data.put("isRelabel", i));

        final XbbResponse<XbbFormLabelResponse> xbbResponse = XbbApiCaller.call(XbbUrl.Label.LIST, data);
        if (xbbResponse.isNullResult()) {
            return Collections.emptyList();
        }
        return xbbResponse.getResult(XbbFormLabelResponse.class).getLabelTree();

    }

    /**
     * 打标签
     *
     * @param model 参数
     * @return 是否添加成功
     */
    public static boolean add(@NonNull XbbAddLabelModel model) {
        final XbbResponse<Void> xbbResponse = XbbApiCaller.call(XbbUrl.Label.BATCH_ADD, model.json());
        return xbbResponse.succeed();
    }

    /**
     * 移除标签（非删除标签本身）
     *
     * @param model 参数
     * @return 是否成功
     */
    public static boolean remove(@NonNull XbbRemoveLabelModel model) {
        final XbbResponse<Void> xbbResponse = XbbApiCaller.call(XbbUrl.Label.REMOVE, model.json());
        return xbbResponse.succeed();
    }

}
