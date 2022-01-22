package io.ljunggren.csvParser.mapper;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;

import io.ljunggren.csvParser.annotation.CSVDate;

public class DateMapper extends MapperChain {

    @Override
    public Object map(Field field, String value) throws Exception {
        CSVDate annotation = field.getAnnotation(CSVDate.class);
        if (annotation != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(annotation.pattern());
            return simpleDateFormat.parse(value);
        }
        return nextChain.map(field, value);
    }

}
