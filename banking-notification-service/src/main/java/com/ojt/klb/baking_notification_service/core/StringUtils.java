package com.ojt.klb.baking_notification_service.core;

public class StringUtils {
    public static final String SPACE = " ";
    public static final String EMPTY = "";
    public static boolean stringNotNullOrEmpty(String value) {
        if (value == null || EMPTY.equals(value.trim()))
            return false;

        return true;
    }
}
