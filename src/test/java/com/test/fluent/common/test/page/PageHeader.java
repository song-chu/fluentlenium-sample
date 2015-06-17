package com.test.fluent.common.test.page;

/**
 * 基底ページ
 *
 * @author c.k.song
 *
 */
public class PageHeader extends PageBase {

	/**
	 * ログアウト
	 */
	public void logout() {
		String selector = "div#globalMenuRight > ul > li#rMenuLogout";
		this.wait$Clickable(selector).click();
		waitAlertIsPresent(getDriver()).accept();
	}

	/**
	 * ログアウト
	 *
	 * @throws Exception
	 */
	public void reLogin() throws Exception {
		logout();

		String selector = "div#logout_footer > label";
		screenShot("再ログインはこちらをクリックする。");

		this.wait$Clickable(selector).click();
	}
}
