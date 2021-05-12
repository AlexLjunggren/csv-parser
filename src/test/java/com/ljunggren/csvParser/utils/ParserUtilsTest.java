package com.ljunggren.csvParser.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class ParserUtilsTest {

    @Test
    public void columnToIntTest() throws Exception {
        assertEquals(ParserUtils.columnToInt("A"), 0);
        assertEquals(ParserUtils.columnToInt("Z"), 25);
        assertEquals(ParserUtils.columnToInt("AA"), 26);
        assertEquals(ParserUtils.columnToInt("AAA"), 702);
    }
    
    @Test(expected = Exception.class)
    public void clumnToIntExceptionTest() throws Exception {
        ParserUtils.columnToInt("1");
        ParserUtils.columnToInt("");
        ParserUtils.columnToInt(null);
    }

}
