package com.legendapl.fluent.common.env;

import java.util.ResourceBundle;

/**
 * 設定項目値取得
 * @author 宋
 *
 */
public class Env {

	/** 設定ファイル */
	private static ResourceBundle bundle = ResourceBundle.getBundle("env");

	/**
	 * 値取得
	 * @param key	キー
	 * @return	値
	 */
	public static String value(String key) {
		String value = bundle.getString(key);
		return value;
	}
}
