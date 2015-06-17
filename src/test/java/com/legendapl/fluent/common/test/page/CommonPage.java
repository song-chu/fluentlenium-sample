package com.legendapl.fluent.common.test.page;

import static org.fluentlenium.core.filter.FilterConstructor.*;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fluentlenium.core.domain.FluentWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * 基底ページ
 *
 * @author c.k.song
 *
 */
public class CommonPage extends PageBase {

	/** ログ */
	private static Log log = LogFactory.getLog(CommonPage.class);

	/**
	 * contents Frame選択
	 */
	protected void switchToContentsFrame() {
		WebDriver driver = untilFrameAvailable("contents");
		driver.switchTo().defaultContent();
		driver.switchTo().frame("businessframe");
		driver.switchTo().frame("contents");
	}

	/**
	 * businessframe Frame選択
	 */
	protected void switchToBusinessFrame() {
		WebDriver driver = untilFrameAvailable("businessframe");
		driver.switchTo().defaultContent();
		driver.switchTo().frame("businessframe");
	}

	/**
	 * ボタンをクリック→ダイアログYES→ダイアログOK
	 * 確認、保留、却下、取消など
	 * @param buttonName	ボタン名
	 * @throws Exception
	 */
	protected void clickBtnDialogYesOk(String buttonName) throws Exception {
		screenShot(buttonName + "をクリックする。");

		clickBtn(buttonName);

		dialogYesOk();
	}

	/**
	 * ボタンをクリックする
	 * @param buttonName	ボタン名
	 * @throws Exception
	 */
	protected void clickBtn(String buttonName) throws Exception {
		clickBtn(buttonName, 1);
	}

	/**
	 * ボタンをクリックする
	 * @param buttonName	ボタン名
	 * @param tryCount 		再実行数
	 * @throws Exception
	 */
	protected void clickBtn(String buttonName, int tryCount) throws Exception {

		String selector = "div[component='Button'] > span.ccm2-cmp-btn-text";

		List<WebElement> btnList = this.findElementsByCss(selector);

		for (WebElement btn : btnList) {

			String text = btn.getText();
			log.info("ボタン名=" + text);

			if (text.trim().equals(buttonName)) {

				if (!btn.isEnabled()) {
					throw new Exception("ボタンがクリックできません。(" + buttonName + ")");
				}

				btn.click();
				return;
			}
		}

		if (tryCount <= 3) {
			clickBtn(buttonName, ++tryCount);
		}

		throw new Exception("ボタンがありません。(" + buttonName + ")");
	}

	/**
	 * TP内のリンクをクリックする
	 * @param linkCss	リンクCSSセレクター
	 * @param scrollCss	スクロールが付いているエレメント
	 * @throws Exception
	 */
	protected void clickLinkInTP(String linkCss, String scrollCss) throws Exception {

		this.switchToContentsFrame();

		WebElement element = this.wait$Element(scrollCss);
		scroll(element);
		screenShot("スクロールし、リンクをクリックする。");

		WebElement link = this.wait$Element(linkCss);
		log.info("link.scrollTop=" + link.getAttribute("scrollTop"));
		log.info("link.scrollHeight=" + link.getAttribute("scrollHeight"));

		clickByJavascript(linkCss);
	}

	/**
	 * Dialog → YES → OK
	 * @throws Exception
	 */
	protected void dialogYesOk() throws Exception {

		FluentWebElement dialog = findFirst(".ui-dialog-buttonset");
		screenShot("YESをクリックする。");

		FluentWebElement yes = dialog.findFirst("button").findFirst(".ui-button-text", withText().contains("YES"));
		yes.click();

		dialog = findFirst(".ui-dialog-buttonset");
		screenShot("OKをクリックする。");

		FluentWebElement ok = dialog.findFirst("button").findFirst(".ui-button-text", withText().contains("OK"));
		ok.click();
	}

	/**
	 * 一覧に戻る
	 *
	 * @throws Exception
	 */
	public void backToList() throws Exception {
		this.clickBtn("一覧に戻る");
	}

	/**
	 * 照会のみ
	 * @throws Exception
	 */
	public void view() throws Exception {
		screenShot("画面が表示される。");
	}

	/**
	 * 照会のみ
	 *
	 * @param comment コメント
	 * @throws Exception
	 */
	public void view(String comment) throws Exception {
		screenShot(comment);
	}

	/**
	 * Sub Menuを選択する
	 *
	 * @param menuName メニュー名
	 * @throws Exception
	 */
	public void subMenu(String menuName) throws Exception {
		String childrenSelector = "ul.subMenuChildren > li.submenuChildrenItem.hasLinkPage";
		List<WebElement> children = this.findElementsByCss(childrenSelector);

		for (WebElement child : children) {
			String menu = getText(child);

			log.info("Sub Menu=" + menu);

			if (menu.equals(menuName)) {
				clickByJavascript(child);
				return;
			}
		}
	}

	/**
	 * 登録
	 * @throws Exception
	 */
	protected void insert() throws Exception {
		wait(1);

		// 登録
		this.clickBtn("登録");

		String selector = "div.ui-dialog-buttonset > button > span.ui-button-text";

		waitIsVisible(selector);

		FluentWebElement ok = findFirst(selector, withText().contains("OK"));

		String format = "登録ボタンクリック後、ダイアログにてOKをクリックする。";
		screenShot(String.format(format));

		ok.click();
	}

	/**
	 * 日付を設定する
	 * @param cssCalendar
	 * @param day
	 */
	protected void setDatePayDay(String cssCalendar, String day) {
		WebElement calendar = this.findElementByCss(cssCalendar);
		calendar.click();

		int yyyy = Integer.parseInt(day.substring(0, 4));
		int mm = Integer.parseInt(day.substring(4, 6));
		int dd = Integer.parseInt(day.substring(6, 8));

		String cssYear = "span[id^='calendarYearSelectList'][id$='-input-input-datePayDay_inpselectListTopCaption']";
		String cssMonth = "span[id^='calendarMonthSelectList'][id$='-input-input-datePayDay_inpselectListTopCaption']";

		moveUntilSameYearMonth(cssYear, cssMonth, yyyy, mm);

		String cssDays = "div.ccm2-cmp-date-picker-days > div.ccm2-cmp-date-picker-day";
		clickDay(cssDays, dd);

		findElementByCss("span#ok").click();
	}

	/**
	 * 指定した年月になるまで、カレンダーを移動する。
	 * @param cssYear	年Selector
	 * @param cssMonth	月Selector
	 * @param yyyy		年
	 * @param mm		月
	 */
	protected void moveUntilSameYearMonth(final String cssYear, final String cssMonth, int yyyy, int mm) {

		String cssPrev = "span#prev";
		String cssNext = "span#next";

		WebElement year = this.findElementByCss(cssYear);
		WebElement month = this.findElementByCss(cssMonth);

		String selectedYear = year.getText();
		String selectedMonth = month.getText();

		if (Integer.parseInt(selectedYear) < yyyy) {
			WebElement next = this.findElementByCss(cssNext);
			next.click();
			moveUntilSameYearMonth(cssYear, cssMonth, yyyy, mm);

		} else if (Integer.parseInt(selectedYear) > yyyy) {

			WebElement prev = this.findElementByCss(cssPrev);
			prev.click();
			moveUntilSameYearMonth(cssYear, cssMonth, yyyy, mm);
		} else {

			if (Integer.parseInt(selectedMonth) < mm) {
				WebElement next = this.findElementByCss(cssNext);
				next.click();
				moveUntilSameYearMonth(cssYear, cssMonth, yyyy, mm);

			} else if (Integer.parseInt(selectedYear) > yyyy) {
				WebElement prev = this.findElementByCss(cssPrev);
				prev.click();
				moveUntilSameYearMonth(cssYear, cssMonth, yyyy, mm);

			} else {
				return;
			}
		}
	}

	/**
	 * カレンダーにて日付をクリックする
	 * @param cssDays	日付Selector
	 * @param dd		日付
	 */
	protected void clickDay(String cssDays, int dd) {

		List<WebElement> days = this.findElementsByCss(cssDays);

		for (WebElement day : days) {
			WebElement span = this.findElementByCss(day, "span");

			String text = span.getText();

			if (StringUtils.isEmpty(text)) {
				continue;
			}

			if (Integer.parseInt(text) == dd) {
				span.click();
				return;
			}
		}
	}
}
