package com.revature.project0.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String localDateTimeToString(LocalDateTime dateTime) {
        return dateTime.format(formatter);
    }
}
