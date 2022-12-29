package com.luban.xbongbong.api.helper.deserializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.luban.xbongbong.api.helper.enums.XbbOperateType;

import java.lang.reflect.Type;

/**
 * @author HP 2022/12/27
 */
public class XbbOperateTypeDeserializer implements ObjectDeserializer {
    @Override
    public XbbOperateType deserialze(DefaultJSONParser defaultJSONParser, Type type, Object o) {
        final String operate = defaultJSONParser.parseObject(String.class);
        return XbbOperateType.of(operate).orElse(null);
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;
    }
}
