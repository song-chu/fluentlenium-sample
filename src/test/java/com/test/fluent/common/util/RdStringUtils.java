package com.test.fluent.common.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * 文字列操作ユーティリティ
 * @author c.k.song
 *
 */
public class RdStringUtils {

	/** 文字：0 */
	private static final char ZERO = '0';
	/** 半角スペース */
	private static final char SPACE = ' ';

	/**
	 * パラメータが全部Blankなのか
	 * @param strs	文字列の配列
	 * @return	true 全部Blank
	 */
	public static boolean isAllBlanck(String... strs) {

		for (String str : strs) {
			if (!StringUtils.isBlank(str)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 指定数分？をつける
	 * @param length	件数
	 * @return	PlaceHolder文
	 */
	public static String preparePlaceHolders(int length) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < length;) {
			builder.append("?");
			if (++i < length) {
				builder.append(",");
			}
		}
		return builder.toString();
	}

	/**
	 * SQLのIN文を取得する
	 * @param objects	オブジェクト
	 * @return	IN文
	 */
	public static String getInClause(Object... objects) {
		StringBuilder builder = new StringBuilder();

		for (Object object : objects) {

			if (!"".equals(builder.toString())) {
				builder.append(",");
			}
			builder.append("'");
			builder.append(object.toString());
			builder.append("'");
		}
		return builder.toString();
	}

	/**
	 * 同一文字を指定数文繰り返す。
	 * @param chr	文字
	 * @param size	サイズ
	 * @return	文字列
	 */
	public static String repeatChar(char chr, int size) {
		char[] data = new char[size];

		for (int i = 0; i < size; i++) {
			data[i] = chr;
		}

		return String.copyValueOf(data);
	}

	/**
	 * ゼロを指定数繰り返す。
	 * @param size	サイズ
	 * @return	文字列
	 */
	public static String zero(int size) {
		return repeatChar(ZERO, size);
	}

	/**
	 * スペースを指定数繰り返す。
	 * @param size	サイズ
	 * @return	文字列
	 */
	public static String space(int size) {
		return repeatChar(SPACE, size);
	}

	/**
	 * 指定した文字エンコーディングでの文字列のバイト数を取得し、
	 * 指定したサイズになるまで、文字を右に付ける。
	 *
	 * @param str 処理対象となる文字列
	 * @param enc 文字エンコード("Shift_JIS", "UTF-8" etc...)
	 * @param size	サイズ
	 * @param chr	文字
	 * @return 文字列のバイト数
	 */
	public static String rightPad(String str, String enc, int size, char chr) {
		int byteLength = getByteLength(str, enc);

		String temp = str;
		if (byteLength > size) {
			temp = StringUtils.left(temp, temp.length() - 1);
			return rightPad(temp, enc, size, chr);
		}

		String padding = repeatChar(chr, (size - byteLength));
		return temp + padding;
	}

	/**
	 * 指定した文字エンコーディングでの文字列のバイト数を取得し、
	 * 指定したサイズになるまで、文字を左に付ける。
	 *
	 * @param str 処理対象となる文字列
	 * @param enc 文字エンコード("Shift_JIS", "UTF-8" etc...)
	 * @param size	サイズ
	 * @param chr	文字
	 * @return 文字列のバイト数
	 */
	public static String leftPad(String str, String enc, int size, char chr) {
		int byteLength = getByteLength(str, enc);

		String temp = str;
		if (byteLength > size) {
			temp = StringUtils.right(temp, temp.length() - 1);
			return leftPad(temp, enc, size, chr);
		}

		String padding = repeatChar(chr, (size - byteLength));
		return padding + temp;
	}

	/**
	 * 指定した文字エンコーディングでの文字列のバイト数を取得
	 *
	 * @param str 処理対象となる文字列
	 * @param enc 文字エンコード("Shift_JIS", "UTF-8" etc...)
	 * @return 文字列のバイト数
	 */
	public static int getByteLength(String str, String enc) {
		if (str == null || str.length() == 0)
			return 0;
		int ret = 0;
		try {
			ret = str.getBytes(enc).length;
		} catch (UnsupportedEncodingException e) {
			ret = 0;
		}
		return ret;
	}

	/**
	 * 指定したオブジェクトがNull場合、空文字列に変換
	 *
	 * @param obj オブジェクト
	 * @return 文字列
	 */
	public static String n2s(Object obj) {
		if (obj == null) {
			return "";
		}

		return obj.toString();
	}

	/**
	 * 指定した複数のオブジェクトを文字列に変換し、連結する。
	 * @param objs	オブジェクト
	 * @return	連結文字列
	 */
	public static String concatenate(Object... objs) {
		StringBuilder sb = new StringBuilder();

		for (Object obj : objs) {
			String str = n2s(obj);
			sb.append(str);
		}

		return sb.toString();
	}

	/**
	 * 指定した複数のオブジェクトを文字列に変換し、指定した区切り文字で連結する。
	 * @param separator	区切り文字
	 * @param objs	オブジェクト
	 * @return	連結文字列
	 */
	public static String separate(char separator, Object... objs) {

		StringBuilder sb = new StringBuilder();

		for (Object obj : objs) {
			String str = n2s(obj);

			if (sb.length() > 0) {
				sb.append(separator);
			}

			sb.append(str);
		}

		return sb.toString();
	}

	/**
	 * CSV文字列をリストに変換する。
	 * @param csvStr	CSV文字列
	 * @return	リスト
	 */
	public static List<String> splitCsv(String csvStr) {
		return split(csvStr, ',');
	}

	/**
	 * 文字列を指定した区切り文字で区切って、リストに変換する。
	 * @param str	文字列
	 * @param delimeter 区切り文字
	 * @return	リスト
	 */
	public static List<String> split(String str, char delimeter) {
		boolean toggle = false;
		List<String> tokens = new ArrayList<String>();

		if (str == null || str.length() == 0) {
			return tokens;
		}

		int beginIndex = 0;
		int endIndex = 0;

		for (int i = 0; i < str.length(); i++) {

			char chr = str.charAt(i);

			if (chr == '"' || chr == '\'') {
				if (toggle) {
					endIndex = i;

				} else {
					beginIndex = i + 1;
					endIndex = i + 1;
				}

				toggle = !(toggle);

			} else if (chr == delimeter) {
				if (toggle) {
					continue;
				} else {
					String token = str.substring(beginIndex, endIndex);
					tokens.add(token);
					beginIndex = i + 1;
					endIndex = i + 1;
				}

			} else {
				endIndex = i + 1;
			}
		}

		String token = str.substring(beginIndex, endIndex);
		tokens.add(token);

		return tokens;
	}
}
