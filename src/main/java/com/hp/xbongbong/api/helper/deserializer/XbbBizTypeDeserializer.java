package com.hp.xbongbong.api.helper.deserializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.hp.xbongbong.api.helper.enums.XbbBizType;

import java.lang.reflect.Type;

/**
 * @author HP 2022/12/27
 */
public class XbbBizTypeDeserializer implements ObjectDeserializer {
    @Override
    public XbbBizType deserialze(DefaultJSONParser defaultJSONParser, Type type, Object o) {
        final String bizType = defaultJSONParser.parseObject(String.class);
        return XbbBizType.of(bizType).orElse(null);
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;
    }
}
