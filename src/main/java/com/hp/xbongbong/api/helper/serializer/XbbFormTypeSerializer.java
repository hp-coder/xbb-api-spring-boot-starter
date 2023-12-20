package com.hp.xbongbong.api.helper.serializer;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.hp.xbongbong.api.helper.enums.XbbFormType;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @author HP 2022/12/27
 */
public class XbbFormTypeSerializer implements ObjectSerializer {
    @Override
    public void write(JSONSerializer jsonSerializer, Object o, Object field, Type type, int i) throws IOException {
        final XbbFormType event = (XbbFormType) o;
        jsonSerializer.write(event.getCode());
    }
}