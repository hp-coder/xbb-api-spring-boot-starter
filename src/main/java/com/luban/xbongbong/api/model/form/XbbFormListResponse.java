package com.luban.xbongbong.api.model.form;

import com.alibaba.fastjson.annotation.JSONField;
import com.luban.xbongbong.api.helper.deserializer.XbbFormBizTypeDeserializer;
import com.luban.xbongbong.api.helper.enums.XbbFormBizType;
import com.luban.xbongbong.api.helper.serializer.XbbFormBizTypeSerializer;
import lombok.Data;

import java.util.List;

/**
 * @author HP 2022/12/30
 */
@Data
public class XbbFormListResponse {
    private List<FormList> formList;

    @Data
    public static class FormList {
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
