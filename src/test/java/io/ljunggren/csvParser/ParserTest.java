package io.ljunggren.csvParser;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import org.junit.Test;

public class ParserTest {
    
    private File getTestFile(String path) {
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(classLoader.getResource(path).getFile());
    }

    @Test
    public void parseWithoutFirstRowIsHeaderTest() throws Exception {
        File file = getTestFile("io/ljunggren/csvParser/testWithoutHeaders.csv");
        Parser parser = new Parser();
        List<TestPojo> results = parser.parse(file, TestPojo.class);
        assertEquals(3, results.size());
    }

    @Test
    public void parseWithFirstRowIsHeaderTest() throws Exception {
        File file = getTestFile("io/ljunggren/csvParser/testWithHeaders.csv");
        Parser parser = new Parser().firstRowIsHeader();
        List<TestPojo> results = parser.parse(file, TestPojo.class);
        assertEquals(3, results.size());
    }

}
