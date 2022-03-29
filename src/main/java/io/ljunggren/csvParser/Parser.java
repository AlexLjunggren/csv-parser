package io.ljunggren.csvParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.reflect.FieldUtils;

import io.ljunggren.csvParser.annotation.CSVColumn;
import io.ljunggren.csvParser.mapper.CatchAllMapper;
import io.ljunggren.csvParser.mapper.DateMapper;
import io.ljunggren.csvParser.mapper.MapperChain;
import io.ljunggren.csvParser.utils.ParserUtils;
import lombok.Getter;

@Getter
public class Parser {

    private char delimiter = ',';
    private boolean firstRowIsHeader;
    
    public Parser delimiter(char delimiter) {
        this.delimiter = delimiter;
        return this;
    }
    
    public Parser firstRowIsHeader() {
        this.firstRowIsHeader = true;
        return this;
    }
    
    public <T> List<T> parse(File file, Class<T> clazz) throws Exception {
        Reader reader = new FileReader(file);
        return parse(reader, clazz);
    }
    
    public <T> List<T> parse(Reader reader, Class<T> clazz) throws Exception {
        try {
            CSVParser parser = createCSVParser(reader);
            return generateListFromParser(parser, clazz);
        }
        finally {
            reader.close();
        }
    }
    
    private CSVParser createCSVParser(Reader reader) throws IOException {
        if (firstRowIsHeader) {
            return CSVFormat.DEFAULT.withDelimiter(delimiter)
                    .withFirstRecordAsHeader()
                    .parse(reader);
        }
        return CSVFormat.DEFAULT.withDelimiter(delimiter)
                .parse(reader);
    }
    
    private <T> List<T> generateListFromParser(CSVParser parser, Class<T> clazz) throws Exception {
        List<T> data = new ArrayList<>();
        List<Field> fields = getAnnotatedFields(clazz);
        for (CSVRecord record: parser.getRecords()) {
            T object = clazz.getDeclaredConstructor().newInstance();
            for (Field field: fields) {
                String column = getColumnFromField(field);
                Object value = getValueFromRecord(field, record, column);
                setValue(field, object, value);
            }
            data.add(object);
        }
        return data;
    }
    
    private List<Field> getAnnotatedFields(Class<?> clazz) {
        return FieldUtils.getFieldsListWithAnnotation(clazz, CSVColumn.class);
    }
    
    private String getColumnFromField(Field field) throws Exception {
        return field.getAnnotation(CSVColumn.class).value();
    }
    
    private Object getValueFromRecord(Field field, CSVRecord record, String column) throws Exception {
        int index = ParserUtils.columnToInt(column);
        String value = record.get(index);
        try {
            return getMapperChain().map(field, value);
        } catch (Exception e) {
            throw new Exception(String.format("[Column %s: Row %d] Unable to cast %s to %s", 
                    column, record.getRecordNumber(), value, field.getType().getName()));
        }
    }
    
    private void setValue(Field field, Object object, Object value) throws Exception {
        FieldUtils.writeField(field, object, value, true);
    }
    
    private MapperChain getMapperChain() {
        return new DateMapper()
                .nextChain(new CatchAllMapper()
                );
    }

}
