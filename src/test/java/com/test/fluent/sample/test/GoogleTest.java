package com.test.fluent.sample.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fluentlenium.core.annotation.Page;
import org.junit.Test;

import com.test.fluent.common.annotation.ClassComment;
import com.test.fluent.common.annotation.MethodComment;
import com.test.fluent.common.test.BrowserTestBase;
import com.test.fluent.sample.test.page.GoogleHomePage;

/**
 * 承認WFシナリオ
 *
 * @author la_song
 *
 */
@ClassComment(reportFileName = "GoogleTest.xls", value = {})
public class GoogleTest extends BrowserTestBase {

	/** ログ */
	private static Log log = LogFactory.getLog(GoogleTest.class);

	/** Googleページ */
	@Page
	public GoogleHomePage googleHomeBase;

	/**
	 * 簡易テスト
	 * メニュー選択まで
	 */
	@MethodComment({
			"■テスト目的：Fluentlenium Sample",
			"■テスト日時：[SYSDATE]",
			"■テストメソッド:[METHODNAME]",
			"■テスト手順：",
			"    1.goole.co.jpへアクセスする。",
			"    2.検索キーワードに「」を入力する。",
			"    3.検索結果から最初のWEBページにアクセスする。" })
	@Test
	public void test001() {

		try {
			goTo("https://www.google.co.jp");

			googleHomeBase.search("日本酒");

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}
}
