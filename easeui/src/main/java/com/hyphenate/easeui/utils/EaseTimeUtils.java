package com.hyphenate.easeui.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class EaseTimeUtils {
	public static final String FC_HANDLER_TIME_PATTERN = "yyyy-MM-dd HH_mm_ss";
	public static final String COMMON_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static final String COMMON_TIME_PATTERN2 = "yyyy-MM-dd HH:mm";
	public static final String WEATHER_TIME_PATTERN = "yyyyMMddHHmm";
	public static final String YEAR_MONTH_DATE_TIME_PATTERN = "yyyy-MM-dd";
	public static final String YEAR_MONTH_DATE_TIME_PATTERN2 = "yyyy年MM月dd日";
	public static final String MINUTE_SECOND_PATTERN = "mm分ss秒";
	public static final String NOTIFICATION_PATTERN = "HH:mm";
	public static final String MESSAGE_CENTER_LISTITEM_PATTERN2 = "MM-dd";
	public static final String REFUND_DETAIL_TIME_PATTERN= "yyyy/MM/dd HH:mm";

	/**
	 * @param date
	 * @return
	 */
	public static long getMillis(Date date) {
		return date.getTime();
	}

	/**
	 * @param millis
	 * @return
	 */
	public static Date getDate(long millis) {
		return new Date(millis);
	}

	/**
	 * @param pattern
	 * @return
	 */
	public static SimpleDateFormat getSimpleDateFormat(String pattern) {
		return new SimpleDateFormat(pattern, Locale.CHINA);
	}

	/**
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String getFormatDate(Date date, String pattern) {
		return getSimpleDateFormat(pattern).format(date);
	}

	/**
	 * @param millis
	 * @param pattern
	 * @return
	 */
	public static String getFormatDate(long millis, String pattern) {
		return getFormatDate(getDate(millis), pattern);
	}

	/**
	 * @param date
	 * @return
	 */
	public static Calendar getCalendar(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}

	/**
	 * @param millis
	 * @return
	 */
	public static Calendar getCalendar(long millis) {
		return getCalendar(getDate(millis));
	}

	/**
	 * @param time
	 * @param pattern
	 * @param needCreate
	 * @return
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
	 * @param seconds
	 * @return
	 */
	public static long secondsToMills(long seconds) {
		return TimeUnit.SECONDS.toMillis(seconds);
	}

	/**
	 * @param minutes
	 * @return
	 */
	public static long minutesToMills(long minutes) {
		return TimeUnit.MINUTES.toMillis(minutes);
	}

	/**
	 * @param hours
	 * @return
	 */
	public static long hoursToMills(long hours) {
		return TimeUnit.HOURS.toMillis(hours);
	}

	/**
	 * @param days
	 * @return
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
	 *
	 * @param sdate
	 * @return
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
		// String curDate = getFormatDate(cal.getTime(),
		// MESSAGE_CENTER_LISTITEM_PATTERN2);
		// String paramDate = getFormatDate(time,
		// MESSAGE_CENTER_LISTITEM_PATTERN2);
		// if (curDate.equals(paramDate)) {
		// int hour = (int) ((cal.getTimeInMillis() - time.getTime()) /
		// 3600000);
		// if (hour == 0)
		// ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1)
		// + "分钟前";
		// else
		// ftime = hour + "小时前";
		// return ftime;
		// }else{
		//
		// }
	}

	/**
	 * 以友好的方式显示时间(订单列表显示)
	 *
	 * @param sdate
	 * @return
	 */
	public static String getFriendlyTimeUsedInOrderList(long millis) {
		// 方法 1
		String fMonthAndDay = "";
		String fHours = "";
		// 获取系统时间
		Calendar cal = Calendar.getInstance();
		long timeNow = cal.getTimeInMillis();
		// if (millis > timeNow || millis < 0) {
		// return "The incoming time error";
		// }
		fMonthAndDay = getFormatDate(millis, "MM-dd");// 格式化为01-01
		fHours = getFormatDate(millis, "HH");// 格式化为24小时

		int fHoursInt = Integer.parseInt(fHours);
		if (fHoursInt <= 12) {// 小于12点 包括0点
			return fMonthAndDay + " " + "上午" + " " + fHoursInt + "点";// 01-01上午11点（含0点）
		} else {// 大于12点
			return fMonthAndDay + " " + "下午" + " " + fHoursInt + "点";// 01-01下午13点（含12点）
		}

		// 方法 2
		// SimpleDateFormat format = new SimpleDateFormat("MM-dd HH");
		// String d1 = format.format(millis);
		// String string1=d1.substring(0, 6);
		// String string2=d1.substring(d1.indexOf(" "), d1.indexOf(" ")+3);
		// String string3;
		// if(Double.parseDouble(string2)<=12){
		// string3="上午";
		// }else{
		// string3="下午";
		// }
		// return string1+" "+string3+" "+string2+"点";

	}

	/**
	 * 以友好的方式显示时间(交易协议)
	 *
	 * @param sdate
	 * @return
	 */
	public static String getDateFormat(long millis) {
		String fMonthAndDay = "";
		String fHours = "";
		fMonthAndDay = getFormatDate(millis, "yyyy年MM月dd日");// 格式化为01-01
		fHours = getFormatDate(millis, "HH");// 格式化为24小时

		int fHoursInt = Integer.parseInt(fHours);
		if (fHoursInt < 12) {// 小于12点 包括0点
			return fMonthAndDay + " " + "上午" + " " + fHoursInt + "点";// 01-01上午11点（含0点）
		} else {// 大于12点
			return fMonthAndDay + " " + "下午" + " " + fHoursInt + "点";// 01-01下午13点（含12点）
		}
	}

	/**
	 * 时间戳 毫秒 转成 2016年05月28日 上午 06时
	 * 220  2016年05月28日 06时
	 * @param l
	 * @return
	 */
	public static String parseLong(long l) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
		String s = "";
		String s2 = "";
		Date date = new Date(l);
		s = sdf.format(date);
		s2 = s.substring(0, s.indexOf(" "));
		System.err.println(s.substring(s.indexOf(" ") + 1, s.indexOf(" ") + 3));
		String s3 = s.substring(s.indexOf(" ") + 1, s.indexOf(" ") + 3);
//		if (Integer.parseInt(s3) <= 12) {
//			s2 = s2 + " " + "上午" + " " + s3 + "时";
//		} else {
//			s2 = s2 + " " + "下午" + " " + s3 + "时";
//		}
		//220   2016年05月28日 06时
		s2 = s2  + " " + s3 + "时";
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
	 * @param format：格式
	 *            如：yyyy年MM月dd日 ，yyyy-MM-dd
	 * @return
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
	 *
	 * @param date
	 * @param sameDate
	 * @return
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

			// if (date.getYear() == sameDate.getYear() && date.getMonth() ==
			// sameDate.getMonth()

			// && date.getDate() == sameDate.getDate()) {

			// return true;

			// }
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		return false;

	}

	/**
	 * 时间显示
	 *
	 * @param strLongTime:时间戳
	 * @return
	 */
	public static String showTime(String strLongTime) {
		String retStr = "";
		if(strLongTime == null || strLongTime.equals("")) {
			return retStr;
		}
		DateFormat df = EaseTimeUtils.getSimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			strLongTime = getFormatDate(Long.parseLong(strLongTime), EaseTimeUtils.COMMON_TIME_PATTERN);
			Date d1 = new Date();// 获取当前时间
			Date d2 = df.parse(strLongTime);

			long diff = d1.getTime() - d2.getTime();
			long days = diff / (1000 * 60 * 60 * 24);
			long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
			long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
			long x = (days * 24 + hours);

			if(days>=1){
				retStr = ""+days + "天前";
				if(days>365){
					retStr = "1年前";
				}
			}else{
				if (x != 0) {
					retStr = ""+x+"小时前";
				} else {
					if(minutes==0){
						//retStr = "1分钟前";
						retStr = "刚刚";
					}else {
						retStr = ""+minutes + "分钟前";
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
	 * @param startTime:yyyy-MM-dd HH:mm:ss
	 * @param endTime:yyyy-MM-dd HH:mm:ss
     * @return 返回间隔的秒数
     */
	public static long calculationDate(String startTime, String endTime) {
		SimpleDateFormat d = new SimpleDateFormat(COMMON_TIME_PATTERN);//格式化时间
		long result = 0;//结果
		try {
			//除以1000得到秒，相应的60000得到分，3600000得到小时
			result =(d.parse(endTime).getTime()-d.parse(startTime).getTime())/1000;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

}
