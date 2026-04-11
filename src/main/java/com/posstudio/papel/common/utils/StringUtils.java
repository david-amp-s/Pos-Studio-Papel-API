package com.posstudio.papel.common.utils;

public class StringUtils {

    public static String normalize(String value) {
        if (value == null)
            return null;
        return value.trim().toUpperCase();
    }
}
