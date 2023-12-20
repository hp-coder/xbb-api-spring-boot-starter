package com.hp.xbongbong.api.helper.deserializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.hp.xbongbong.api.helper.enums.XbbFormType;

import java.lang.reflect.Type;

/**
 * @author HP 2022/12/27
 */
public class XbbFormTypeDeserializer implements ObjectDeserializer {
    @Override
    public XbbFormType deserialze(DefaultJSONParser defaultJSONParser, Type type, Object o) {
        final Integer code = defaultJSONParser.parseObject(Integer.class);
        return XbbFormType.of(code).orElse(null);
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;
    }
}
