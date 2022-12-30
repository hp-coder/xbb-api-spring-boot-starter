package com.luban.xbongbong.api.helper.serializer;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.luban.xbongbong.api.helper.enums.XbbFormBizType;
import com.luban.xbongbong.api.helper.enums.XbbFormConditionSymbol;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @author HP 2022/12/27
 */
public class XbbFormConditionSymbolSerializer implements ObjectSerializer {
    @Override
    public void write(JSONSerializer jsonSerializer, Object o, Object field, Type type, int i) throws IOException {
        final XbbFormConditionSymbol event = (XbbFormConditionSymbol) o;
        jsonSerializer.write(event.getCode());
    }
}
