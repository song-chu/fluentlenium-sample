package com.legendapl.fluent.common.test.page;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fluentlenium.core.FluentPage;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.legendapl.fluent.common.util.EnvUtils;
import com.legendapl.fluent.common.util.FileAttributeUtil;

/**
 * 基底ページ
 *
 * @author c.k.song
 *
 */
public class PageBase extends FluentPage {

	/** ログ */
	private static Log log = LogFactory.getLog(PageBase.class);

	/**
	 * 指定秒まで待機
	 * @param seconds	秒
	 */
	protected void wait(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Alertが表示されるまで待機
	 * @param driver	Driver
	 * @return Alert
	 */
	protected Alert waitAlertIsPresent(WebDriver driver) {
		long before = System.currentTimeMillis();

		WebDriverWait wait = new WebDriverWait(driver, 30);
		Alert alert = wait.until(ExpectedConditions.alertIsPresent());

		long after = System.currentTimeMillis();
		log.info("waitUtilAlertPresent=" + (after - before) + "ms");

		return alert;
	}

	/**
	 * CSSのエレメントが表示されるまで待機
	 * @param selector	CSS Selector
	 * @return エレメント
	 */
	protected WebElement waitIsPresent(final String selector) {
		return wait$Element(30, selector);
	}

	/**
	 * CSSのエレメントが表示されるまで待機して、取得
	 * @param seconds	Max待機時間
	 * @param selector	CSS Selector
	 * @return エレメント
	 */
	private WebElement wait$Element(final long seconds, final String selector) {

		long before = System.currentTimeMillis();

		Wait<WebDriver> wait = new WebDriverWait(getDriver(), seconds);
		ExpectedCondition<WebElement> condition = new ExpectedCondition<WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.cssSelector(selector));
			}
		};

		WebElement element = wait.until(condition);

		log.info(selector + ":::wait$Element=" + (System.currentTimeMillis() - before) + "ms");

		return element;
	}

	/**
	 * CSSのエレメントがロード、表示されるまで待機して、取得
	 * @param selector	CSS Selector
	 * @return エレメント
	 */
	protected WebElement waitIsVisible(final String selector) {
		return waitIsVisible(30, selector);
	}

	/**
	 * CSSのエレメントがロード、表示されるまで待機して、取得
	 * @param seconds	Max待機時間
	 * @param selector	CSS Selector
	 * @return エレメント
	 */
	protected WebElement waitIsVisible(final long seconds, final String selector) {

		long before = System.currentTimeMillis();

		Wait<WebDriver> wait = new WebDriverWait(getDriver(), seconds);

		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(selector)));

		log.info(selector + ":::waitIsLoaded=" + (System.currentTimeMillis() - before) + "ms");

		return element;
	}

	/**
	 * CSSのエレメントが表示されるまで待機して、取得
	 * @param seconds	Max待機時間
	 * @param selector	CSS Selector
	 * @return エレメントリスト
	 */
	protected List<WebElement> wait$Elements(final long seconds, final String selector) {
		long before = System.currentTimeMillis();

		Wait<WebDriver> wait = new WebDriverWait(getDriver(), seconds);
		ExpectedCondition<List<WebElement>> condition = new ExpectedCondition<List<WebElement>>() {
			public List<WebElement> apply(WebDriver driver) {
				return driver.findElements(By.cssSelector(selector));
			}
		};

		List<WebElement> elements = wait.until(condition);

		log.info(selector + ":::wait$Elements=" + (System.currentTimeMillis() - before) + "ms");
		return elements;
	}

	/**
	 * CSSのエレメントが表示されるまで待機して、取得
	 * @param selector	CSS Selector
	 * @return エレメントリスト
	 */
	protected List<WebElement> wait$Elements(final String selector) {
		return wait$Elements(30, selector);
	}

	/**
	 * 基底elementCSSのエレメントが表示されるまで待機し、取得
	 * @param element	基底element
	 * @param seconds	Max待機時間
	 * @param selector	CSS Selector
	 * @return element
	 */
	protected WebElement wait$Element(final WebElement element, final long seconds, final String selector) {
		long before = System.currentTimeMillis();

		Wait<WebDriver> wait = new WebDriverWait(getDriver(), seconds);
		ExpectedCondition<WebElement> condition = new ExpectedCondition<WebElement>() {
			public WebElement apply(WebDriver driver) {
				return element.findElement(By.cssSelector(selector));
			}
		};

		WebElement target = wait.until(condition);

		log.info(selector + ":::wait$Element=" + (System.currentTimeMillis() - before) + "ms");
		return target;
	}

	/**
	 * 基底elementCSSのエレメントが表示されるまで待機し、取得
	 * @param element	基底element
	 * @param seconds	Max待機時間
	 * @param selector	CSS Selector
	 * @return element
	 */
	protected List<WebElement> wait$Elements(final WebElement element, final long seconds, final String selector) {
		long before = System.currentTimeMillis();

		Wait<WebDriver> wait = new WebDriverWait(getDriver(), seconds);
		ExpectedCondition<List<WebElement>> condition = new ExpectedCondition<List<WebElement>>() {
			public List<WebElement> apply(WebDriver driver) {
				return element.findElements(By.cssSelector(selector));
			}
		};

		List<WebElement> targets = wait.until(condition);
		log.info(selector + ":::wait$Element=" + (System.currentTimeMillis() - before) + "ms");
		return targets;
	}

	/**
	 * 基底elementCSSのエレメントが表示されるまで待機し、取得
	 * @param element	基底element
	 * @param selector	CSS Selector
	 * @return element
	 */
	protected List<WebElement> wait$Elements(final WebElement element, final String selector) {
		return wait$Elements(element, 30, selector);
	}

	/**
	 * 基底elementCSSのエレメントが表示されるまで待機し、取得
	 * @param element	基底element
	 * @param selector	CSS Selector
	 * @return element
	 */
	protected WebElement wait$Element(final WebElement element, final String selector) {
		return wait$Element(element, 30, selector);
	}

	/**
	 * CSSのエレメントが表示されるまで待機
	 * @param selector	CSS Selector
	 * @return element
	 */
	protected WebElement wait$Element(String selector) {
		return wait$Element(30, selector);
	}

	/**
	 * HTML要素がクリック可能になるまで待機
	 * @param selector	CSS Selector
	 * @return element
	 */
	protected WebElement wait$Clickable(String selector) {
		long before = System.currentTimeMillis();

		WebDriverWait wait = (WebDriverWait)new WebDriverWait(getDriver(), 30)
				.ignoring(StaleElementReferenceException.class);
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(selector)));

		log.info(selector + ":::untilClickable=" + (System.currentTimeMillis() - before) + "ms");
		return element;
	}

	/**
	 * フレームが有効になるまで待機
	 * @param frameName	フレーム名
	 * @return WebDriver
	 */
	protected WebDriver untilFrameAvailable(String frameName) {
		long before = System.currentTimeMillis();
		WebDriverWait wait = new WebDriverWait(getDriver(), 30);

		WebDriver driver = wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameName));
		log.info(frameName + ":::untilFrameAvailable=" + (System.currentTimeMillis() - before) + "ms");

		return driver;
	}

	/**
	 * CSSによるElement存在有無確認
	 * @param selector	CSS Selector
	 * @return true あり
	 */
	protected boolean has(String selector) {
		return !getDriver().findElements(By.cssSelector(selector)).isEmpty();
	}

	/**
	 * CSSによるElement存在有無確認
	 * @param selector	CSS Selector
	 * @return true なし
	 */
	protected boolean isEmpty(String selector) {
		return !has(selector);
	}

	/**
	 * 検索基底エレメントからCSSによるElement存在有無確認
	 * @param element	検索基底エレメント
	 * @param selector	CSS Selector
	 * @return true あり
	 */
	protected boolean has(WebElement element, String selector) {
		return !element.findElements(By.cssSelector(selector)).isEmpty();
	}

	/**
	 * 検索基底エレメントからCSSによるElement存在有無確認
	 * @param element	検索基底エレメント
	 * @param selector	CSS Selector
	 * @return true なし
	 */
	protected boolean isEmpty(WebElement element, String selector) {
		return !has(element, selector);
	}

	/**
	 * CSSによるElement検索
	 * @param selector	CSS selector
	 * @return WebElement
	 */
	protected WebElement findElementByCss(String selector) {
		return wait$Element(selector);
		//		return getDriver().findElement(By.cssSelector(selector));
	}

	/**
	 * 指定エレメントからCSSによるElement検索
	 * @param element	エレメント
	 * @param selector	CSS selector
	 * @return WebElement
	 */
	protected WebElement findElementByCss(WebElement element, String selector) {
		return wait$Element(element, selector);
		//		return element.findElement(By.cssSelector(css));
	}

	/**
	 * CSSによる複数Element検索
	 * @param selector	CSS selector
	 * @return	複数Element
	 */
	protected List<WebElement> findElementsByCss(String selector) {
		return wait$Elements(selector);
	}

	/**
	 * 指定エレメントからCSSによる複数Element検索
	 * @param element	エレメント
	 * @param selector	CSS
	 * @return	複数Element
	 */
	protected List<WebElement> findElementsByCss(WebElement element, String selector) {
		return wait$Elements(element, selector);
	}

	/**
	 * CSSによる複数Element検索
	 * @param seconds	Max待機時間
	 * @param selector	CSS Selector
	 * @return	複数Element
	 */
	protected List<WebElement> findElementsByCss(long seconds, String selector) {
		return wait$Elements(seconds, selector);
	}

	/**
	 *  CSSによるElement検索、クリック
	 * @param css	CSS Selector
	 */
	protected void clickElementByCss(String css) {
		clickByJavascript(css);
		//		WebElement element = findElementByCss(css);
		//
		//		if (element.isEnabled()) {
		//			element.click();
		//		}
	}

	/**
	 * 指定検索方法でElement検索
	 * @param by		検索方法
	 * @param timeOut	タイムアウト
	 * @return	Element
	 */
	protected WebElement findElementUntil(By by, int timeOut) {
		WebDriverWait wait = new WebDriverWait(getDriver(), timeOut);
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		return element;
	}

	/**
	 * 指定検索方法でElement検索
	 * @param by		検索方法
	 * @return	Element
	 */
	protected WebElement findElementBy(By by) {
		return findElementUntil(by, 30);
	}

	/**
	 * 画面キャプチャ
	 * @return ファイルパス
	 */
	private String screenShot() {
		wait(1);

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

	/**
	 * JavaScriptを利用してCSS Selectorで探したエレメントをクリックする。
	 * @param selector	CSS Selector
	 */
	protected void clickByJavascript(String selector) {
		String script = "var x = document.querySelector(\"" + selector + "\"); x.click();";
		((JavascriptExecutor)getDriver()).executeScript(script);
	}

	/**
	 * JavaScriptを利用してエレメントをクリックする。
	 * @param element
	 */
	protected void clickByJavascript(WebElement element) {
		String script = "arguments[0].click();";
		((JavascriptExecutor)getDriver()).executeScript(script, element);
	}

	/**
	 * JavaScriptを利用してエレメントにblurイベントを発生する。
	 * @param element
	 */
	protected void blurByJavascript(WebElement element) {
		String script = "arguments[0].blur();";
		((JavascriptExecutor)getDriver()).executeScript(script, element);
	}

	/**
	 * JavaScriptを利用してエレメントにblurイベントを発生する。
	 * @param selector
	 */
	protected void blurByJavascript(String selector) {
		String script = "var x = document.querySelector(\"" + selector + "\"); x.blur();";
		((JavascriptExecutor)getDriver()).executeScript(script);
	}

	/**
	 * スクロールが付いているエレメントの縦スクロールを底に移動
	 * @param element	エレメント
	 */
	protected void scroll(WebElement element) {
		String script = "arguments[0].scrollTop += arguments[0].scrollHeight;";
		((JavascriptExecutor)getDriver()).executeScript(script, element);
	}

	/**
	 * スクロールが付いているエレメントの縦スクロールを底に移動
	 * @param element	エレメント
	 */
	protected void scrollLeft(WebElement element, int x) {
		String script = "arguments[0].scrollLeft = arguments[1];";
		((JavascriptExecutor)getDriver()).executeScript(script, element, x);
	}

	/**
	 * 縦スクロールを指定位置に移動
	 * @param element	エレメント
	 * @param scrollTop	縦位置
	 */
	protected void scrollTo(WebElement element, int scrollTop) {
		String script = "arguments[0].scrollTop += arguments[1];";
		((JavascriptExecutor)getDriver()).executeScript(script, element, scrollTop);
	}

	/**
	 *
	 * @param element
	 * @return
	 */
	protected String getText(WebElement element) {
		String script = "return arguments[0].innerHTML;";
		return (String)((JavascriptExecutor)getDriver()).executeScript(script, element);
	}

	/**
	 * 入力後、blurイベント発生
	 * @param fillSelector	入力項目
	 * @param fillValue		入力値
	 */
	protected void fill$blur(String fillSelector, String fillValue) {
		fill(fillSelector).with(fillValue);
		this.blurByJavascript(fillSelector);
	}

	/**
	 * リストから値が一致する要素をクリックする
	 * @param selector	リストselector
	 * @param key		検索キー
	 */
	protected void selectFromList(String selector, String key) {

		List<WebElement> list = this.findElementsByCss(selector);

		for (WebElement item : list) {

			String text = item.getText();

			log.info("key=" + key + ",text=" + text + "<");

			if (text.trim().equals(key)) {
				item.click();
				return;
			}
		}
	}
}
