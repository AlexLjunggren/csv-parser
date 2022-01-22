package io.ljunggren.csvParser;

import io.ljunggren.csvParser.annotation.CSVColumn;
import lombok.Data;

@Data
public class TestPojo {
    
    @CSVColumn(value = "A")
    private Long id;
    
    @CSVColumn(value = "B")
    private String name;
    
    @CSVColumn(value = "D")
    private String state;
    
}

