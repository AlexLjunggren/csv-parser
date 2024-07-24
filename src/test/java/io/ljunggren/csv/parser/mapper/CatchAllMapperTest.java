package io.ljunggren.csv.parser.mapper;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;

import org.junit.Test;

import io.ljunggren.csv.parser.mapper.CatchAllMapper;

public class CatchAllMapperTest {
    
    int year;
    Long id;

    @Test
    public void mapPrimitiveTest() throws Exception {
        Field field = this.getClass().getDeclaredField("year");
        Object object = new CatchAllMapper().map(field, "2021");
        assertEquals(2021, object);
    }

    @Test
    public void mapObjectTest() throws Exception {
        Field field = this.getClass().getDeclaredField("id");
        Object object = new CatchAllMapper().map(field, "1");
        assertEquals(1L, object);
    }
    
}
