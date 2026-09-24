package jp.co.sss.lms.ct.f02_faq;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

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
 * 結合テスト よくある質問機能
 * ケース05
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース05 キーワード検索 正常系")
public class Case05 {

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
		}, "case5_01");
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

		assertEquals("コース詳細 | LMS", webDriver.getTitle());

		//エビデンス取得
		WebDriverUtils.getEvidence(new Object() {
		}, "case05_02");
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「ヘルプ」リンクからヘルプ画面に遷移")
	void test03() {
		//機能(ドロップダウン)を開く
		WebElement LinkElement = webDriver.findElement(By.className("dropdown-toggle"));
		LinkElement.click();

		WebElement helpLinkElement = webDriver.findElement(By.linkText("ヘルプ"));

		assertTrue(helpLinkElement.getAttribute("href").endsWith("/help"), "ヘルプ画面へのリンク先が正しいこと");

		helpLinkElement.click();

		assertEquals("ヘルプ | LMS", webDriver.getTitle());

		WebDriverUtils.getEvidence(new Object() {
		}, "case05_03");
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「よくある質問」リンクからよくある質問画面を別タブに開く")
	void test04() {
		WebElement questionLinkElement = webDriver.findElement(By.linkText("よくある質問"));
		questionLinkElement.click();

		//新しく開いたタブを含むウィンドウハンドル取得
		Object[] windowHandles = webDriver.getWindowHandles().toArray();
		//新しく開いたタブへ切り替え
		webDriver.switchTo().window((String) windowHandles[1]);

		assertEquals("よくある質問 | LMS", webDriver.getTitle());

		WebDriverUtils.getEvidence(new Object() {
		}, "case05_04");
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 キーワード検索で該当キーワードを含む検索結果だけ表示")
	void test05() {
		String keyword = "助成金";
		WebElement formElement = webDriver.findElement(By.id("form"));
		formElement.clear();
		formElement.sendKeys(keyword);
		assertEquals(keyword, formElement.getAttribute("value"), "IDで指定した要素の値が正しく入力されていること");

		WebElement searchBtnElement = webDriver.findElement(By.cssSelector("input[value='検索']"));
		searchBtnElement.click();

		//検索結果取得、入力値が含まれているか確認
		WebElement searchResultElement = webDriver.findElement(By.id("question-h[${status.index}]"));
		assertTrue(searchResultElement.getText().contains(keyword));

		WebDriverUtils.getEvidence(new Object() {
		}, "case05_05");

	}

	@Test
	@Order(6)
	@DisplayName("テスト06 「クリア」ボタン押下で入力したキーワードを消去")
	void test06() {
		WebElement clearBtnElement = webDriver.findElement(By.cssSelector("input[value='クリア']"));
		clearBtnElement.click();

		//検索欄取得、空文字確認
		WebElement formElement = webDriver.findElement(By.id("form"));
		assertEquals("", formElement.getAttribute("value"));

		WebDriverUtils.getEvidence(new Object() {
		}, "case05_06");

	}

}
