package jp.co.sss.lms.ct.f03_report;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import jp.co.sss.lms.ct.util.WebDriverUtils;

/**
 * 結合テスト レポート機能
 * ケース09
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース09 受講生 レポート登録 入力チェック")
public class Case09 {

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {
		// ログイン画面表示確認
		webDriver.get("http://localhost:8080/lms/");
		assertEquals("ログイン | LMS", webDriver.getTitle());

		//待ち処理
		final WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(60));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginId")));

		//エビデンス取得
		WebDriverUtils.getEvidence(new Object() {
		}, "case09_01");
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {
		//DB登録済みのログインID入力、IDが正しく入力されていることを確認
		WebElement idElement = webDriver.findElement(By.id("loginId"));
		idElement.clear();
		idElement.sendKeys("StudentAB01");
		assertEquals("StudentAB01", idElement.getAttribute("value"), "IDで指定した要素の値が正しく入力されていること");

		//DB登録済みのパスワード入力、パスワードが正しく入力されていることを確認
		WebElement passIdElement = webDriver.findElement(By.id("password"));
		passIdElement.clear();
		passIdElement.sendKeys("StudentAB011");
		assertEquals("StudentAB011", passIdElement.getAttribute("value"), "IDで指定した要素の値が正しく入力されていること");

		//ログインボタンを取得しクリック
		WebElement btnCssElement = webDriver.findElement(By.cssSelector(".btn.btn-primary"));
		btnCssElement.click();

		//待ち処理
		final WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(60));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("active")));

		assertEquals("コース詳細 | LMS", webDriver.getTitle());

		//エビデンス取得
		WebDriverUtils.getEvidence(new Object() {
		}, "case09_02");
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test03() {
		WebElement userLinkElement = webDriver.findElement(By.linkText("ようこそ受講生ＡＢ１さん"));
		userLinkElement.click();

		assertEquals("ユーザー詳細", webDriver.getTitle());

		WebDriverUtils.getEvidence(new Object() {
		}, "case09_03");
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 該当レポートの「修正する」ボタンを押下しレポート登録画面に遷移")
	void test04() {
		List<WebElement> rows = webDriver.findElements(By.cssSelector("tr"));

		for (WebElement row : rows) {
			if (row.getText().contains("週報【デモ】")) {
				WebElement fixElement = row.findElement(By.cssSelector("input[value='修正する']"));

				((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView({block: 'center'});",
						fixElement);

				fixElement.click();
				break;
			}
		}

		//テキストエリアが表示されているか確認
		WebElement textAreaElement = webDriver.findElement(By.id("content_0"));
		assertTrue(textAreaElement.isDisplayed());

		WebDriverUtils.getEvidence(new Object() {
		}, "case09_04");
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しエラー表示：学習項目が未入力")
	void test05() {
		WebElement subjectElement = webDriver.findElement(By.id("intFieldName_0"));
		subjectElement.clear();

		//提出するボタン押下
		WebElement completeSubmitElement = webDriver.findElement(By.cssSelector(".btn.btn-primary"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", completeSubmitElement);

		completeSubmitElement.click();

		WebElement errorSubjectElement = webDriver.findElement(By.id("intFieldName_0"));
		assertTrue(errorSubjectElement.getAttribute("class").contains("errorInput"));

		WebDriverUtils.getEvidence(new Object() {
		}, "case09_05");
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：理解度が未入力")
	void test06() {
		WebElement subjectElement = webDriver.findElement(By.id("intFieldName_0"));
		subjectElement.clear();
		subjectElement.sendKeys("Selenium");

		// プルダウン要素を取得
		Select select = new Select(webDriver.findElement(By.id("intFieldValue_0")));
		select.selectByIndex(0);

		//提出するボタン押下
		WebElement completeSubmitElement = webDriver.findElement(By.cssSelector(".btn.btn-primary"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", completeSubmitElement);

		completeSubmitElement.click();

		WebElement errorSubjectElement = webDriver.findElement(By.id("intFieldValue_0"));
		assertTrue(errorSubjectElement.getAttribute("class").contains("errorInput"));

		WebDriverUtils.getEvidence(new Object() {
		}, "case09_06");
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度が数値以外")
	void test07() {
		Select select = new Select(webDriver.findElement(By.id("intFieldValue_0")));
		select.selectByIndex(2);

		WebElement achElement = webDriver.findElement(By.id("content_0"));
		achElement.clear();
		achElement.sendKeys("高");

		//提出するボタン押下
		WebElement completeSubmitElement = webDriver.findElement(By.cssSelector(".btn.btn-primary"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", completeSubmitElement);

		completeSubmitElement.click();

		WebElement errorSubjectElement = webDriver.findElement(By.id("content_0"));
		assertTrue(errorSubjectElement.getAttribute("class").contains("errorInput"));

		WebDriverUtils.getEvidence(new Object() {
		}, "case09_07");
	}

	@Test
	@Order(8)
	@DisplayName("テスト08 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度が範囲外")
	void test08() {

		WebElement achElement = webDriver.findElement(By.id("content_0"));
		achElement.clear();
		achElement.sendKeys("100");

		//提出するボタン押下
		WebElement completeSubmitElement = webDriver.findElement(By.cssSelector(".btn.btn-primary"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", completeSubmitElement);

		completeSubmitElement.click();

		WebElement errorSubjectElement = webDriver.findElement(By.id("content_0"));
		assertTrue(errorSubjectElement.getAttribute("class").contains("errorInput"));

		WebDriverUtils.getEvidence(new Object() {
		}, "case09_08");
	}

	@Test
	@Order(9)
	@DisplayName("テスト09 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度・所感が未入力")
	void test09() {
		WebElement achElement = webDriver.findElement(By.id("content_0"));
		achElement.clear();

		WebElement impElement = webDriver.findElement(By.id("content_1"));
		impElement.clear();

		//提出するボタン押下
		WebElement completeSubmitElement = webDriver.findElement(By.cssSelector(".btn.btn-primary"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", completeSubmitElement);

		completeSubmitElement.click();

		WebElement achErrorSubjectElement = webDriver.findElement(By.id("content_0"));
		WebElement impErrorSubjectElement = webDriver.findElement(By.id("content_1"));
		assertTrue(achErrorSubjectElement.getAttribute("class").contains("errorInput"));
		assertTrue(impErrorSubjectElement.getAttribute("class").contains("errorInput"));

		WebDriverUtils.getEvidence(new Object() {
		}, "case09_09");
	}

	@Test
	@Order(10)
	@DisplayName("テスト10 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：所感・一週間の振り返りが2000文字超")
	void test10() {
		WebElement achElement = webDriver.findElement(By.id("content_0"));
		achElement.clear();
		achElement.sendKeys("1");

		String Text = "あ".repeat(2001);

		WebElement impElement = webDriver.findElement(By.id("content_1"));
		impElement.clear();
		impElement.sendKeys(Text);

		WebElement rvwElement = webDriver.findElement(By.id("content_2"));
		rvwElement.clear();
		rvwElement.sendKeys(Text);

		//提出するボタン押下
		WebElement completeSubmitElement = webDriver.findElement(By.cssSelector(".btn.btn-primary"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", completeSubmitElement);

		completeSubmitElement.click();

		WebElement rvwErrorSubjectElement = webDriver.findElement(By.id("content_2"));
		WebElement impErrorSubjectElement = webDriver.findElement(By.id("content_1"));
		assertTrue(rvwErrorSubjectElement.getAttribute("class").contains("errorInput"));
		assertTrue(impErrorSubjectElement.getAttribute("class").contains("errorInput"));

		((JavascriptExecutor) webDriver).executeScript(
				"window.scrollTo(0, document.body.scrollHeight);");

		WebDriverUtils.getEvidence(new Object() {
		}, "case09_10");
	}

}
