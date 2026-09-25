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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import jp.co.sss.lms.ct.util.WebDriverUtils;

/**
 * 結合テスト レポート機能
 * ケース08
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース08 受講生 レポート修正(週報) 正常系")
public class Case08 {

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
		}, "case08_01");
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
		}, "case08_02");
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 提出済の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {
		assertEquals("提出済み", webDriver.findElement(By.className("w10per")).getText());

		//詳細ボタン押下
		WebElement detailBtnElement = webDriver.findElement(By.cssSelector("input[value='詳細']"));
		detailBtnElement.click();

		//待ち処理
		final WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(60));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sectionDetail")));

		assertEquals("セクション詳細 | LMS", webDriver.getTitle());

		WebDriverUtils.getEvidence(new Object() {
		}, "case08_03");

	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「確認する」ボタンを押下しレポート登録画面に遷移")
	void test04() {
		//確認するボタン押下
		WebElement comfirmElement = webDriver.findElement(By.cssSelector("input[value='提出済み日報【デモ】を確認する']"));
		comfirmElement.click();

		//待ち処理
		final WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(60));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("content_0")));

		assertEquals("レポート登録 | LMS", webDriver.getTitle());

		WebDriverUtils.getEvidence(new Object() {
		}, "case08_04");
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しセクション詳細画面に遷移")
	void test05() {
		//テキストエリア取得・入力
		WebElement textAreaElement = webDriver.findElement(By.id("content_0"));
		textAreaElement.clear();
		textAreaElement.sendKeys("修正済みの日報");

		//提出するボタン押下
		WebElement completeSubmitElement = webDriver.findElement(By.cssSelector(".btn.btn-primary"));
		completeSubmitElement.click();

		//待ち処理
		final WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(60));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sectionDetail")));

		WebDriverUtils.getEvidence(new Object() {
		}, "case08_05");
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test06() {
		WebElement userLinkElement = webDriver.findElement(By.linkText("ようこそ受講生ＡＢ１さん"));
		userLinkElement.click();

		assertEquals("ユーザー詳細", webDriver.getTitle());

		WebDriverUtils.getEvidence(new Object() {
		}, "case08_06");
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 該当レポートの「詳細」ボタンを押下しレポート詳細画面で修正内容が反映される")
	void test07() {
		WebElement detailElement = webDriver.findElement(By.cssSelector("input[value='詳細']"));
		detailElement.click();

		//修正した文言になっているか確認
		List<WebElement> confirmTextElement = webDriver.findElements(By.cssSelector(".table.table-hover td"));
		assertEquals("修正済みの日報", confirmTextElement.get(1).getText());

		WebDriverUtils.getEvidence(new Object() {
		}, "case08_07");

	}

}
