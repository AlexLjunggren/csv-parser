package com.ljunggren.csvParser.mapper;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.ljunggren.csvParser.annotation.CSVDate;

public class DateMapperTest {
    
    @CSVDate(pattern = "MM-dd-yyyy")
    Date date;

    @Test
    public void mapTest() throws Exception {
        Field field = this.getClass().getDeclaredField("date");
        Object obejct = new DateMapper().map(field, "05-04-2021");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime((Date) obejct);
        assertEquals(4, calendar.get(Calendar.DATE));
        assertEquals(2021, calendar.get(Calendar.YEAR));
    }

    @Test(expected = Exception.class)
    public void mapExceptionTest() throws Exception {
        Field field = this.getClass().getDeclaredField("date");
        new DateMapper().map(field, "not a date");
    }

}
