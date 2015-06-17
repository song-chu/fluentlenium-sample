package com.legendapl.fluent.common.test.page;

/**
 * ログインページ
 *
 * @author c.k.song
 *
 */
public class LoginPage extends PageBase {

	@Override
	public String getUrl() {
		return "";
	}

	/**
	 * ログイン
	 * @param id			ID
	 * @param pw			パスワード
	 */
	public void login(String id, String pw) throws Exception {

		wait(1);

		fill("#id_text").with(id);
		fill("#pass_text").with(pw);

		String format = "UserIDに%sを、Passwordに%s入力し、Loginボタンをクリックする";
		screenShot(String.format(format, id, pw));
		wait$Clickable("button[type='submit']").click();
	}
}
