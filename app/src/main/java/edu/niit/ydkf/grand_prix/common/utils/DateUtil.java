package edu.niit.ydkf.grand_prix.common.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String DATE_PATTERN_CN = "yyyy年MM月dd日";
    public static final String DATETIME_RSSDATE = "yyyy-MM-dd HH:mm:ss ";
    public static final String DATETIME_RSSPUBDATE = "EEE, dd MMM yyyy hh:mm:ss zzz";

    /**
     * 当前日期
     *
     * @return 系统当前时间
     */
    public static Date getDate() {
        return new Date();
    }

    /**
     * 字符串转换成日期
     *
     * @param str 字符串
     * @return 日期
     */
    public static Date str2Date(String str) {
        if (null == str || "".equals(str)) {
            return null;
        }
        return Timestamp.valueOf(str);
    }

    /**
     * 字符串转换成日期
     *
     * @param str 字符串
     * @return 日期
     */
    public static Date str2YearDate(String str) {
        if (null == str || "".equals(str)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        try {
            return sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    /**
     * 字符串转换成日期
     *
     * @param str     字符串
     * @param pattern 日期格式
     * @return 日期
     */
    public static Date str2Date(String str, String pattern) {
        if (null == str || "".equals(str.trim())) {
            return null;
        }
        if (pattern == null || "".equals(pattern.trim())) {
            pattern = DATETIME_PATTERN;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        try {
            Date date = sdf.parse(str);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 日期转换为字符串
     *
     * @return 字符串
     */
    public static String date2Str() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_PATTERN);
        return sdf.format(new Date());
    }

    /**
     * 日期转换为字符串
     *
     * @param date 日期
     * @return 字符串
     */
    public static String date2Str(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_PATTERN);
        return sdf.format(date);
    }

    public static String date2ShortStr(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
        return sdf.format(date);
    }

    /**
     * 日期转换为字符串，中文年月日
     *
     * @param date 日期
     * @return 字符串
     */
    public static String date2StrCn(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN_CN);
        return sdf.format(date);
    }


    /**
     * 格式化日期,如果异常，则返回def
     *
     * @param date
     * @param format
     * @param def
     * @return
     */
    public static String formatDate(Date date, String format, String def) {
        String ret = def;
        if (date == null) return ret;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            ret = sdf.format(date);
        } catch (Exception e) {
            ret = def;
        }
        ;
        return ret;
    }

    /**
     * 日期转换为字符串
     *
     * @param pattern 日期
     * @return 字符串
     */
    public static String date2Str(String pattern) {
        if (pattern == null || "".equals(pattern.trim())) {
            pattern = DATETIME_PATTERN;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(new Date());
    }

    /**
     * 日期转换为字符串
     *
     * @param date   日期
     * @param pattern 日期格式
     * @return 字符串
     */
    public static String date2Str(Date date, String pattern) {
        if (null == date) {
            return null;
        }
        if (pattern == null || "".equals(pattern.trim())) {
            pattern = DATETIME_PATTERN;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * 日期转换为字符串
     *
     * @param date   日期
     * @param format 日期格式
     * @return 字符串
     */
    public static String date2Str(Date date, String pattern, Locale loc) {
        if (null == date) {
            return null;
        }
        if (pattern == null || "".equals(pattern.trim())) {
            pattern = DATETIME_PATTERN;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, loc);
        return sdf.format(date);
    }

    public static int getCurrentYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    public static String getFormattedDateUtil(Date dtDate, String strFormatTo) {
        if (dtDate == null) {
            return "";
        }
        strFormatTo = strFormatTo.replace('/', '-');
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(strFormatTo);
            return formatter.format(dtDate);
        } catch (Exception e) {
            //Common.printLog("转换日期字符串格式时出错;" + e.getMessage());
            return "";
        }
    }

}
