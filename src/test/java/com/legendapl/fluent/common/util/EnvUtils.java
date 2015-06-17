package com.legendapl.fluent.common.util;

import java.util.Date;

import com.legendapl.fluent.common.env.Env;

/**
 * テスト環境関連ユーティリティクラス
 * @author la_song
 *
 */
public class EnvUtils {

	/**
	 * インスタンス生成抑制
	 */
	private EnvUtils() {
	}

	/**
	 * 画面キャプチャーを保存するディレクトリパスを取得する。
	 * @param className	 クラス名
	 * @param methodName メソッド名
	 * @return ディレクトリパス
	 * @throws Exception
	 */
	public static String getScreenShotDirPath(String className, String methodName) throws Exception {

		String screenshotDir = Env.value("dir.screenshot.base");

		StringBuilder sb = new StringBuilder();
		sb.append(screenshotDir).append("/");
		sb.append(className).append("/");
		sb.append(methodName).append("/");

		return sb.toString();
	}

	/**
	 * 臨時画面キャプチャイメージファイルパスを取得する。
	 * @return	臨時ファイルパス
	 */
	public static String getTempScreenshotFilePath() {

		String tempDir = getTempScreenshotDirPath();

		StringBuilder sb = new StringBuilder();
		sb.append(tempDir).append("/");
		sb.append(new Date().getTime() + ".png");

		String filePath = sb.toString();
		return filePath;
	}

	/**
	 * 臨時画面キャプチャイメージディレクトリパスを取得する。
	 * @return	臨時ディレクトリパス
	 */
	public static String getTempScreenshotDirPath() {
		return Env.value("dir.screenshot.temp");
	}
}
