package com.test.fluent.common.test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fluentlenium.adapter.FluentTest;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import com.test.fluent.common.env.Env;
import com.test.fluent.common.util.EnvUtils;
import com.test.fluent.common.util.FileAttributeUtil;
import com.test.fluent.common.util.Reporter;

/**
 * 画面テストの基底
 * @author c.k.song
 *
 */
public class BrowserTestBase extends FluentTest {

	/** ログ */
	private static Log log = LogFactory.getLog(BrowserTestBase.class);

	/**
	 * 初期処理
	 *
	 * 既存証跡フォルダを削除する
	 * @throws Exception
	 */
	@Before
	public void deleteExistingScreenShots() throws Exception {
		String className = this.getClass().getSimpleName();
		String methodName = name.getMethodName();
		log.info(className + "." + methodName + " is started.");

		String screenShotPath = EnvUtils.getScreenShotDirPath(className, methodName);

		File destDir = new File(screenShotPath);

		log.info("Delete ==>" + destDir.getAbsolutePath());
		FileUtils.deleteDirectory(destDir);
	}

	/**
	 * 後処理
	 *
	 * 一時作成した証跡ファイルをテスト証跡フォルダへ移動する。<br>
	 * また、Excelリポートファイルも作成する。
	 * @throws Exception
	 */
	@After
	public void moveScreenShots() throws Exception {

		Class<?> clazz = this.getClass();

		String className = clazz.getSimpleName();
		String methodName = name.getMethodName();
		log.info(className + "." + methodName + " is ended.");

		String srcDirStr = EnvUtils.getTempScreenshotDirPath();
		String descDirStr = EnvUtils.getScreenShotDirPath(className, methodName);

		File destDir = new File(descDirStr);

		if (!destDir.exists()) {
			destDir.mkdir();
		}

		File srcDir = new File(srcDirStr);

		File[] fileList = srcDir.listFiles();

		int idx = 0;
		for (File srcFile : fileList) {

			if (!srcFile.isFile()) {
				continue;
			}

			File destFile = new File(destDir, methodName + "_" + (++idx) + ".png");

			log.info(srcFile.getAbsolutePath() + "==>" + destFile.getAbsolutePath());
			FileUtils.moveFile(srcFile, destFile);
		}

		try {
			Reporter.makeReport(clazz, methodName);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 基底URLを取得する
	 *
	 */
	@Override
	public String getDefaultBaseUrl() {
		withDefaultPageWait(10, TimeUnit.SECONDS);
		withDefaultSearchWait(10, TimeUnit.SECONDS);

		String baseUrl = Env.value("url.default.base");
		return baseUrl;
	}

	/**
	 * デフォルトDriverを取得する
	 *
	 */
	@Override
	public WebDriver getDefaultDriver() {
		String iePath = Env.value("path.ie.driver");
		String logPath = Env.value("webdriver.ie.driver.logfile");

		System.setProperty("webdriver.ie.driver", iePath);
		System.setProperty("webdriver.ie.driver.loglevel", "WARN");
		System.setProperty("webdriver.ie.driver.logfile", logPath);

		WebDriver driver = new InternetExplorerDriver();

		driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);

		return driver;
	}

	/**
	 * 画面キャプチャ
	 * @return ファイルパス
	 */
	private String screenShot() {
		String filePath = EnvUtils.getTempScreenshotFilePath();
		takeScreenShot(filePath);

		return filePath;
	}

	/**
	 * 画面キャプチャ
	 * @param metaInfo	メタ情報
	 * @throws Exception
	 */
	protected void screenShot(Map<String, String> metaInfo) throws Exception {

		// 画面キャプチャ
		String filePath = screenShot();

		File imageFile = new File(filePath);

		for (Entry<String, String> entry : metaInfo.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();

			FileAttributeUtil.setAttribute(imageFile, key, value);
		}
	}

	/**
	 * 画面キャプチャ(コメント付き)
	 *
	 * @param comment	コメント
	 * @throws Exception
	 */
	protected void screenShot(String comment) throws Exception {

		Map<String, String> metaInfo = new HashMap<String, String>();
		metaInfo.put("comment", comment);

		screenShot(metaInfo);
	}
}
