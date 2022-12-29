package com.luban.xbongbong.api.helper.serializer;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.luban.xbongbong.api.helper.enums.XbbBizType;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @author HP 2022/12/27
 */
public class XbbBizTypeSerializer implements ObjectSerializer {
    @Override
    public void write(JSONSerializer jsonSerializer, Object o, Object field, Type type, int i) throws IOException {
        final XbbBizType event = (XbbBizType) o;
        jsonSerializer.write(event.getCode());
    }
}
