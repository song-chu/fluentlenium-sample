package com.test.fluent.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;

/**
 * 日付ユーティリティ
 * @author c.k.song
 *
 */
public class RdDateUtils {

	/** yyyy/MM/dd */
	private static final String YYYY_MM_DD_SLASH = "yyyy/MM/dd";
	/** yyyyMMdd */
	private static final String YYYY_MM_DD_PLAIN = "yyyyMMdd";
	/** Locale:JP */
	private static final Locale JP_LOCALE = new Locale("ja", "JP", "JP");
	/** 和暦フォーマット：yyMMdd */
	private static final FastDateFormat warekiFomtatter = FastDateFormat.getInstance("yyMMdd", JP_LOCALE);

	/**
	 * 指定日を指定したフォーマットに変換
	 * @param date		日付
	 * @param format	フォーマット
	 * @return	フォーマットされた日付
	 */
	public static String getFormattedDate(Date date, String format) {
		if (date == null) {
			return "";
		}
		return DateFormatUtils.format(date, format);
	}

	/**
	 * システム日付をYYYYMMDDに変換
	 * @return	システム日付
	 */
	public static String getSystemDate() {
		return getFormattedDate(new Date(), YYYY_MM_DD_PLAIN);
	}

	/**
	 * 明日をYYYYMMDDに変換
	 * @return	明日
	 */
	public static String getTomorrow() {
		Date tomorrow = DateUtils.addDays(new Date(), 1);
		return getFormattedDate(tomorrow, YYYY_MM_DD_PLAIN);
	}

	/**
	 * 次営業日をYYYYMMDDに変換
	 * ※注意：土日のみスキップ
	 * @return	次営業日
	 */
	public static String getNextSaleDay() {
		java.sql.Date tomorrow = getNextSaleDate();
		return getFormattedDate(tomorrow, YYYY_MM_DD_PLAIN);
	}

	/**
	 * 次営業日をjava.sql.Dateで取得
	 * ※注意：土日のみスキップ
	 * @return	次営業日
	 */
	public static java.sql.Date getNextSaleDate() {
		Date tomorrow = DateUtils.addDays(new Date(), 1);

		while (true) {
			String shortName = getDayOfWeekShort(tomorrow);

			if (shortName.equals("土")) {
				tomorrow = DateUtils.addDays(tomorrow, 2);
			} else if (shortName.equals("日")) {
				tomorrow = DateUtils.addDays(tomorrow, 1);
			}

			break;
		}

		return new java.sql.Date(tomorrow.getTime());
	}

	/**
	 * 前営業日をYYYYMMDDに変換
	 * ※注意：土日のみスキップ
	 * @return	前営業日
	 */
	public static String getBeforeSaleDay() {
		java.sql.Date tomorrow = getBeforeSaleDate();
		return getFormattedDate(tomorrow, YYYY_MM_DD_PLAIN);
	}

	/**
	 * 前営業日をjava.sql.Dateで取得
	 * ※注意：土日のみスキップ
	 * @return	前営業日
	 */
	public static java.sql.Date getBeforeSaleDate() {
		Date previous = DateUtils.addDays(new Date(), -1);

		while (true) {
			String shortName = getDayOfWeekShort(previous);

			if (shortName.equals("土")) {
				previous = DateUtils.addDays(previous, -1);
			} else if (shortName.equals("日")) {
				previous = DateUtils.addDays(previous, -2);
			}

			break;
		}

		return new java.sql.Date(previous.getTime());
	}

	/**
	 *	システム日付を和暦に変換
	 * @return	和暦
	 */
	public static String getWarekiSysDate() {
		//ロケールを指定してCalendarインスタンスを取得
		Locale locale = new Locale("ja", "JP", "JP");
		Calendar calendar = Calendar.getInstance(locale);
		return toWareki(calendar.getTime());
	}

	/**
	 *	和暦に変換
	 * @param date	日付型
	 * @return	和暦
	 */
	public static String toWareki(Date date) {
		//ロケールを指定してCalendarインスタンスを取得

		//calendar.getTime()で現在日時を取得して和暦にフォーマットする
		String dateStr = warekiFomtatter.format(date);

		return dateStr;
	}

	/**
	 * 日付をYYYYMMDDに変換
	 * @param date	日付
	 * @return	YYYYMMDD
	 */
	public static String getFormattedDate(Date date) {
		return getFormattedDate(date, YYYY_MM_DD_PLAIN);
	}

	/**
	 * 指定されたパターン文字列の文字列を Date オブジェクトにして返します。
	 * Date オブジェクトとして有効でない場合は null を返します。
	 *
	 * @param value 日付を表す文字列
	 * @param format 日付を表す文字列のパターン書式 (yyyy/MM/dd など)
	 * @return 日付を表す文字列の Date オブジェクト
	 */
	public static Date toDate(String value, String format) {

		if (StringUtils.isEmpty(value)) {
			return null;
		}

		if (StringUtils.isEmpty(format)) {
			format = YYYY_MM_DD_SLASH;
		}

		// 日付フォーマットを作成
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);

		// 日付の厳密チェックを指定
		dateFormat.setLenient(false);

		try {
			// 日付値を返す
			return dateFormat.parse(value);
		} catch (ParseException e) {
			return null;
		} finally {
			dateFormat = null;
		}
	}

	/**
	 * 指定されたパターン文字列の文字列を Date オブジェクトにして返します。
	 * Date オブジェクトとして有効でない場合は null を返します。
	 *
	 * @param value 日付を表す文字列
	 * @return 日付を表す文字列の Date オブジェクト
	 */
	public static Date toDate(String value) {
		return toDate(value, YYYY_MM_DD_PLAIN);
	}

	/**
	 * 異なる日付オブジェクトが同じ日なのか判断
	 * @param date1		日付１
	 * @param date2		日付２
	 * @return	true　同じ日
	 */
	public static boolean isSameDay(Date date1, Date date2) {
		return DateUtils.isSameDay(date1, date2);
	}

	/**
	 * 開始日～終了日の日付リストを取得する。
	 * @param startDate		開始日
	 * @param endDate		終了日
	 * @return	日付リスト
	 */
	public static List<Date> getListBetweenDates(Date startDate, Date endDate) {
		List<Date> dateList = new ArrayList<Date>();

		if (startDate == null || endDate == null) {
			return null;
		}

		Calendar start = Calendar.getInstance();
		start.setTime(startDate);

		Calendar end = Calendar.getInstance();
		end.setTime(endDate);

		// 当日をリストに含めるため1日を＋
		end.add(Calendar.DAY_OF_YEAR, 1);

		while (start.before(end)) {
			dateList.add(start.getTime());
			start.add(Calendar.DAY_OF_YEAR, 1);
		}

		return dateList;
	}

	/**
	 * java.utilDateをjava.sql.Dateに変換する。
	 * @param utilDate	java.utilDate
	 * @return	java.sql.Date
	 */
	public static java.sql.Timestamp toSqlTimestamp(java.util.Date utilDate) {

		if (utilDate == null) {
			return null;
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(utilDate);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		java.sql.Timestamp sqlTimestamp = new java.sql.Timestamp(cal.getTimeInMillis());

		return sqlTimestamp;
	}

	/**
	 * 指定されたパターン文字列の文字列をjava.sql.Timestampオブジェクトにして返します。
	 * Date オブジェクトとして有効でない場合は null を返します。
	 *
	 * @param value 日付を表す文字列
	 * @param format 日付を表す文字列のパターン書式 (yyyy/MM/dd など)
	 * @return java.sql.Timestamp
	 */
	public static java.sql.Timestamp toSqlTimestamp(String value, String format) {
		return toSqlTimestamp(toDate(value, format));
	}

	/**
	 * 指定された日付文字列をjava.sql.Timestampオブジェクトにして返します。
	 * @param value		日付（yyyyMMdd)
	 * @return　java.sql.Timestamp
	 */
	public static java.sql.Timestamp toSqlTimestamp(String value) {
		return toSqlTimestamp(value, YYYY_MM_DD_PLAIN);
	}

	/**
	 * 現在の曜日を返します。
	 * ※曜日は省略します。
	 * @param date 日付
	 * @return	現在の曜日
	 */
	public static String getDayOfWeekShort(Date date) {

		if (date == null) {
			return null;
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		switch (cal.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.SUNDAY:
			return "日";
		case Calendar.MONDAY:
			return "月";
		case Calendar.TUESDAY:
			return "火";
		case Calendar.WEDNESDAY:
			return "水";
		case Calendar.THURSDAY:
			return "木";
		case Calendar.FRIDAY:
			return "金";
		case Calendar.SATURDAY:
			return "土";
		}
		throw new IllegalStateException();
	}
}
