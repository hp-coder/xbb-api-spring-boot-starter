package com.hp.xbongbong.api.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.hp.xbongbong.api.helper.enums.XbbFormConditionSymbol;
import com.hp.xbongbong.api.helper.serializer.XbbFormConditionSymbolSerializer;
import com.hp.xbongbong.api.helper.deserializer.XbbFormConditionSymbolDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author HP 2022/12/30
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class XbbFormCondition {
    private String attr;
    private List<?> value;
    private String subAttr;
    @JSONField(
            serializeUsing = XbbFormConditionSymbolSerializer.class,
            deserializeUsing = XbbFormConditionSymbolDeserializer.class
    )
    private XbbFormConditionSymbol symbol;

    public XbbFormCondition(String attr, List<?> values, XbbFormConditionSymbol symbol){
        this.attr = attr;
        this.value = values;
        this.symbol = symbol;
    }
}
