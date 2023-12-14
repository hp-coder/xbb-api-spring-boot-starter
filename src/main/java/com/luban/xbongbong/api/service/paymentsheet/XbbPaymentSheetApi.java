package com.luban.xbongbong.api.service.paymentsheet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.luban.xbongbong.api.helper.enums.paymentsheet.XbbPaymentSheetGroup;
import com.luban.xbongbong.api.helper.utils.XbbApiCaller;
import com.luban.xbongbong.api.model.XbbFormCondition;
import com.luban.xbongbong.api.model.XbbResponse;
import com.luban.xbongbong.api.model.common.detail.XbbDetailModel;
import com.luban.xbongbong.api.model.common.list.XbbListItemModel;
import com.luban.xbongbong.api.model.common.page.XbbPageResponse;
import lombok.NonNull;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.luban.xbongbong.api.helper.XbbUrl.PaymentSheet;

/**
 * @author hp
 */
public class XbbPaymentSheetApi {
    private XbbPaymentSheetApi() {
        throw new AssertionError();
    }

    public static List<XbbListItemModel> list(List<XbbFormCondition> conditions, XbbPaymentSheetGroup group, Integer page, Integer pageSize) throws Exception {
        JSONObject data = new JSONObject();
        Optional.ofNullable(conditions).ifPresent(i -> data.put("conditions", i));
        Optional.ofNullable(group).ifPresent(i -> data.put("listGroupId", i.getCode()));
        Optional.ofNullable(page).ifPresent(i -> data.put("page", i));
        Optional.ofNullable(pageSize).ifPresent(i -> data.put("pageSize", i));
        //调用xbbApi方法，发起API请求
        String response = XbbApiCaller.call(PaymentSheet.LIST, data);
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
        return XbbApiCaller.get(dataId, PaymentSheet.GET);
    }
}
