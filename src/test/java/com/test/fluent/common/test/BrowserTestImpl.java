package com.test.fluent.common.test;

import org.fluentlenium.core.annotation.Page;

import com.test.fluent.common.test.page.CommonPage;
import com.test.fluent.common.test.page.CompanySelectPage;
import com.test.fluent.common.test.page.LoginPage;
import com.test.fluent.common.test.page.PageHeader;
import com.test.fluent.common.test.page.TPPage;

/**
 * テスト共通機能
 *
 * @author c.k.song
 *
 */
public class BrowserTestImpl extends BrowserTestBase {

	/** 共通ベース */
	@Page
	public CommonPage commonPage;

	/** ヘッダー */
	@Page
	public PageHeader header;

	/** ログインページ */
	@Page
	public LoginPage loginPage;

	/** 会社選択ページ */
	@Page
	public CompanySelectPage companySelectPage;

	/** メニューページ */
	@Page
	public TPPage tpPage;

	/**
	 * ログイン→トレジャリーポータル
	 * @param id			ID
	 * @param pw			パスワード
	 * @param companyName	会社名
	 * @throws Exception
	 */
	protected void loginToTP(String id, String pw, String companyName) throws Exception {

		// ログイン処理
		loginPage.login(id, pw);

		// 会社選択
		companySelectPage.select(companyName);
	}

	/**
	 * ログアウト
	 */
	protected void logout() {
		header.logout();
	}

	/**
	 * ログアウト
	 * @throws Exception
	 */
	protected void reLogin() throws Exception {
		header.reLogin();
	}

	/**
	 * 照会（screen shot）
	 * @throws Exception
	 */
	protected void view() throws Exception {
		commonPage.view();
	}
}
