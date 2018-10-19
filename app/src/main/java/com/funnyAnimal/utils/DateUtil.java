package com.funnyAnimal.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public final class DateUtil {

    public static String[] monthStr = {"一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"};

    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        }
    };

    /**
     * 字符串转换为日期
     *
     * @param date
     * @return
     */
    public static Date ParseDate(String date) {
        try {
            return dateFormater.get().parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Date();
    }

    /**
     * 将日期转换为字符串
     *
     * @param date 时间格式，必须符合yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String ParseDateToString(Date date) {
        return dateFormater.get().format(date);
    }

    /**
     * 格式化时间格式为yyyy-MM-dd hh:mm:ss
     *
     * @param date
     * @return
     */
    public static Date formatDate(Date date) {
        return ParseDate(ParseDateToString(date));
    }

    public static String formatDateTime(Date date, String format) {
        return new SimpleDateFormat(format, Locale.CHINA).format(date);
    }

    public static String getWeekOfDay() {
        return formatWeek(Calendar.getInstance(TimeZone.getDefault()).get(Calendar.DAY_OF_WEEK));
    }

    public static String formatWeek(int week) {
        switch (week) {
            case Calendar.MONDAY:
                return "周一";
            case Calendar.TUESDAY:
                return "周二";
            case Calendar.WEDNESDAY:
                return "周三";
            case Calendar.THURSDAY:
                return "周四";
            case Calendar.FRIDAY:
                return "周五";
            case Calendar.SATURDAY:
                return "周六";
            case Calendar.SUNDAY:
                return "周日";
            default:
                return "";
        }
    }

    /**
     * 获取当前时间（小时）
     */
    public static int getHour() {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        return now.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 是否为白天
     *
     * @return
     */
    public static boolean isSun() {
        int currentHour = getHour();
        if (currentHour > 6 && currentHour < 18) {
            return true;
        }
        return false;
    }

    /**
     * 是早上6-9点
     *
     * @return
     */
    public static boolean isMorning() {
        int currentHour = getHour();
        if (currentHour > 6 && currentHour < 9) {
            return true;
        }
        return false;
    }


    //1991-07-23 变为1991/07/23
    public static String getdate(String strDate) {
        return strDate.substring(0, 4) + "/" + strDate.substring(5, 7) + "/" + strDate.substring(8, 10);
    }

    //1991-07-23 变为 7月23日
    public static String getdate2(String strDate) {
        return strDate.substring(5, 7) + "月" + strDate.substring(8, 10) + "日";
    }

    /**
     * 获得一个日期 对应的星期几
     */

    public static String getWeekByDateStr(String strDate) {
        int year = Integer.parseInt(strDate.substring(0, 4));
        int month = Integer.parseInt(strDate.substring(4, 6));
        int day = Integer.parseInt(strDate.substring(6, 8));
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, day);

        int weekIndex = c.get(Calendar.DAY_OF_WEEK);
        return formatWeek(weekIndex);
    }

    public static String getMonthByDateStr(String strDate) {
        int month = Integer.parseInt(strDate.substring(4, 6));
        return monthStr[month - 1] + "月";
    }

    //获得日期的日
    public static String getLastDay(String strDate) {
        return strDate.substring(6, 8);
    }

    // 判断第几周
    public static int judgeWeeks(String strDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date date = sdf.parse(strDate);
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.setMinimalDaysInFirstWeek(7);
        cal.setTime(date);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    //本周开始的时间
    public static String getWeekStart(String str) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date date = sdf.parse(str);
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return sdf.format(cal.getTime());
    }
}
