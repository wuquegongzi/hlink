package com.haibao.admin.utils;

import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;


public class DateUtil {
    private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);
    private static final FastDateFormat DATE_FORMAT = createDateFormat("yyyy-MM-dd");
    private static final FastDateFormat DATETIME_FORMAT = createDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final FastDateFormat YYYY_MM_DD_HH_MM_SS_SSS = createDateFormat("yyyyMMddHHmmssSSS");
    private static final FastDateFormat YYYY_MM_DD_HH_MM_SS = createDateFormat("yyyyMMddHHmmss");
    private static final FastDateFormat DATE_HOURS_FORMAT = createDateFormat("HH");
    private static final FastDateFormat YYYY_MM_DD = createDateFormat("yyyyMMdd");

    public DateUtil() {
    }

    public static FastDateFormat createDateFormat(String pattern) {
        return FastDateFormat.getInstance(pattern);
    }

    public static Date parseDate(String source) throws ParseException {
        return DATE_FORMAT.parse(source);
    }

    public static Date parseDateTime(String source) throws ParseException {
        return DATETIME_FORMAT.parse(source);
    }

    public static String yyyyMMddFormat(Date date) {
        return YYYY_MM_DD.format(date);
    }

    public static Date parse(long timestamp) {
        return new Date(timestamp);
    }

    public static Long parseLong(Object obj) {
        if (obj instanceof Long) {
            return Long.valueOf(((Long) obj).longValue());
        } else if (obj instanceof Date) {
            return Long.valueOf(((Date) obj).getTime());
        } else if (null == obj) {
            return null;
        } else {
            String dateStr = obj.toString();

            try {
                return dateStr.contains("-") ? Long.valueOf(parseDateTime(dateStr).getTime()) : Long.valueOf(Long.parseLong(dateStr));
            } catch (Exception var3) {
                logger.error(var3.getMessage(), var3);
                return null;
            }
        }
    }

    public static String formatDate(Date date) {
        return DATE_FORMAT.format(date);
    }

    public static String formatDate(long timestamp) {
        return DATE_FORMAT.format(timestamp);
    }

    public static String formatDateTimeTwo(Date date) {
        return YYYY_MM_DD_HH_MM_SS.format(date);
    }

    public static String formatHour(Date date) {
        return DATE_HOURS_FORMAT.format(date);
    }

    public static String formatDate(String source) throws ParseException {
        return formatDate(parseDateTime(source));
    }

    public static String formatDateTime(Date date) {
        return DATETIME_FORMAT.format(date);
    }

    public static String formatDateTime(long timestamp) {
        return DATETIME_FORMAT.format(timestamp);
    }

    public static String formatDateTime(String source) throws ParseException {
        return formatDateTime(parseDate(source));
    }

    public static int compare(String arg1, String arg2) throws ParseException {
        try {
            return parseDateTime(arg1).compareTo(parseDateTime(arg2));
        } catch (ParseException var3) {
            return parseDate(arg1).compareTo(parseDate(arg2));
        }
    }

    public static String getTimeStampString() {
        return YYYY_MM_DD_HH_MM_SS_SSS.format(System.currentTimeMillis());
    }

    public static Timestamp formatTimestampString(String dateStr) {
        Calendar c = Calendar.getInstance();
        long time = 4133865600000L;
        c.setTimeInMillis(time);
        if (dateStr == null) {
            return new Timestamp(c.getTimeInMillis());
        } else {
            try {
                return new Timestamp(parseDate(dateStr).getTime());
            } catch (Exception var5) {
                return new Timestamp(c.getTimeInMillis());
            }
        }
    }


    //获取分钟是几小时几分钟
    public static String getTimeDay(Long time) {
        int hours = (int) Math.floor(time / 60);
        Long minute = time % 60;
        if (hours > 0) {
            return hours + "小时" + minute + " 分钟";
        } else {
            return minute + " 分钟";
        }
    }

    public static int getDiffMinute(Long time) {
        int hours = (int) Math.floor(time / 60);
        return hours;
    }

    public static void main(String[] args) {
        System.out.println(getTimeDay(1441L));
    }
}