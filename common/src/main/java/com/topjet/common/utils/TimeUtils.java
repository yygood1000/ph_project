package com.topjet.common.utils;

import android.text.TextUtils;

import com.topjet.common.config.CPersisData;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * 时间转换工具
 */
public class TimeUtils {
    public static final String FC_HANDLER_TIME_PATTERN = "yyyy-MM-dd HH_mm_ss";
    public static final String COMMON_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String COMMON_TIME_PATTERN2 = "yyyy-MM-dd HH:mm";
    public static final String WEATHER_TIME_PATTERN = "yyyyMMddHHmm";
    public static final String YEAR_MONTH_DATE_TIME_PATTERN = "yyyy-MM-dd";
    public static final String YEAR_MONTH_DATE_TIME_PATTERN2 = "yyyy年MM月dd日";
    public static final String MINUTE_SECOND_PATTERN = "mm分ss秒";
    public static final String NOTIFICATION_PATTERN = "HH:mm";
    public static final String MESSAGE_CENTER_LISTITEM_PATTERN2 = "MM-dd";
    public static final String REFUND_DETAIL_TIME_PATTERN = "yyyy/MMdd HH:mm";
    public static final String ORDER_LIST_TIME_PATTERN = "MM-dd HH 点";
    public static final String OFTEN_GOODS_LIST_TIME_PATTERN = "MM月dd日 HH:mm";
    public static final String YEAR_MONTH_DATE_HOURS_TIME_PATTERN2 = "yyyy年MM月dd日HH:mm";
    public static final String YEAR_MONTH_DATE_HOURS_SECOND_TIME_PATTERN = "yyyy年MM月dd日 HH:mm:ss";
    public static final String YEAR_MONTH_DATE_HOURS_MINTE_MESSAGES_PATTERN = "yyyy/MM/dd HH:mm";

    public static final String DAY_HOURS = "MM-dd HH:mm";

    /**
     * 通过Data对象获取所对应的毫秒值
     */
    public static long getMillis(Date date) {
        return date.getTime();
    }

    /**
     * 通过毫秒值获取所对应的Data对象
     */
    public static Date getDate(long millis) {
        return new Date(millis);
    }

    /**
     * 初始化SimpleDateFormat对象
     */
    private static SimpleDateFormat getSimpleDateFormat(String pattern) {
        return new SimpleDateFormat(pattern, Locale.CHINA);
    }

    /**
     * 格式化Data对象
     */
    public static String getFormatDate(Date date, String pattern) {
        return getSimpleDateFormat(pattern).format(date);
    }

    /**
     * 显示更新时间
     * 例:yyyy-MM-dd HH:mm
     */
    public static String showUpdateTime(String updateTime) {
        if (TextUtils.isEmpty(updateTime)) {
            return "";
        }
        long longPublishTime = NumberFormatUtils.strToLong(updateTime, 0L);
        return TimeUtils.getFormatDate(longPublishTime, TimeUtils.COMMON_TIME_PATTERN2);
    }

    /**
     * 显示无年的更新时间
     * @param updateTime
     * @return MM-dd HH:mm
     */
    public static String showUpdateTimeDay(String updateTime) {
        if (TextUtils.isEmpty(updateTime)) {
            return "";
        }
        long longPublishTime = NumberFormatUtils.strToLong(updateTime, 0L);
        return TimeUtils.getFormatDate(longPublishTime, TimeUtils.DAY_HOURS);
    }

    /**
     * yyyy年MM月dd日HH:mm
     * @param updateTime
     * @return
     */
    public static String showTimeDayHours(String updateTime) {
        if (TextUtils.isEmpty(updateTime)) {
            return "";
        }
        long longPublishTime = NumberFormatUtils.strToLong(updateTime, 0L);
        return TimeUtils.getFormatDate(longPublishTime, TimeUtils.YEAR_MONTH_DATE_HOURS_TIME_PATTERN2);
    }

    /**
     * yyyy/MM/dd HH:mm
     *
     * @param updateTime
     * @return
     */
    public static String showTimeMessage(String updateTime) {
        if (TextUtils.isEmpty(updateTime)) {
            return "";
        }
        long longPublishTime = NumberFormatUtils.strToLong(updateTime, 0L);
        return TimeUtils.getFormatDate(longPublishTime, TimeUtils.YEAR_MONTH_DATE_HOURS_MINTE_MESSAGES_PATTERN);
    }

    /**
     * yyyy年MM月dd日 HH:mm:ss
     *
     * @param updateTime
     * @return
     */
    public static String showTimeDaySecond(String updateTime) {
        if (TextUtils.isEmpty(updateTime)) {
            return "";
        }
        long longPublishTime = NumberFormatUtils.strToLong(updateTime, 0L);
        return TimeUtils.getFormatDate(longPublishTime, TimeUtils.YEAR_MONTH_DATE_HOURS_SECOND_TIME_PATTERN);
    }

    /**
     * 格式化毫秒值
     */
    public static String getFormatDate(long millis, String pattern) {
        return getFormatDate(getDate(millis), pattern);
    }

    /**
     * 通过Data对象获取Calendar日历操作对象
     */
    public static Calendar getCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * 通过毫秒值获取Calendar日历操作对象
     */
    public static Calendar getCalendar(long millis) {
        return getCalendar(getDate(millis));
    }

    /**
     * 通过格式化时间，获取所对应的毫秒值
     *
     * @param time       格式化后的时间
     * @param pattern    格式化的格式
     * @param needCreate 是否返回当前时间作为默认返回
     */
    public static long getMillis(String time, String pattern, boolean needCreate) {
        Date date;
        try {
            date = getSimpleDateFormat(pattern).parse(time);
        } catch (ParseException e) {
            date = needCreate ? new Date() : null;
        }
        return getMillis(date);
    }

    /**
     * 秒 - 毫秒
     */
    public static long secondsToMills(long seconds) {
        return TimeUnit.SECONDS.toMillis(seconds);
    }

    /**
     * 分钟 - 毫秒
     */
    public static long minutesToMills(long minutes) {
        return TimeUnit.MINUTES.toMillis(minutes);
    }

    /**
     * 小时- 毫秒
     */
    public static long hoursToMills(long hours) {
        return TimeUnit.HOURS.toMillis(hours);
    }

    /**
     * 天数 - 毫秒
     */
    public static long daysToMills(long days) {
        return TimeUnit.DAYS.toMillis(days);
    }

    public static int getYear(Date date) {
        return getCalendar(date).get(Calendar.YEAR);
    }

    public static int getMonth(Date date) {
        return getCalendar(date).get(Calendar.MONTH);
    }

    public static int getDayOfMonth(Date date) {
        return getCalendar(date).get(Calendar.DAY_OF_MONTH);
    }

    public static int getDayOfWeek(Date date) {
        return getCalendar(date).get(Calendar.DAY_OF_WEEK);
    }

    public static int getDayOfYear(Date date) {
        return getCalendar(date).get(Calendar.DAY_OF_YEAR);
    }

    public static int getHour(Date date) {
        return getCalendar(date).get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinute(Date date) {
        return getCalendar(date).get(Calendar.MINUTE);
    }

    public static int getSecond(Date date) {
        return getCalendar(date).get(Calendar.SECOND);
    }

    public static int getYear(long millis) {
        return getCalendar(millis).get(Calendar.YEAR);
    }

    public static int getMonth(long millis) {
        return getCalendar(millis).get(Calendar.MONTH);
    }

    public static int getDayOfMonth(long millis) {
        return getCalendar(millis).get(Calendar.DAY_OF_MONTH);
    }

    public static int getDayOfWeek(long millis) {
        return getCalendar(millis).get(Calendar.DAY_OF_WEEK);
    }

    public static int getDayOfYear(long millis) {
        return getCalendar(millis).get(Calendar.DAY_OF_YEAR);
    }

    public static int getHour(long millis) {
        return getCalendar(millis).get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinute(long millis) {
        return getCalendar(millis).get(Calendar.MINUTE);
    }

    public static int getSecond(long millis) {
        return getCalendar(millis).get(Calendar.SECOND);
    }

    /**
     * 以友好的方式显示时间(车源列表显示)
     */
    public static String getFriendlyTime(long millis) {
        String ftime = "";
        Date time = getDate(millis);// 使用毫秒值获得Data对象
        if (time == null) {
            return "Unknown";
        }
        long lt = time.getTime() / 86400000;// 传入时间转化为天数

        // 获取系统时间
        Calendar cal = Calendar.getInstance();
        long ct = cal.getTimeInMillis() / 86400000;// 系统时间转化为天数

        int days = (int) (ct - lt);
        Logger.i("oye", "服务端返回时间 " + millis + "               系統时间 " + cal.getTimeInMillis());
        // 判断是否是同一天
        if (days == 0) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0) // 判断是否是一个小时内
                ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
            else
                ftime = hour + "小时" + (Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) - (hour * 60))
                        + "分钟前";
        } else {
            ftime = getFormatDate(millis, "MM-dd HH:mm");
        }
        return ftime;
    }

    /**
     * 以友好的方式显示时间(订单列表显示)
     */
    public static String getFriendlyTimeUsedInOrderList(long millis) {
        // 方法 1
        String fMonthAndDay = "";
        String fHours = "";
        // 获取系统时间
        Calendar cal = Calendar.getInstance();
        long timeNow = cal.getTimeInMillis();
        fMonthAndDay = getFormatDate(millis, "MM-dd");// 格式化为01-01
        fHours = getFormatDate(millis, "HH");// 格式化为24小时

        int fHoursInt = Integer.parseInt(fHours);
        if (fHoursInt <= 12) {// 小于12点 包括0点
            return fMonthAndDay + " " + "上午" + " " + fHoursInt + "点";// 01-01上午11点（含0点）
        } else {// 大于12点
            return fMonthAndDay + " " + "下午" + " " + fHoursInt + "点";// 01-01下午13点（含12点）
        }
    }


    /**
     * 常发货源页面，显示的发货时间
     * <p>
     * 例：
     * 1.上午 12:23
     * 2.下午 14:25
     * 3.7月2日 12:22
     */
    public static String getDateFormatOnOftenGoods(long millis) {
        String fHours;
        fHours = getFormatDate(millis, "HH");// 格式化为24小时

        int fHoursInt = Integer.parseInt(fHours);
        if (fHoursInt < 12) {// 小于12点 包括0点
            return "上午" + " " + getFormatDate(millis, "HH:mm");
        } else if (fHoursInt >= 12 && fHoursInt < 24) {// 大于12点 小于24点
            return "下午" + " " + getFormatDate(millis, "HH:mm");
        } else {
            return getFormatDate(millis, OFTEN_GOODS_LIST_TIME_PATTERN);//7月2日 12:22
        }
    }


    /**
     * 时间戳 毫秒 转成 2016年05月28日 上午 06时
     * 220  2016年05月28日 06时
     */
    public static String parseLong(long l) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String s = "";
        String s2 = "";
        Date date = new Date(l);
        Logger.i("=====转换的日期1=====", "" + date.toString());
        s = sdf.format(date);
        Logger.i("=====转换的日期2=====", "" + s);
        s2 = s.substring(0, s.indexOf(" "));
        System.err.println(s.substring(s.indexOf(" ") + 1, s.indexOf(" ") + 3));
        String s3 = s.substring(s.indexOf(" ") + 1, s.indexOf(" ") + 3);
        s2 = s2 + " " + s3 + "时";
        return s2;
    }


    /**
     * 时间戳 毫秒 转成 2016年05月28日
     */
    public static String parseLongFormat(long l) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        String s = "";
        Date date = new Date(l);
        s = sdf.format(date);
        return s;
    }

    /**
     * 当前日期加减若干天 后的日期
     *
     * @param m：天数
     * @param format：格式 如：yyyy年MM月dd日 ，yyyy-MM-dd
     */
    public static String GetDateAdd(int m, String format) {
        Date date = new Date();
        SimpleDateFormat h = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String n = h.format(date);
        Timestamp time = Timestamp.valueOf(n);
        // 在天数上加（减）天数
        long l = time.getTime() + 24 * 60 * 60 * m * 1000;
        time.setTime(l);
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        // 获取当前时间并格式化之
        return sdf.format(time);
    }

    /**
     * 判断是否是 当天
     */
    public static boolean isSameDay(long time1, long time2) {
        if (0 == time1 || 0 == time2) {
            return false;// 传0 ，表示sp中没有值，不是当天
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String d1 = format.format(time1);
        String d2 = format.format(time2);
        Date date1, date2;
        try {
            date1 = format.parse(d1);
            date2 = format.parse(d2);
            Calendar nowCalendar = Calendar.getInstance();

            nowCalendar.setTime(date1);

            Calendar dateCalendar = Calendar.getInstance();

            dateCalendar.setTime(date2);

            if (nowCalendar.get(Calendar.YEAR) == dateCalendar.get(Calendar.YEAR)

                    && nowCalendar.get(Calendar.MONTH) == dateCalendar.get(Calendar.MONTH)

                    && nowCalendar.get(Calendar.DATE) == dateCalendar.get(Calendar.DATE)) {

                return true;

            }
        } catch (ParseException e1) {
            e1.printStackTrace();
        }

        return false;

    }

    /**
     * 时间显示
     *
     * @param strLongTime:时间戳
     * @return 59分钟前/1小时前/1天前
     */
    public static String displayTime(String strLongTime) {
        String retStr = "";
        if (StringUtils.isBlank(strLongTime)) {
            return retStr;
        }
        try {
            Date d1 = new Date();// 获取当前时间
            Date d2 = getDate(Long.parseLong(strLongTime));

            long diff = d1.getTime() - d2.getTime();
            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
            long x = (days * 24 + hours);

            if (days >= 1) {
                retStr = "" + days + "天前";
                if (days > 365) {
                    retStr = "1年前";
                }
            } else {
                if (x != 0) {
                    retStr = "" + x + "小时前";
                } else {
                    if (minutes == 0) {
                        //retStr = "1分钟前";
                        retStr = "刚刚";
                    } else {
                        retStr = "" + minutes + "分钟前";
                    }
                }
            }
        } catch (Exception e) {
            retStr = strLongTime;
        }

        return retStr;
    }

    /**
     * 两个时间相减
     *
     * @param startTime:yyyy-MM-dd HH:mm:ss
     * @param endTime:yyyy-MM-dd   HH:mm:ss
     * @return 返回间隔的秒数
     */
    public static long calculationDate(String startTime, String endTime) {
        SimpleDateFormat d = new SimpleDateFormat(COMMON_TIME_PATTERN);//格式化时间
        long result = 0;//结果
        try {
            //除以1000得到秒，相应的60000得到分，3600000得到小时
            result = (d.parse(endTime).getTime() - d.parse(startTime).getTime()) / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 判断是否在活动时间内
     * @param starTime 开始时间
     * @param endTime 结束时间
     * @return 是否满足活动时间
     */
    public static boolean isActionTime(String starTime, String endTime){

        // 服务器时间
        long severDate = CPersisData.getServersDate();
        // 活动开始时间
        long starDate = StringUtils.getLongToString(starTime);
        // 活动结束时间
        long endDate = StringUtils.getLongToString(endTime);

        Logger.d("活动时间 "+severDate+" "+starDate+" "+endDate+" "+" "+(severDate >= starDate)+" "+(severDate < endDate));
        // 大于等于开始时间，小于结束时间就显示活动图标
        return (severDate >= starDate) && (severDate < endDate);
    }

}
