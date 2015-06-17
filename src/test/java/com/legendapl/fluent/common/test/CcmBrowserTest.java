package com.legendapl.fluent.common.test;

import org.fluentlenium.core.annotation.Page;

import com.legendapl.fluent.common.test.page.CcmPageBase;
import com.legendapl.fluent.common.test.page.CompanySelectPage;
import com.legendapl.fluent.common.test.page.LoginPage;
import com.legendapl.fluent.common.test.page.PageBase;
import com.legendapl.fluent.common.test.page.PageHeader;
import com.legendapl.fluent.common.test.page.TPPage;

/**
 * 承認WFシナリオ
 *
 * @author la_song
 *
 */
public class CcmBrowserTest extends BrowserTestBase {

	/** ベース */
	@Page
	public PageBase pageBase;

	/** Ccmベース */
	@Page
	public CcmPageBase ccmPageBase;

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
		ccmPageBase.view();
	}
}
