package org.dreaght.killwarrant.util;

public class ParseValue {
    public static String parseWithBraces(String configString, String[] variables, Object[] values) {
        String parsedString = configString;
        for (int i = 0; i < variables.length; i++) {
            parsedString = parsedString.replace("%" + variables[i] + "%", String.valueOf(values[i]));
        }
        return parsedString;
    }
}
