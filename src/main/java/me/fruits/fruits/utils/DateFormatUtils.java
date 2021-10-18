package me.fruits.fruits.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 统一的时间格式化处理
 */
public class DateFormatUtils {

    public static String format(LocalDateTime dateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(dateTimeFormatter);
    }
}
