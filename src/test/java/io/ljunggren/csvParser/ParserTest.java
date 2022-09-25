package io.ljunggren.csvParser;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    
    @Test
    public void parseWithHeaderMapping() throws Exception {
        File file = getTestFile("io/ljunggren/csvParser/testWithHeadersOutOfOrder.csv");
        Map<String, String> columnMap = new HashMap<>();
        columnMap.put("A", "E");
        columnMap.put("B", "D");
        columnMap.put("D", "B");
        Parser parser = new Parser().firstRowIsHeader().columnMap(columnMap);
        List<TestPojo> results = parser.parse(file, TestPojo.class);
        assertEquals(3, results.size());
        assertEquals("Alex", results.get(0).getName());
        assertEquals("Indiana", results.get(0).getState());
        assertEquals(Long.valueOf(1), results.get(0).getId());
    }

    @Test
    public void parseWithHeaderMappingWithMissingColumn() throws Exception {
        File file = getTestFile("io/ljunggren/csvParser/testWithHeadersOutOfOrder.csv");
        Map<String, String> columnMap = new HashMap<>();
        columnMap.put("A", "E");
        Parser parser = new Parser().firstRowIsHeader().columnMap(columnMap);
        List<TestPojo> results = parser.parse(file, TestPojo.class);
        assertEquals(3, results.size());
        assertEquals(Long.valueOf(1), results.get(0).getId());
        assertEquals("Alex", results.get(0).getState());
        assertEquals("Indiana", results.get(0).getName());
    }

    @Test
    public void parseWithPipeDelimiterTest() throws Exception {
        File file = getTestFile("io/ljunggren/csvParser/testWithPipeDelimiter.csv");
        Parser parser = new Parser().firstRowIsHeader().delimiter('|');
        List<TestPojo> results = parser.parse(file, TestPojo.class);
        assertEquals(3, results.size());
    }
    
    @Test(expected = Exception.class)
    public void parseWithInvalidColumnTest() throws Exception {
        File file = getTestFile("io/ljunggren/csvParser/testWithInvalidId.csv");
        Parser parser = new Parser().firstRowIsHeader();
        parser.parse(file, TestPojo.class);
    }
    
}
