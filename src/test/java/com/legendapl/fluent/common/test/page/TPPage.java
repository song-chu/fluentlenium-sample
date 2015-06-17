package com.legendapl.fluent.common.test.page;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

/**
 * TPページ
 *
 * @author la_song
 *
 */
public class TPPage extends CommonPage {

	/** ログ */
	private static Log log = LogFactory.getLog(TPPage.class);

	/**
	 * メニューをクリックする
	 * @param menuName	メニュー名
	 * @throws Exception
	 */
	public void menu(String menuName) throws Exception {
		wait(1);

		// フレーム切り替え
		this.switchToContentsFrame();

		waitIsVisible("div#tpMenuSearchResultListArea");

		// メニューリストを取得する
		String cssMenuList = "div.tpMenuSearchResultRecordDiv.tpOnSelectedBGColor";
		List<WebElement> list = this.findElementsByCss(cssMenuList);
		log.info("メニューリスト数=" + list.size());

		int height = 0;
		String cssSelector = "";

		for (WebElement row : list) {

			int elementHeight = row.getSize().getHeight() + 3;	// 行の縦サイズ（margin=3）

			// メニューリンクを取得
			String subCss = "div.tpMenuSearchResultFuncCaptionDiv > span.tpLink";
			WebElement menu = this.findElementByCss(row, subCss);
			String text = menu.getText();

			if (text.equals(menuName)) {

				// メニューのCSSセレクターを完成する
				String format = "#%s > div.tpMenuSearchResultFuncCaptionDiv > span.tpLink";
				cssSelector = String.format(format, row.getAttribute("id"));
				break;
			}

			height += elementHeight;
		}

		if (cssSelector.isEmpty()) {
			throw new Exception("メニューが存在しません。(" + menuName + ")");
		}

		log.info(menuName + "=" + height);
		log.info(menuName + "=" + cssSelector);

		// メニューエーリアをスクロールする
		this.scrollTo(this.findElementByCss("div#TpGadgetMenuMain"), height);
		String format = "MENUから%sをクリックする";
		screenShot(String.format(format, menuName));

		clickByJavascript(cssSelector);
	}

	/**
	 * インフォメーションをクリックする
	 * @param msg	インフォメーションのメッセージ
	 * @throws Exception
	 */
	public void information(String msg) throws Exception {
		wait(1);

		// フレーム切り替え
		this.switchToContentsFrame();

		while (true) {

			// 一致するインフォメーションを取得する
			String cssSelector = getInformationCss(msg);

			// 一致するインフォメーションがある場合
			if (cssSelector != null) {
				clickByJavascript(cssSelector);
				return;
			}

			// インフォメーションが存在しない、かつ次のx件表示リンクがあれば
			if (hasNext()) {
				// 次のx件表示
				next();
			} else {
				throw new Exception("インフォメーションが存在しません。(" + msg + ")");
			}
		}
	}

	/**
	 * 次のx件表示リンクがあるか
	 * @return true ある
	 */
	private boolean hasNext() {
		return findFirst("span.tpInfoNextLink").isDisplayed();
	}

	/**
	 * 次のx件表示
	 */
	private void next() {
		clickByJavascript("span.tpInfoNextLink");
	}

	/**
	 * インフォメーションを検索する
	 * @param msg	メッセージ
	 * @return	インフォメーションのCSSセレクター
	 * @throws Exception
	 */
	private String getInformationCss(String msg) throws Exception {

		String cssSelector = null;

		try {
			waitIsVisible("div#TpGadgetInformationMain");

			// インフォメーションリストを取得する
			String rowListSelector = "div.tpInfoMessageRecord.tpOnSelectedBGColor.font11";
			List<WebElement> list = this.findElementsByCss(rowListSelector);
			log.info("インフォメーションリスト数=" + list.size());

			int height = 0;
			cssSelector = null;

			for (WebElement row : list) {

				int elementHeight = row.getSize().getHeight();	// 行の縦サイズ

				// インフォメーションリンクを取得
				String subCss = "div.tpInfoMessageDiv.ccmLabel > span.tpLink";
				WebElement menu = this.findElementByCss(row, subCss);
				String text = menu.getText();
				log.info(text);

				if (text.startsWith(msg)) {
					// インフォメーションのCSSセレクターを完成する
					String format = "div#%s > div.tpInfoMessageDiv > span.tpLink";
					cssSelector = String.format(format, row.getAttribute("id"));

					// インフォメーションエーリアをスクロールする
					this.scrollTo(this.findElementByCss("div.tpInfoListMainDiv"), height);
					screenShot(String.format("Informationから%sをクリックする", text));

					return cssSelector;
				}

				height += elementHeight;
			}
		} catch (StaleElementReferenceException e) {
			log.error(e.getMessage());
			getInformationCss(msg);
		}

		return cssSelector;
	}

}
