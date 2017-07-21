package com.aaron.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.text.ParseException;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ���ڹ�����
 * 
 * @author Administrator
 * 
 */
public class DateUtil extends org.apache.commons.lang3.time.DateUtils {
	public static Logger logger = LoggerFactory.getLogger(DateUtil.class);

	/**
	 * �������ڸ�ʽ yyyy-MM-dd
	 */
	public final static DateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * �������ڸ�ʽ yyyy-MM-dd HH:mm:ss
	 */
	public final static DateFormat dfDateTime = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	/**
	 * �������ڸ�ʽ yyyyMMddHHmmssSSS
	 */
	public final static DateFormat serialFormatter = new SimpleDateFormat(
			"yyyyMMddHHmmssSSS");

	/**
	 * �������ڸ�ʽ yyyy-MM-dd HH:mm
	 */
	public final static DateFormat dfLessonDate = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");

	/**
	 * �������ڸ�ʽ HH:mm
	 */
	public final static DateFormat dfHm = new SimpleDateFormat("HH:mm");

	/**
	 * �������ڸ�ʽ yyyyMMdd
	 */
	public final static DateFormat dfYMD = new SimpleDateFormat("yyyyMMdd");

	/**
	 * �������ڸ�ʽ yyyyMMddHHmmss
	 */
	public final static DateFormat dfYMDHMS = new SimpleDateFormat(
			"yyyyMMddHHmmss");

	/**
	 * ���ڶ���ת�ַ���
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String formatDate(Date date, String format) {
		String result = "";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		if (date != null) {
			result = sdf.format(date);
		}
		return result;
	}

	/**
	 * �ַ���ת���ڶ���
	 * 
	 * @param str
	 * @param format
	 * @return
	 * @throws Exception
	 */
	public static Date formatString(String str, String format) throws Exception {
		if (StringUtil.isEmpty(str)) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(str);
	}

	public static String getCurrentDateStr() throws Exception {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		return sdf.format(date);
	}

	/**
	 * ת��Ϊʱ�䣨��,ʱ:��:��.���룩
	 * 
	 * @param timeMillis
	 * @return
	 */
	public static String formatDateTime(long timeMillis) {
		long day = timeMillis / (24 * 60 * 60 * 1000);
		long hour = (timeMillis / (60 * 60 * 1000) - day * 24);
		long min = ((timeMillis / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long s = (timeMillis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		long sss = (timeMillis - day * 24 * 60 * 60 * 1000 - hour * 60 * 60
				* 1000 - min * 60 * 1000 - s * 1000);
		return (day > 0 ? day + "," : "") + hour + ":" + min + ":" + s + "."
				+ sss;
	}

	/**
	 * ��ѯ��ʼʱ��
	 * 
	 * @param source
	 * @return
	 */
	public static Date getStartDate(String source) {
		if (StringUtils.isNotBlank(source)) {
			source = source.trim() + " 00:00:00";
			return parseDateTime(source);
		}
		return null;
	}

	public static Date getStartDate(Date date) {
		String source = dfDate.format(date);
		return getStartDate(source);
	}

	/**
	 * ��ѯ����ʱ��
	 * 
	 * @param source
	 * @return
	 */
	public static Date getEndDate(String source) {
		if (StringUtils.isNotBlank(source)) {
			source = source.trim() + " 23:59:59";
			return parseDateTime(source);
		}
		return null;
	}

	public static Date getEndDate(Date date) {
		String source = dfDate.format(date);
		return getEndDate(source);
	}

	/**
	 * 
	 * @param source
	 * @return
	 */
	public static Date parseDateTime(String source) {
		if (StringUtils.isBlank(source)) {
			return null;
		}
		Date date = null;
		try {
			date = dfDateTime.parse(source.trim());
		} catch (ParseException e) {
			logger.error("����ʱ���쳣��[" + source + "] " + e.getMessage());
		}
		return date;
	}

	/**
	 * 
	 * @param source
	 * @return
	 */
	public static Date parseDate(String source) {
		if (StringUtils.isBlank(source)) {
			return null;
		}
		Date date = null;
		try {
			date = dfDate.parse(source.trim());
		} catch (ParseException e) {
			logger.error("����ʱ���쳣��[" + source + "] " + e.getMessage());
		}
		return date;
	}

	/**
	 * 
	 * @param source
	 * @param pattern
	 * @return
	 */
	public static Date parseDate(String source, String pattern) {
		if (StringUtils.isBlank(source)) {
			return null;
		}
		Date date = null;
		try {
			DateFormat dateFormat = new SimpleDateFormat(pattern);
			date = dateFormat.parse(source.trim());
		} catch (ParseException e) {
			logger.error("����ʱ���쳣��[" + source + "] " + e.getMessage());
		}
		return date;
	}

	/**
	 * ���ݵ�ǰʱ���������к�
	 * 
	 * @return
	 */
	public synchronized static String serialNumber() {
		return serialFormatter.format(new Date());
	}

	/**
	 * �µ�һ��
	 * 
	 * @param date
	 * @return
	 */
	public static Date getMonthStartDate(Date date) {
		Date d = new Date(date.getTime());
		d = setHours(d, 0);
		d = setMinutes(d, 0);
		d = setSeconds(d, 1);
		return setDays(d, 1);
	}

	/**
	 * �����һ��
	 * 
	 * @param date
	 * @return
	 */
	public static Date getMonthEndDate(Date date) {
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(date);
		int maxMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		c.set(Calendar.DAY_OF_MONTH, maxMonth);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		return c.getTime();
	}

	/**
	 * ��ǰ���ڵĵ�һ��
	 * 
	 * @param date
	 * @return
	 */
	public static Date getWeekStartDate(Date date) {
		GregorianCalendar c = new GregorianCalendar(Locale.CHINA);
		c.setTime(date);
		c.setFirstDayOfWeek(RANGE_WEEK_MONDAY);
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		c.set(Calendar.HOUR_OF_DAY, 00);
		c.set(Calendar.MINUTE, 00);
		c.set(Calendar.SECOND, 00);
		return c.getTime();
	}

	/**
	 * ��ǰ���ڵ����һ��
	 * 
	 * @param date
	 * @return
	 */
	public static Date getWeekEndDate(Date date) {
		GregorianCalendar c = new GregorianCalendar(Locale.CHINA);
		c.setTime(date);
		c.setFirstDayOfWeek(RANGE_WEEK_MONDAY);
		c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		return c.getTime();
	}

	public static Date getLessonDate(String source) {
		if (StringUtils.isBlank(source)) {
			return null;
		}
		Date date = null;
		try {
			date = dfLessonDate.parse(source);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * ����ʱ��֮��������
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 * @throws ParseException
	 */
	public static int getMonthSpace(Date startDate, Date endDate) {

		int result = 0;

		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();

		c1.setTime(startDate);
		c2.setTime(endDate);

		result = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);

		return result;

	}

	/**
	 * ���ڼ��������
	 * 
	 * @param early
	 * @param late
	 * @return
	 */
	public static final int daysBetween(Date early, Date late) {

		java.util.Calendar calst = java.util.Calendar.getInstance();
		java.util.Calendar caled = java.util.Calendar.getInstance();
		calst.setTime(early);
		caled.setTime(late);
		// ����ʱ��Ϊ0ʱ
		calst.set(java.util.Calendar.HOUR_OF_DAY, 0);
		calst.set(java.util.Calendar.MINUTE, 0);
		calst.set(java.util.Calendar.SECOND, 0);
		caled.set(java.util.Calendar.HOUR_OF_DAY, 0);
		caled.set(java.util.Calendar.MINUTE, 0);
		caled.set(java.util.Calendar.SECOND, 0);
		// �õ�����������������
		int days = ((int) (caled.getTime().getTime() / 1000) - (int) (calst
				.getTime().getTime() / 1000)) / 3600 / 24;

		return days;
	}

	public static void main(String[] args) {
		try {
			Date currentDate = dfDateTime.parse("2016-10-16 14:19:40");
			Date startDate = DateUtil.getWeekStartDate(currentDate);
			Date endDate = DateUtil.getWeekEndDate(currentDate);
			System.out.println(startDate + "--" + endDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
