package com.hp.xbongbong.api.model;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.hp.xbongbong.api.helper.deserializer.XbbBizTypeDeserializer;
import com.hp.xbongbong.api.helper.deserializer.XbbFormTypeDeserializer;
import com.hp.xbongbong.api.helper.enums.XbbBizType;
import com.hp.xbongbong.api.helper.enums.XbbFormType;
import com.hp.xbongbong.api.helper.enums.XbbOperateType;
import com.hp.xbongbong.api.helper.serializer.XbbBizTypeSerializer;
import com.hp.xbongbong.api.helper.serializer.XbbFormTypeSerializer;
import com.hp.xbongbong.api.helper.deserializer.XbbOperateTypeDeserializer;
import com.hp.xbongbong.api.helper.serializer.XbbOperateTypeSerializer;
import lombok.Getter;
import lombok.Setter;

/**
 * @author HP 2022/12/27
 */
@Getter
@Setter
public class XbbWebhookPayload {

    /**
     * 对应数据的表单模板id*
     */
    @JSONField(ordinal = 0)
    private Long formId;

    /**
     * 操作类型（说明见操作类型枚举）*
     */
    @JSONField(
            ordinal = 1,
            deserializeUsing = XbbOperateTypeDeserializer.class,
            serializeUsing = XbbOperateTypeSerializer.class
    )
    private XbbOperateType operate;

    /**
     * 公司id*
     */
    @JSONField(ordinal = 2)
    private String corpid;

    /**
     * 业务主键id*
     */
    @JSONField(ordinal = 3)
    private Long dataId;

    /**
     * 回调业务(对于自定义的表单, 这里为空)*
     * <p>
     * 销帮帮文档没写这东西(SHA BI)
     * @Nullable
     */
    @JSONField(
            ordinal = 4,
            deserializeUsing = XbbBizTypeDeserializer.class,
            serializeUsing = XbbBizTypeSerializer.class
    )
    private XbbBizType type;

    /**
     * 1:销帮帮系统模板数据，2:自定义表单数据*
     */
    @JSONField(
            ordinal = 5,
            deserializeUsing = XbbFormTypeDeserializer.class,
            serializeUsing = XbbFormTypeSerializer.class
    )
    private XbbFormType saasMark;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
