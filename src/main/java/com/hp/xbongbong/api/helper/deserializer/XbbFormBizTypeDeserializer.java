package com.hp.xbongbong.api.helper.deserializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.hp.xbongbong.api.helper.enums.XbbFormBizType;

import java.lang.reflect.Type;

/**
 * @author HP 2022/12/27
 */
public class XbbFormBizTypeDeserializer implements ObjectDeserializer {
    @Override
    public XbbFormBizType deserialze(DefaultJSONParser defaultJSONParser, Type type, Object o) {
        final Integer bizType = defaultJSONParser.parseObject(Integer.class);
        return XbbFormBizType.of(bizType).orElse(null);
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;
    }
}
