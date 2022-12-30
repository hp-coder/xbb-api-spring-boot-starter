package com.luban.xbongbong.api.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.luban.xbongbong.api.helper.deserializer.XbbFormConditionSymbolDeserializer;
import com.luban.xbongbong.api.helper.enums.XbbFormConditionSymbol;
import com.luban.xbongbong.api.helper.serializer.XbbFormConditionSymbolSerializer;
import lombok.Data;

import java.util.List;

/**
 * @author HP 2022/12/30
 */
@Data
public class XbbFormCondition {
    private String attr;
    @JSONField(
            serializeUsing = XbbFormConditionSymbolSerializer.class,
            deserializeUsing = XbbFormConditionSymbolDeserializer.class
    )
    private XbbFormConditionSymbol symbol;
    private List<?> value;
    private String subAttr;
}
