package com.hp.xbongbong.api.helper.serializer;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

/**
 * @author HP 2023/2/1
 */
public class XbbDateSerializer implements ObjectSerializer {
    @Override
    public void write(JSONSerializer jsonSerializer, Object o, Object o1, Type type, int i) throws IOException {
        final Date date = (Date) o;
        final long l = date.getTime() / 1000;
        jsonSerializer.write(l);
    }
}
