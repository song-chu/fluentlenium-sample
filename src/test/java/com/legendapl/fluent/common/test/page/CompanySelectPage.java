package com.legendapl.fluent.common.test.page;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebElement;

/**
 * 会社選択
 *
 * @author la_song
 *
 */
public class CompanySelectPage extends CcmPageBase {

	/** ログ */
	private static Log log = LogFactory.getLog(CompanySelectPage.class);

	@Override
	public String getUrl() {
		return "";
	}

	/**
	 * 会社選択
	 * @param companyName	会社名
	 * @throws Exception
	 */
	public void select(String companyName) throws Exception {
		wait(1);

		this.switchToBusinessFrame();

		String selector = "div#corpsel_box_inner > table > tbody > tr";

		List<WebElement> trs = this.findElementsByCss(selector);

		int companyCount = 0;

		for (WebElement tr : trs) {

			List<WebElement> tds = this.findElementsByCss(tr, "td");

			for (WebElement td : tds) {

				if (isEmpty(td, "input[type='checkbox']")) {
					break;
				}

				WebElement checkBox = this.findElementByCss(td, "input[type='checkbox']");
				WebElement label = this.findElementByCss(td, "label");

				String text = label.getText();
				log.info("会社名=" + text + ", checked=" + checkBox.isSelected());

				if (text.startsWith(companyName)) {

					companyCount++;

					if (!checkBox.isSelected()) {
						checkBox.click();
					}
				} else {

					if (checkBox.isSelected()) {
						checkBox.click();
					}
				}
			}

			if (companyCount > 0) {
				break;
			}
		}

		if (companyCount == 0) {
			throw new Exception("会社が存在しません。(" + companyName + ")");
		}

		String format = "会社%sを選択し、Nextボタンをクリックする";
		screenShot(String.format(format, companyName));

		this.clickByJavascript("button[type='submit']");
	}
}
