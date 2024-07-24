package io.ljunggren.csv.parser.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import io.ljunggren.csv.parser.utils.ParserUtils;

public class ParserUtilsTest {

    @Test
    public void instantiationTest() {
        assertNotNull(new ParserUtils());
    }
    
    @Test
    public void columnToIntTest() {
        assertEquals(ParserUtils.columnToInt("A"), 0);
        assertEquals(ParserUtils.columnToInt("Z"), 25);
        assertEquals(ParserUtils.columnToInt("AA"), 26);
        assertEquals(ParserUtils.columnToInt("AAA"), 702);
    }
    
    @Test
    public void columnNonLetterTest() throws Exception {
        assertEquals(ParserUtils.columnToInt("1"), 1);
        assertEquals(ParserUtils.columnToInt("0"), 0);
    }
    
    @Test(expected = RuntimeException.class)
    public void columnExceptionTest() {
        ParserUtils.columnToInt("*");
    }

}
