package com.smtl.edi.util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import org.springframework.util.Assert;

/**
 * 日期时间处理工具类
 *
 * @author martin
 * @since 2010年2月22日23:09:09
 */
public class DatetimeUtil {

    /**
     * 默认构造方法
     */
    private DatetimeUtil() {
    }

    /**
     * 将所有的日期字符串date统一转换成yyyy-MM-dd HH:mm:ss格式
     *
     * @param date
     * @return
     */
    public static String toFullDateFormat(String date) {

        Assert.notNull(date, "日期字符串不能为空");

        //如果是yyyy-MM-dd HH:mm:ss这样的格式，直接返回
        if (date.split("\\-").length == 3 && date.split("\\:").length == 3
                && date.split("\\ ").length == 2 && date.indexOf(" ") == 10) {
            return date;
        } else {
            //否则将可能出现的字符全部替换为空
            date = date.replaceAll("\\-", "").replaceAll(" ", "").replaceAll(":", "");
            //如果替换后不满足yyyyMMddHHmmss这种格式的长度，那么将在后面自动补齐0
            if (date.length() < 14) {
                date = date + String.format("%1$0" + (14 - date.length()) + "d", 0);
            }
            //将上面14位数字用如下的格式组装成yyyy-MM-dd HH:mm:ss
            date = date.substring(0, 4) + "-" 
                    + date.substring(4, 6) + "-" 
                    + date.substring(6, 8) + " "
                    + date.substring(8, 10) + ":" 
                    + date.substring(10, 12) + ":" 
                    + date.substring(12, 14);

        }

        return date;

    }

    /**
     * 从begin到现在的天数
     *
     * @param begin
     * @return
     */
    public static int daysToNow(String begin) {

        LocalDate to = LocalDate.now();
        LocalDate from = LocalDate.parse(toFullDateFormat(begin).split("\\ ")[0]);

        return (int) ChronoUnit.DAYS.between(from, to);

    }

    /**
     * 返回年
     *
     * @param calendar
     * @return
     */
    public static int year(Calendar calendar) {
        if (calendar != null) {
            return calendar.get(Calendar.YEAR);
        } else {
            return year();
        }
    }

    /**
     * 返回年
     *
     * @return
     */
    public static int year() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /**
     * 返回月份
     *
     * @param calendar
     * @return
     */
    public static int month(Calendar calendar) {
        if (calendar != null) {
            return calendar.get(Calendar.MONTH) + 1;
        } else {
            return month();
        }
    }

    /**
     * 返回月份
     *
     * @return
     */
    public static int month() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    /**
     * 返回日期
     *
     * @param calendar
     * @return
     */
    public static int date(Calendar calendar) {
        if (calendar != null) {
            return calendar.get(Calendar.DATE);
        } else {
            return date();
        }
    }

    /**
     * 返回日期
     *
     * @return
     */
    public static int date() {
        return Calendar.getInstance().get(Calendar.DATE);
    }

    /**
     * 返回星期
     *
     * @param calendar
     * @return
     */
    public static int dayOfWeek(Calendar calendar) {
        if (calendar != null) {
            return calendar.get(Calendar.DAY_OF_WEEK);
        } else {
            return dayOfWeek();
        }
    }

    /**
     * 返回星期
     *
     * @return
     */
    public static int dayOfWeek() {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 返回小时
     *
     * @param calendar
     * @return
     */
    public static int hour(Calendar calendar) {
        if (calendar != null) {
            return calendar.get(Calendar.HOUR);
        } else {
            return hour();
        }
    }

    /**
     * 返回小时
     *
     * @return
     */
    public static int hour() {
        return Calendar.getInstance().get(Calendar.HOUR);
    }

    /**
     * 返回分钟
     *
     * @param calendar
     * @return
     */
    public static int minute(Calendar calendar) {
        if (calendar != null) {
            return calendar.get(Calendar.MINUTE);
        } else {
            return minute();
        }
    }

    /**
     * 返回分钟
     *
     * @return
     */
    public static int minute() {
        return Calendar.getInstance().get(Calendar.MINUTE);
    }

    /**
     * 返回秒
     *
     * @param calendar
     * @return
     */
    public static int second(Calendar calendar) {
        if (calendar != null) {
            return calendar.get(Calendar.SECOND);
        } else {
            return second();
        }
    }

    /**
     * 返回秒
     *
     * @return
     */
    public static int second() {
        return Calendar.getInstance().get(Calendar.SECOND);
    }

    /**
     * 返回推前days天的Calendar对象
     *
     * @param days
     * @return
     */
    public static Calendar daysAgo(double days) {
        return daysAgo(Calendar.getInstance(), days);
    }

    /**
     * 返回经过days天后的Calendar对象
     *
     * @param days
     * @return
     */
    public static Calendar daysAfter(double days) {
        return daysAfter(Calendar.getInstance(), days);
    }

    /**
     * 返回指定的calendar对象，推前days天的Calendar对象
     *
     * @param calendar
     * @param days
     * @return
     */
    public static Calendar daysAgo(Calendar calendar, double days) {
        // 偏移量，给定n天的毫秒数

        long offset = BigDecimal.valueOf(days).multiply(BigDecimal.valueOf(24 * 60 * 60 * 1000d)).longValue();

        if (calendar == null) {
            calendar = Calendar.getInstance();
        }

        calendar.setTimeInMillis(calendar.getTimeInMillis() - offset);
        return calendar;
    }

    /**
     * 返回指定的calendar日期时间字符串，推前days天的Calendar对象
     *
     * @param calendar
     * @param days
     * @return
     */
    public static Calendar daysAgo(String calendar, double days) {
        // 偏移量，给定n天的毫秒数
        long offset = BigDecimal.valueOf(days).multiply(BigDecimal.valueOf(24 * 60 * 60 * 1000d)).longValue();

        Calendar c = toCalendar(calendar);
        c.setTimeInMillis(c.getTimeInMillis() - offset);

        return c;
    }

    /**
     *
     * 返回指定的Date对象，推前半小时的Calendar对象
     *
     * @param date
     * @return
     */
    public static Calendar halfAnHourAgo(Date date) {
        return halfAnHourAgo(toCalendar(date));
    }

    /**
     *
     * 返回指定的date日期时间字符串，推前半小时的Calendar对象
     *
     * @param date
     * @return
     */
    public static Calendar halfAnHourAgo(String date) {
        return halfAnHourAgo(toCalendar(date));
    }

    /**
     *
     * 返回指定的Date对象，经过半小时后的Calendar对象
     *
     * @param date
     * @return
     */
    public static Calendar halfAnHourAfter(Date date) {
        return halfAnHourAfter(toCalendar(date));
    }

    /**
     *
     * 返回指定的date日期时间字符串，经过半小时后的Calendar对象
     *
     * @param date
     * @return
     */
    public static Calendar halfAnHourAfter(String date) {
        return halfAnHourAfter(toCalendar(date));
    }

    /**
     *
     * 返回指定的Calendar对象，推前半小时的Calendar对象
     *
     * @param calendar
     * @return
     */
    public static Calendar halfAnHourAgo(Calendar calendar) {
        // 偏移量，给定n天的毫秒数
        long offset = Double.valueOf(24 * 60 * 60 * 1000 * (1 / 48d)).longValue();

        if (calendar == null) {
            calendar = Calendar.getInstance();
        }

        calendar.setTimeInMillis(calendar.getTimeInMillis() - offset);
        return calendar;
    }

    /**
     *
     * 返回指定的Calendar对象，经过半小时后的Calendar对象
     *
     * @param calendar
     * @return
     */
    public static Calendar halfAnHourAfter(Calendar calendar) {
        // 偏移量，给定n天的毫秒数
        long offset = Double.valueOf(24 * 60 * 60 * 1000 * (1 / 48d)).longValue();

        if (calendar == null) {
            calendar = Calendar.getInstance();
        }

        calendar.setTimeInMillis(calendar.getTimeInMillis() + offset);
        return calendar;
    }

    /**
     * 返回指定的Calendar对象，经过days天后的Calendar对象
     *
     * @param calendar
     * @param days
     * @return
     */
    public static Calendar daysAfter(Calendar calendar, double days) {
        // 偏移量，给定n天的毫秒数
        long offset = BigDecimal.valueOf(days).multiply(BigDecimal.valueOf(24 * 60 * 60 * 1000d)).longValue();

        if (calendar == null) {
            calendar = Calendar.getInstance();
        }

        calendar.setTimeInMillis(calendar.getTimeInMillis() + offset);
        return calendar;
    }

    /**
     * 返回指定calendar日期时间字符串，经过days天后的Calendar对象
     *
     * @param calendar
     * @param days
     * @return
     */
    public static Calendar daysAfter(String calendar, double days) {
        // 偏移量，给定n天的毫秒数
        long offset = BigDecimal.valueOf(days).multiply(BigDecimal.valueOf(24 * 60 * 60 * 1000d)).longValue();

        Calendar c = toCalendar(calendar);
        c.setTimeInMillis(c.getTimeInMillis() + offset);

        return c;
    }

    /**
     * 返回指定的calendar对象，推前一天的Calendar对象
     *
     * @param calendar
     * @return
     */
    public static Calendar yesterday(Calendar calendar) {
        long offset = 1 * 24 * 60 * 60 * 1000;

        if (calendar == null) {
            calendar = Calendar.getInstance();
        }

        calendar.setTimeInMillis(calendar.getTimeInMillis() - offset);
        return calendar;
    }

    /**
     * 返回昨天的Calendar对象
     *
     * @return
     */
    public static Calendar yesterday() {
        return DatetimeUtil.yesterday(Calendar.getInstance());
    }

    /**
     * 返回指定的calendar对象，下一天的Calendar对象
     *
     * @param calendar
     * @return
     */
    public static Calendar tomorrow(Calendar calendar) {
        long offset = 1 * 24 * 60 * 60 * 1000;

        if (calendar == null) {
            calendar = Calendar.getInstance();
        }

        calendar.setTimeInMillis(calendar.getTimeInMillis() + offset);
        return calendar;
    }

    /**
     * 返回下一天的Calendar对象
     *
     * @return
     */
    public static Calendar tomorrow() {
        return DatetimeUtil.tomorrow(Calendar.getInstance());
    }

    /**
     * 返回指定的calendar对象经过偏移offset毫秒后的Calendar对象
     *
     * @param calendar
     * @param offset
     * @return
     */
    public static Calendar offset(Calendar calendar, long offset) {

        if (calendar == null) {
            calendar = Calendar.getInstance();
        }

        calendar.setTimeInMillis(calendar.getTimeInMillis() + offset);
        return calendar;
    }

    /**
     * 返回指定的Calendar对象，按照pattern格式化后的日期字符串
     *
     * @param calendar
     * @param pattern
     * @return
     */
    public static String format(Calendar calendar, String... pattern) {

        String fmt;

        if (calendar == null) {
            calendar = Calendar.getInstance();
        }

        if (pattern == null || pattern.length == 0) {
            fmt = YYYY_MM_DD_HH_MM_SS;
        } else {
            fmt = pattern[0];
        }

        SimpleDateFormat sdf = new SimpleDateFormat(fmt);

        return sdf.format(calendar.getTime());
    }

    /**
     * 返回指定的Date对象，按照pattern格式化后的日期字符串
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date, String... pattern) {
        return format(toCalendar(date), pattern);
    }

    /**
     * 返回指定的date日期时间字符串，按照pattern格式化后的日期字符串
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String format(String date, String... pattern) {
        return format(toCalendar(date), pattern);
    }

    /**
     * 将Date类型转换成Calendar类型
     *
     * @param date
     * @return
     */
    public static Calendar toCalendar(Date date) {
        if (date == null) {
            return Calendar.getInstance();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c;
    }

    /**
     * 将Calendar类型转换到Date类型
     *
     * @param calendar
     * @return
     */
    public static Date toDate(Calendar calendar) {
        if (calendar == null) {
            return Calendar.getInstance().getTime();
        }
        return calendar.getTime();
    }

    /**
     * 返回指定的date日期时间字符串，经过fmt格式化后的Date类型
     *
     * @param date
     * @param fmt
     * @return
     */
    public static Date toDate(String date, String... fmt) {
        try {
            if (null == fmt || fmt.length == 0) {
                fmt = new String[1];
                fmt[0] = YYYY_MM_DD_HH_MM_SS;
            }
            date = toFullDateFormat(date);
            SimpleDateFormat sdf = new SimpleDateFormat(fmt[0]);
            return sdf.parse(date);
        } catch (ParseException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 将日期时间字符串转换成Date类型
     *
     * @param date
     * @return
     */
    public static Date toDate(String date) {
        return toDate(date, DatetimeUtil.YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 将Date类型转换成Timestamp类型
     *
     * @param date
     * @return
     */
    public static Timestamp toTimestamp(Date date) {
        return new Timestamp(date.getTime());
    }

    /**
     * 将Calendar类型转换成Timestamp类型
     *
     * @param calendar
     * @return
     */
    public static Timestamp toTimestamp(Calendar calendar) {
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * 将Timestamp类型转换成Calendar类型
     *
     * @param timestamp
     * @return
     */
    public static Calendar toCalendar(Timestamp timestamp) {
        Calendar c = Calendar.getInstance();
        c.setTime(timestamp);
        return c;
    }

    /**
     * 返回当前时间按照format格式化后的字符串
     *
     * @param format
     * @return
     */
    public static String now(String format) {
        return format(Calendar.getInstance(), format);
    }

    /**
     * 返回当前时间字符串YYYY_MM_DD_HH_MM_SS
     *
     * @return
     */
    public static String now() {
        return format(Calendar.getInstance(), YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 将日期时间字符串转换成Calendar类型
     *
     * @param date
     * @return
     */
    public static Calendar toCalendar(String date) {
        return DatetimeUtil.toCalendar(toDate(date));
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(DatetimeUtil.format(DatetimeUtil.daysAgo(Calendar.getInstance(), 1.5), YYYY_MM_DD_HH_MM_SS));
        System.out.println(DatetimeUtil.format(DatetimeUtil.daysAfter(Calendar.getInstance(), 1.5), YYYY_MM_DD_HH_MM_SS));

        System.out.println(DatetimeUtil.format(DatetimeUtil.daysAfter("20201103", 1), YYYY_MM_DD));
        System.out.println(DatetimeUtil.format(DatetimeUtil.daysAgo("20201103", 1), YYYY_MM_DD));

        System.out.println(DatetimeUtil.format(DatetimeUtil.daysAfter("2020-01-01 01:01:01", 1), YYYY_MM_DD));
        System.out.println(DatetimeUtil.format(DatetimeUtil.daysAgo("2020-01-01 01:01:01", 1), YYYY_MM_DD));

        System.out.println(DatetimeUtil.format(DatetimeUtil.tomorrow(), YYYYMMDDHHMMSS));
        System.out.println(DatetimeUtil.format(DatetimeUtil.toCalendar("2020-01-01 01:00")));

        System.out.println(DatetimeUtil.daysToNow("2020-01-01 01:01"));

        System.out.println(DatetimeUtil.format(DatetimeUtil.halfAnHourAgo("20200101"), YYYYMMDD_HHMM));
        System.out.println(DatetimeUtil.format(DatetimeUtil.halfAnHourAgo("2020-01-01  01:01:01"), YYYYMMDD_HHMM));

        System.out.println(DatetimeUtil.now());

        System.out.println("java补空格:" + String.format("%1$-5s", "a"));
        System.out.println(DatetimeUtil.format(DatetimeUtil.yesterday(), YYYYMMDD));
        System.out.println(DatetimeUtil.format(DatetimeUtil.toDate("20200101", new String[0]), YYYY_MM_DD_HH_MM_SS));
        System.out.println(DatetimeUtil.format(DatetimeUtil.toDate("20200101"), YYYYMMDDHHMMSS));
        System.out.println(DatetimeUtil.format("20200101002311", YYYY_MM_DD_HH_MM_SS));
        
        System.out.println(DatetimeUtil.daysToNow("20200101002311"));

    }
    public static final String HHMMMSS = "HHmmss";
    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YYMMDDHHMM = "yyMMddHHmm";
    public static final String YYYYMMDDHHMM = "yyyyMMddHHmm";
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYYMMDD_HHMM = "yyyyMMdd:HHmm";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
}
