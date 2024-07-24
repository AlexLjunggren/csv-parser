package io.ljunggren.csv.parser.mapper;

import java.lang.reflect.Field;

import org.apache.commons.lang3.ClassUtils;

public class CatchAllMapper extends MapperChain {

    @Override
    public Object map(Field field, String value) throws Exception {
        if (field.getType().isPrimitive()) {
            return ClassUtils.primitiveToWrapper(field.getType()).getConstructor(String.class).newInstance(value);
        }
        return field.getType().getConstructor(String.class).newInstance(value);
    }

}
