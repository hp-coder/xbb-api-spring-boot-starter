package com.luban.xbongbong.api.sdk.paymentsheet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.luban.xbongbong.api.helper.config.ConfigConstant;
import com.luban.xbongbong.api.helper.enums.api.ApiType;
import com.luban.xbongbong.api.helper.enums.paymentsheet.XbbPaymentSheetGroup;
import com.luban.xbongbong.api.model.XbbFormCondition;
import com.luban.xbongbong.api.model.XbbResponse;
import com.luban.xbongbong.api.model.common.detail.XbbDetailModel;
import com.luban.xbongbong.api.model.common.list.XbbListItemModel;
import com.luban.xbongbong.api.model.common.page.XbbPageResponse;
import lombok.NonNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author hp
 */
public class XbbPaymentSheetApi {
    private XbbPaymentSheetApi() {
        throw new AssertionError();
    }

    public static List<XbbListItemModel> list(List<XbbFormCondition> conditions, XbbPaymentSheetGroup group, Integer page, Integer pageSize) throws Exception {
        JSONObject data = new JSONObject();
        Optional.ofNullable(conditions).ifPresent(_0 -> data.put("conditions", _0));
        Optional.ofNullable(group).ifPresent(_0 -> data.put("listGroupId", _0.getCode()));
        Optional.ofNullable(page).ifPresent(_0 -> data.put("page", _0));
        Optional.ofNullable(pageSize).ifPresent(_0 -> data.put("pageSize", _0));
        //调用xbbApi方法，发起API请求
        String response = ConfigConstant.xbbApi(ConfigConstant.PAYMENT_SHEET.LIST, data, ApiType.READ);
        //对返回值进行解析
        XbbResponse<XbbPageResponse> xbbResponse;
        try {
            xbbResponse = JSON.parseObject(response, new TypeReference<>() {
            });
        } catch (Exception e) {
            throw new Exception("json解析出错", e);
        }
        List<XbbListItemModel> retArray = null;
        if (Objects.equals(xbbResponse.getCode(), 1)) {
            final XbbPageResponse result = xbbResponse.getResult();
            if (result != null) {
                retArray = result.getList();
            }
            return retArray;
        } else {
            throw new Exception(xbbResponse.getMsg());
        }
    }

    public static XbbDetailModel get(@NonNull Long dataId) throws Exception {
        return ConfigConstant.get(dataId, ConfigConstant.PAYMENT_SHEET.GET);
    }
}
