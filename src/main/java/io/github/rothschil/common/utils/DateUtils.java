package io.github.rothschil.common.utils;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类, 继承 {@link org.apache.commons.lang3.time.DateUtils }类
 *
 * @author <a href="https://github.com/rothschil">Sam</a>
 * @since 2014-4-15
 */
@Slf4j
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

	public static final Date DEFAULT_DATE = DateUtils.parseByDayPattern("1970-01-01");

	public static final String SINGLE_MONTH_PATTERN = "MM";
	public static final String SINGLE_DAY_PATTERN = "dd";

	public static final String YYYY_PATTERN = "yyyy";
	public static final String MONTH_PATTERN = "yyyy-MM";
	public static final String HOUR_PATTERN = "HH:mm:ss";
	public static final String DAY_PATTERN = "yyyy-MM-dd";

	public static final String HOUR_MI_PATTERN = "HHmm";

	public static final String IVR_COMP_PATTERN = "MM月dd日HH时";
	public static final String IVR_COMP_PATTERN_YMD = "yyyy年MM月dd日";

	public static final String TRANS_DAY_PATTERN = "yyyyMMdd";
	public static final String TRANS_MONTH_PATTERN = "yyyyMM";

	public static final String TRANS_PATTERN_MM = "yyyyMMddHHMM";
	public static final String TRANS_PATTERN = "yyyyMMddHHmmss";
	public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static final String STAND_OS_PATTERN = "yyyy/MM/dd";

	public static Date getNowDate() {
		return new Date();
	}


	/**
	 * 返回 yyyyMMddHHmmss 时间格式
	 *
	 * @return String
	 * @author <a href="https://github.com/rothschil">Sam</a>
	 * @date 2018/4/24-20:47
	 **/
	public static String getTransId() {
		LocalDateTime dateTime = LocalDateTime.now();
		DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern(TRANS_PATTERN);
		return dtf2.format(dateTime);
	}

	/**
	 * 7位
	 */
	private final static int SALT = 0X989680;

	/**
	 * 6位
	 */
	private final static int SALT_6 = 0XF4240;

	/**
	 * 返回 yyyyMMddHHmmss 时间格式，具体使用还需 加上 6/7/8 位随机数字
	 *
	 * @return String
	 * @author <a href="https://github.com/rothschil">Sam</a>
	 * @date 2018/4/24-20:47
	 **/
	public static String transId() {
		String time = DateUtils.getTransId();
		long dom = (long) (Math.random() * SALT);
		return time + dom;
	}

	/**
	 * 返回 yyyyMMddHHmmss 时间格式，具体使用还需 加上 6/7/8 位随机数字
	 *
	 * @return String
	 * @author <a href="https://github.com/rothschil">Sam</a>
	 * @date 2018/4/24-20:47
	 **/
	public static String transId(int salt) {
		String time = DateUtils.getTransId();
		long dom = (long) (Math.random() * salt);
		return time + dom;
	}


	/**
	 * 计算相差天数
	 */
	public static int differentDaysByMillisecond(Date date1, Date date2) {
		return Math.abs((int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24)));
	}

	/**
	 * 计算两个时间差
	 */
	public static String getDatePoor(Date endDate, Date nowDate) {
		long nd = 1000 * 24 * 60 * 60;
		long nh = 1000 * 60 * 60;
		long nm = 1000 * 60;
		// long ns = 1000;
		// 获得两个时间的毫秒时间差异
		long diff = endDate.getTime() - nowDate.getTime();
		// 计算差多少天
		long day = diff / nd;
		// 计算差多少小时
		long hour = diff % nd / nh;
		// 计算差多少分钟
		long min = diff % nd % nh / nm;
		// 计算差多少秒//输出结果
		// long sec = diff % nd % nh % nm / ns;
		return day + "天" + hour + "小时" + min + "分钟";
	}

	/**
	 * 获取服务器启动时间
	 */
	public static Date getServerStartDate() {
		long time = ManagementFactory.getRuntimeMXBean().getStartTime();
		return new Date(time);
	}

	/**
	 * 日期路径 即年/月/日 如2018/08/08
	 */
	public static String datePath() {
		Date now = new Date();
		return DateFormatUtils.format(now, STAND_OS_PATTERN);
	}

	private static final String[] PARSE_PATTERNS = {
			"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
			"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
			"yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};


	public static String dateTimeNow(final String format) {
		return parseDateToStr(format, new Date());
	}

	public static String parseDateToStr(final String format, final Date date) {
		return new SimpleDateFormat(format).format(date);
	}

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate(DAY_PATTERN);
	}

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}

	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） patternDateUtil.java可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 *
	 * @param date    输入日期
	 * @param pattern 格式化的规则列表
	 * @return 对给定日期按照输入格式进行格式化
	 */
	public static String formatDate(Date date, Object... pattern) {
		String formatDate;
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, DAY_PATTERN);
		}
		return formatDate;
	}


	public static Date parseByDayPattern(String str) {
		return parseDate(str, DAY_PATTERN);
	}

	public static Date parseDate(String str, String pattern) {
		try {
			return parseDate(str, new String[]{pattern});
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, DATETIME_PATTERN);
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 *
	 * @return String  yyyy-MM-dd HH:mm:ss 标准格式 格式
	 */
	public static String formatCurrentDateTime() {
		return formatDate(new Date(), DATETIME_PATTERN);
	}


	public static String getNowDateYyyyMmDdHhMmSs() {
		return DateUtil.format(new Date(), "yyyyMMddHHmmss");
	}

	public static String getNowDateYyyyMmDdHhMmSs2() {
		return DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 *
	 * @return String  HH:mm:ss 标准格式 格式
	 */
	public static String formatCurrentTime() {
		return formatDate(new Date(), HOUR_PATTERN);
	}

	public static String formatByDateTimePattern(Date date) {
		return DateFormatUtils.format(date, DATETIME_PATTERN);
	}

	/**
	 * 根据格式产生时间
	 *
	 * @param format 格式化表达式
	 * @return 格式化的字符串时间戳
	 */
	public static String getDateTime(String format) {
		return formatDate(new Date(), format);
	}

	/**
	 * 得到当前年份字符串 格式（yyyy）
	 *
	 * @return YYYY格式的年份
	 */
	public static String getYear() {
		return formatDate(new Date(), YYYY_PATTERN);
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 *
	 * @return MM格式的月份
	 */
	public static String getMonth() {
		return formatDate(new Date(), SINGLE_MONTH_PATTERN);
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), SINGLE_DAY_PATTERN);
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}

	/**
	 * 日期型字符串转化为日期 格式
	 * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
	 * "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm",
	 * "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
	 */
	public static Date parseDate(Object str) {
		if (str == null) {
			return null;
		}
		try {
			return parseDate(str.toString(), PARSE_PATTERNS);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * @param date1 第一个时间
	 * @param date2 第二个时间
	 * @return long
	 * @date 2019/12/4 9:35
	 */
	public static long getMills(Date date1, Date date2) {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.clear();
		calendar1.setTime(date1);

		Calendar calendar2 = Calendar.getInstance();
		calendar2.clear();
		calendar2.setTime(date2);
		return calendar2.getTimeInMillis() - calendar1.getTimeInMillis();
	}

	/**
	 * 获取过去的天数
	 *
	 * @param date 给定日期
	 * @return 日期之间相差的天数
	 */
	public static long pastDays(Date date) {
		long t = System.currentTimeMillis() - date.getTime();
		return t / (24 * 60 * 60 * 1000);
	}

	/**
	 * 获取过去的小时
	 *
	 * @param date 给定日期
	 * @return 日期之间相差的小时数
	 */
	public static long pastHour(Date date) {
		long t = System.currentTimeMillis() - date.getTime();
		return t / (60 * 60 * 1000);
	}

	/**
	 * 获取过去的分钟
	 *
	 * @param date 给定日期
	 * @return 日期之间相差的分钟数
	 */
	public static long pastMinutes(Date date) {
		long t = System.currentTimeMillis() - date.getTime();
		return t / (60 * 1000);
	}

	/**
	 * 转换为时间（天,时:分:秒.毫秒）
	 *
	 * @param timeMillis 毫秒数
	 * @return yyyy-MM-dd HH:mm:ss:sss 格式时间戳
	 */
	public static String formatDateTime(long timeMillis) {
		long day = timeMillis / (24 * 60 * 60 * 1000);
		long hour = (timeMillis / (60 * 60 * 1000) - day * 24);
		long min = ((timeMillis / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long s = (timeMillis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		long sss = (timeMillis - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - s * 1000);
		return (day > 0 ? day + "," : "") + hour + ":" + min + ":" + s + "." + sss;
	}

	/**
	 * 获取两个日期之间的天数
	 *
	 * @param before 第一日期，代表相对于第二日期来说是以前
	 * @param after  第二日期
	 * @return 日期之间相差的天数
	 */
	public static long distanceOfDayByDate(Date before, Date after) {
		return (after.getTime() - before.getTime()) / (1000 * 60 * 60 * 24);
	}

	/**
	 * 获取两个日期之间的秒
	 *
	 * @param date1 第一日期
	 * @param date2 第二日期
	 * @return long 二者下相差的秒
	 * @author <a href="https://github.com/rothschil">Sam</a>
	 * @date 2018/4/12 19:53
	 */
	public static long distanceOfSecondByDate(Date date1, Date date2) {
		long time1 = date1.getTime();
		long time2 = date2.getTime();
		return (time2 - time1) / (1000 * 3600 * 24);
	}

	/**
	 * 获取两个日期之间的毫秒数
	 *
	 * @param before 第一日期，代表相对于第二日期来说是以前
	 * @param after  第二日期
	 * @return 日期之间相差的天数
	 */
	public static long distanceOfDayByMills(Date before, Date after) {
		return (after.getTime() - before.getTime());
	}

	/**
	 * offset 与当天的偏移，负数 则是往前
	 *
	 * @param offset 偏移数值
	 * @return String yyyyMMdd 格式日期戳
	 */
	public static String offset(int offset) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, offset);
		Date time = cal.getTime();
		return new SimpleDateFormat(TRANS_DAY_PATTERN).format(time);
	}



	/**
	 * offset 与当天的偏移，负数 则是往前
	 *
	 * @param offset  偏移数值
	 * @param pattern 使用传入的格式
	 * @return String yyyyMMdd 格式日期戳
	 */
	public static String offset(int offset, String pattern) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, offset);
		Date time = cal.getTime();
		return new SimpleDateFormat(pattern).format(time);
	}

	/**
	 * offset 与当天的偏移，负数 则是往前
	 *
	 * @param offset  偏移数值
	 * @param pattern 使用传入的格式
	 * @return String yyyyMMdd 格式日期戳
	 */
	public static String offsetHour(int offset, String pattern) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, offset);
		Date time = cal.getTime();
		return new SimpleDateFormat(pattern).format(time);
	}

	/**
	 * 日期时间偏移，在给定日期上做偏移，负数 则是往前
	 * offset = 1,date=2018-11-02 16:47:00
	 * 结果：2018-11-03 16:47:00
	 *
	 * @param date   给定日期
	 * @param offset 偏移的数值
	 * @return Date 目标日期
	 */
	public static Date offset(Date date, int offset) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, offset);
		return cal.getTime();
	}

	/**
	 * 日期时间偏移，在给定日期上做偏移，负数 则是往前
	 * offset = 1,date=2018-11-02 16:47:00
	 * 结果：2018-11-03 16:47:00
	 *
	 * @param inputDate   YYYY-MM-DD 格式字符
	 * @param offset 偏移的数值
	 * @return Date 目标日期
	 */
	public static String offset(String inputDate, int offset) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DAY_PATTERN);
		LocalDate date = LocalDate.parse(inputDate, dtf);
		date.plusDays(-1);
		return dtf.format(date);
	}

	/**
	 * 当前时间日期时间偏移，在给定日期上做偏移，负数 则是往前
	 * offset = 1,date=2018-11-02 16:47:00
	 * 结果：2018-11-03 16:47:00
	 *
	 * @param offset 偏移的数值
	 * @return Date 目标日期
	 */
	public static String offsetByDateNow(int offset) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DAY_PATTERN);
		LocalDate localDate = LocalDate.now();
		localDate.plusDays(offset);
		log.error("localDate.getDayOfMonth:{}", localDate.getDayOfMonth());
		return dtf.format(localDate);
	}


	/**
	 * 日期偏移，负数 则是往前
	 *
	 * @param date     给定日期
	 * @param offset   偏移量
	 * @param calendar 日历
	 * @return java.util.Date
	 */
	public static Date offset(Date date, int offset, int calendar) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(calendar, offset);
		return cal.getTime();
	}

	/**
	 * 月份偏移
	 *
	 * @param offset 与当前月份的偏移，负数 则是往前
	 * @return String   yyyyMM 格式的日期戳
	 */
	public static String monthOffset(int offset) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, offset);
		Date time = cal.getTime();
		return new SimpleDateFormat(TRANS_MONTH_PATTERN).format(time);
	}

	public static boolean isValidDate(String str, String pattern) {
		boolean convertSuccess = true;
		// 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		try {
			// 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
			format.setLenient(false);
			format.parse(str);
		} catch (ParseException e) {
			convertSuccess = false;
		}
		return convertSuccess;
	}

	/**
	 * 根据输入字符串，转换日期
	 *
	 * @param str     日期时间戳
	 * @param pattern 格式化 pattern
	 * @return java.utils.Date
	 * @author <a href="https://github.com/rothschil">Sam</a>
	 */
	public static Date parseDateByInput(String str, String pattern) {
		if (StringUtils.isBlank(str)) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			return sdf.parse(str);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 日期格式转换
	 *
	 * @param aDate util类型Date
	 * @return Date SQL类型 Date
	 * @author <a href="https://github.com/rothschil">Sam</a>
	 **/
	public static java.sql.Date util2sql(Date aDate) {

		if (aDate == null) {
			return null;
		}
		return new java.sql.Date(aDate.getTime());
	}

	/**
	 * 根据当前时间，获取上 N 个小时整点小时，格式为 yyyy-MM-dd HH:00:00
	 *
	 * @param n 偏移量
	 * @return 返回 小时
	 */
	public static String hourOffset(int n) {
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.SECOND, 0);
		SimpleDateFormat sdf = new SimpleDateFormat(DATETIME_PATTERN);
		ca.set(Calendar.HOUR_OF_DAY, ca.get(Calendar.HOUR_OF_DAY) - n);
		return sdf.format(ca.getTime());
	}

	/**
	 * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
	 *
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 * @author jqlin
	 */
	public static boolean isEffectiveDate(Date startTime, Date endTime) {
		return isEffectiveDate(new Date(),startTime,endTime);
	}

	/**
	 * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
	 *
	 * @param nowTime 当前时间
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 * @author jqlin
	 */
	public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
		if (nowTime.getTime() == startTime.getTime()
				|| nowTime.getTime() == endTime.getTime()) {
			return true;
		}
		Calendar date = Calendar.getInstance();
		date.setTime(nowTime);
		Calendar begin = Calendar.getInstance();
		begin.setTime(startTime);
		Calendar end = Calendar.getInstance();
		end.setTime(endTime);
		if (date.after(begin) && date.before(end)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 两时间的相差的秒
	 *
	 * @param bef 日期时间1
	 * @param end 日期时间2
	 * @return long 返回 秒
	 */
	public static long secondOffset(LocalDateTime bef, LocalDateTime end) {
		Duration duration = Duration.between(bef, end);
		return duration.getSeconds();
	}

	/**
	 * 给定时间与当前时间之间的相差的秒
	 *
	 * @param bef 日期时间1
	 * @return long 返回 秒
	 */
	public static long secondOffset(LocalDateTime bef) {
		return secondOffset(bef, LocalDateTime.now());
	}

	/**
	 * 获取当前时间的整点小时时间，默认格式为yyyy-MM-dd HH:mm:ss
	 *
	 * @param pattern 可为空
	 * @return java.lang.String
	 * @date 2019/10/23 23:59
	 */
	public static String getCurrHourTime(String pattern) {
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.SECOND, 0);
		if (null == pattern) {
			pattern = DATETIME_PATTERN;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(ca.getTime());
	}


	/**
	 * 判断两个日期的年月日是否相等
	 *
	 * @param date1 第一日期
	 * @param date2 第二日期
	 * @return boolean 真：True，假：False
	 * @author <a href="https://github.com/rothschil">Sam</a>
	 * @date 2019/10/23 23:58
	 */
	public static boolean isSameDate(Date date1, Date date2) {
		try {
			Calendar cal1 = Calendar.getInstance();
			cal1.setTime(date1);
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(date2);
			boolean isSameYear = cal1.get(Calendar.YEAR) == cal2
					.get(Calendar.YEAR);
			boolean isSameMonth = isSameYear
					&& cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
			return isSameMonth
					&& cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}


	public static final Date dateTime(final String format, final String ts) {

		try {
			return new SimpleDateFormat(format).parse(ts);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取时长
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static String getDateDuration(Date startDate, Date endDate) {
		// 获得两个时间的毫秒时间差异
		long diff = endDate.getTime() - startDate.getTime();
		int duration = (int) ((diff) / 1000);
		return  duration+"";
	}

	// 定义偏移日期方法
	public static String offsetDate(int daysToSubtract) {

		// 获取当前日期
		Calendar calendar = Calendar.getInstance();

		// 往前偏移指定天数
		calendar.add(Calendar.DAY_OF_MONTH, daysToSubtract);
		Date offsetDate = calendar.getTime();

		// 格式化日期为 YYYYMMDD 格式
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		return dateFormat.format(offsetDate);
	}


	/** 上个月的开始时间
	 * @description:
	 * @return: java.lang.String
	 * @date: 2024/12/17 12:34
	 */
	public static String getLastMonthStartDate() {

		// 获取当前日期
		LocalDate currentDate = LocalDate.now();

		// 计算上个月的开始日期
		LocalDate lastMonthStartDate = currentDate.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());

		// 使用指定的格式将日期转换为字符串
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(TRANS_DAY_PATTERN);
		return lastMonthStartDate.format(dateFormatter);
	}

	//上个月的结束时间
	public static String getLastMonthEndDate() {
		// 获取当前日期
		LocalDate currentDate = LocalDate.now();

		// 计算上个月的结束日期
		LocalDate lastMonthEndDate = currentDate.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());

		// 使用指定的格式将日期转换为字符串
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(TRANS_DAY_PATTERN);
		return lastMonthEndDate.format(dateFormatter);
	}
}
