package io.ljunggren.csv.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.reflect.FieldUtils;

import io.ljunggren.csv.parser.annotation.CSVColumn;
import io.ljunggren.csv.parser.mapper.CatchAllMapper;
import io.ljunggren.csv.parser.mapper.DateMapper;
import io.ljunggren.csv.parser.mapper.MapperChain;
import io.ljunggren.csv.parser.utils.ParserUtils;
import lombok.Getter;

@Getter
public class Parser {

    private char delimiter = ',';
    private boolean firstRowIsHeader;
    private Map<String, String> columnMap;
    
    public Parser delimiter(char delimiter) {
        this.delimiter = delimiter;
        return this;
    }
    
    public Parser firstRowIsHeader() {
        this.firstRowIsHeader = true;
        return this;
    }
    
    public Parser columnMap(Map<String, String> columnMap) {
        this.columnMap = columnMap;
        return this;
    }
    
    public List<List<String>> parse(File file) throws Exception {
        Reader reader = getReader(file);
        try {
            CSVParser parser = createCSVParser(reader);
            return parse(parser);
        } finally {
            reader.close();
        }
    }
    
    public <T> List<T> parse(File file, Class<T> clazz) throws Exception {
        Reader reader = getReader(file);
        try {
            CSVParser parser = createCSVParser(reader);
            return generateListFromParser(parser, clazz);
        } finally {
            reader.close();
        }
    }
    
    public List<String> parseHeaders(File file) throws Exception {
        Reader reader = getReader(file);
        try {
            CSVParser parser = createCSVParser(reader);
            return parser.getHeaderNames();
        } finally {
            reader.close();
        }
    }
    
    private Reader getReader(File file) throws Exception {
        return new InputStreamReader(new FileInputStream(file), "UTF-8");
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
    
    private List<List<String>> parse(CSVParser parser) throws Exception {
        List<List<String>> data = new ArrayList<>();
        for (CSVRecord record: parser.getRecords()) {
            List<String> rowData = new ArrayList<>();
            Iterator<String> iterator = record.iterator();
            while(iterator.hasNext()) {
                rowData.add(iterator.next());
            }
            data.add(rowData);
        }
        return data;
    }
    
    private <T> List<T> generateListFromParser(CSVParser parser, Class<T> clazz) throws Exception {
        if (clazz == String.class) {
            return generateFromColumn(parser, clazz, 0);
        }
        List<T> data = new ArrayList<>();
        List<Field> fields = getAnnotatedFields(clazz);
        for (CSVRecord record: parser.getRecords()) {
            T object = clazz.getDeclaredConstructor().newInstance();
            for (Field field: fields) {
                String column = getColumnFromField(field);
                String mappedColumn = getMappedColumn(column);
                Object value = getValueFromRecord(field, record, mappedColumn);
                setValue(field, object, value);
            }
            data.add(object);
        }
        return data;
    }
    
    @SuppressWarnings("unchecked")
    private <T> List<T> generateFromColumn(CSVParser parser, Class<T> clazz, int column) throws IOException {
        List<T> data = new ArrayList<>();
        for (CSVRecord record: parser.getRecords()) {
            data.add((T) record.get(column));
        }
        return data;
    }
    
    private List<Field> getAnnotatedFields(Class<?> clazz) {
        return FieldUtils.getFieldsListWithAnnotation(clazz, CSVColumn.class);
    }
    
    private String getColumnFromField(Field field) throws Exception {
        return field.getAnnotation(CSVColumn.class).value();
    }
    
    private String getMappedColumn(String column) {
        if (columnMap == null) {
            return column;
        }
        String mappedColumn = columnMap.get(column);
        return mappedColumn == null ? column : mappedColumn;
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
