package com.ljunggren.csvParser.utils;

public class ParserUtils {

    public static int columnToInt(String column) {
        if (!column.matches("[a-zA-Z0-9]+")) {
            throw new RuntimeException(
                    String.format("Column must be alphanumeric. Actual: %s", column));
        }
        if (column.matches("[0-9]+")) {
            return Integer.valueOf(column);
        }
        int index = 0;
        for (int i = 0; i < column.length(); i++) {
            index *= 26;
            index += column.toUpperCase().charAt(i) - 'A' + 1;
        }
        return index - 1;
    }
    
}
