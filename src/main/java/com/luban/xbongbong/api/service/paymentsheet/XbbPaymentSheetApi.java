package com.luban.xbongbong.api.service.paymentsheet;

import com.alibaba.fastjson.JSONObject;
import com.luban.xbongbong.api.helper.enums.paymentsheet.XbbPaymentSheetGroup;
import com.luban.xbongbong.api.helper.utils.XbbApiCaller;
import com.luban.xbongbong.api.model.XbbFormCondition;
import com.luban.xbongbong.api.model.XbbResponse;
import com.luban.xbongbong.api.model.common.detail.XbbDetailModel;
import com.luban.xbongbong.api.model.common.list.XbbListItemModel;
import com.luban.xbongbong.api.model.common.page.XbbPageResponse;
import lombok.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.luban.xbongbong.api.helper.XbbUrl.PaymentSheet;

/**
 * @author hp
 */
public class XbbPaymentSheetApi {
    private XbbPaymentSheetApi() {
        throw new AssertionError();
    }

    public static List<XbbListItemModel> list(
            List<XbbFormCondition> conditions,
            XbbPaymentSheetGroup group,
            Integer page,
            Integer pageSize
    ) {
        final JSONObject data = new JSONObject();
        Optional.ofNullable(conditions).ifPresent(i -> data.put("conditions", i));
        Optional.ofNullable(group).ifPresent(i -> data.put("listGroupId", i.getCode()));
        Optional.ofNullable(page).ifPresent(i -> data.put("page", i));
        Optional.ofNullable(pageSize).ifPresent(i -> data.put("pageSize", i));

        final XbbResponse<XbbPageResponse> response = XbbApiCaller.call(PaymentSheet.LIST, data);
        final XbbPageResponse result = response.getResult(XbbPageResponse.class);
        if (result != null) {
            return result.getList();
        }
        return Collections.emptyList();
    }

    public static XbbDetailModel get(@NonNull Long dataId) {
        return XbbApiCaller.get(dataId, PaymentSheet.GET);
    }
}
