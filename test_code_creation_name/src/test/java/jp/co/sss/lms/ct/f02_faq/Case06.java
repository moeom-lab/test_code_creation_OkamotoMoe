package jp.co.sss.lms.ct.f02_faq;

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
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import jp.co.sss.lms.ct.util.WebDriverUtils;

/**
 * 結合テスト よくある質問機能
 * ケース06
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース06 カテゴリ検索 正常系")
public class Case06 {

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
		}, "case6_01");
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
		}, "case06_02");
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
		}, "case06_03");
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
		}, "case06_04");
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 カテゴリ検索で該当カテゴリの検索結果だけ表示")
	void test05() {
		WebElement categoryLinkElement = webDriver.findElement(By.linkText("【研修関係】"));
		categoryLinkElement.click();

		//検索結果取得
		List<WebElement> searchResultElements = webDriver.findElements(By.cssSelector("[id^='question-h']"));

		for (WebElement searchResultElement : searchResultElements) {
			assertTrue(searchResultElement.isDisplayed());
		}

		//検索結果がすべて表示されるようにブラウザサイズ調節
		webDriver.manage().window().setSize(new Dimension(1280, 1000));

		WebDriverUtils.getEvidence(new Object() {
		}, "case06_05");

	}

	@Test
	@Order(6)
	@DisplayName("テスト06 検索結果の質問をクリックしその回答を表示")
	void test06() {
		WebElement searchResultElement = webDriver.findElement(By.className("mb10"));
		searchResultElement.click();

		WebElement answerElement = webDriver.findElement(By.className("fs18"));
		assertTrue(answerElement.getText().contains("受講者の退職"));

		WebDriverUtils.getEvidence(new Object() {
		}, "case06_06");

	}

}
