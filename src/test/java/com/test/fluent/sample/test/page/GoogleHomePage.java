package com.test.fluent.sample.test.page;

import com.test.fluent.common.test.page.PageBase;

/**
 * ログインページ
 *
 * @author c.k.song
 *
 */
public class GoogleHomePage extends PageBase {

	@Override
	public String getUrl() {
		return "";
	}

	/**
	 * Google検索
	 * @param keyword	キーワード
	 * @throws Exception
	 */
	public void search(String keyword) throws Exception {

		screenShot("goole.co.jpへアクセスする。");

		fill("input[name='q']").with(keyword);

		screenShot(String.format("検索キーワードに「%s」を入力して、Google検索ボタンをクリックする。", keyword));

		click("input[name='btnG']");
	}
}
