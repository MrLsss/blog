package com.blog.admin.utils;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author liushuai
 * 日期时间工具
 */
public class DateTimeUtil {

    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final DateTimeFormatter DATETIMESTRING = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /**
     * 获取当前的日期时间
     * yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(DATE_TIME);
    }

    /**
     * 获取当前的日期
     * yyyy-MM-dd
     */
    public static String getCurrentDate() {
        LocalDate now = LocalDate.now();
        return now.format(DATE);
    }

    /**
     * 获取当前时间的字符串
     * @return
     */
    public static String getDateString() {
        return LocalDateTime.now().format(DATETIMESTRING);
    }

}
