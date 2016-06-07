package com.kingdee.grab.util;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.Date;

/**
 * 时间转换处理公共方法
 */
public class DateTransformUtil {
    /**
     * 将 unix 的时间戳转换成日期
     * @param unixTimestamp unix的时间戳
     * @return 该时间戳代表的日期
     * @throws ParseException 日期转换错误
     */
    public static Date unixTimestamp2Date(long unixTimestamp) throws ParseException {
        String datePattern = "yyyyMMdd HH:mm:ss";
        String dateStr = DateFormatUtils.format(unixTimestamp * 1000, datePattern); // unix 的时间戳精确到秒，而java的精确到毫秒
        return DateUtils.parseDate(dateStr, datePattern);
    }
}
