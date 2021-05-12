package com.ljunggren.csvParser.utils;

public class ParserUtils {

    public static int columnToInt(String column) throws Exception {
        if (!column.matches("[a-zA-Z]+")) {
            throw new Exception(String.format("Column value must be a letter. Actual: %s", column));
        }
        int index = 0;
        for (int i = 0; i < column.length(); i++) {
            index *= 26;
            index += column.toUpperCase().charAt(i) - 'A' + 1;
        }
        return index - 1;
    }
    
}
