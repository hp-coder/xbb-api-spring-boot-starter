package com.hp.xbongbong.api.model.form;

import com.alibaba.fastjson.annotation.JSONField;
import com.hp.xbongbong.api.helper.deserializer.XbbFormBizTypeDeserializer;
import com.hp.xbongbong.api.helper.enums.XbbFormBizType;
import com.hp.xbongbong.api.helper.serializer.XbbFormBizTypeSerializer;
import com.luban.common.base.model.Response;
import lombok.Data;

import java.util.List;

/**
 * @author HP 2022/12/30
 */
@Data
public class XbbFormListResponse implements Response {
    private List<XbbFormListModel> formList;

    @Data
    public static class XbbFormListModel {
        private Long appId;
        @JSONField(
                serializeUsing = XbbFormBizTypeSerializer.class,
                deserializeUsing = XbbFormBizTypeDeserializer.class
        )
        private XbbFormBizType businessType;
        private Long formId;
        private boolean isProcessForm;
        private Long menuId;
        private String name;
    }
}
