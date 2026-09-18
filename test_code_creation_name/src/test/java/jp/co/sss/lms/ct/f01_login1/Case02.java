package jp.co.sss.lms.ct.f01_login1;

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
 * 結合テスト ログイン機能①
 * ケース02
 * @author 岡本もえ
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース02 受講生 ログイン 認証失敗")
public class Case02 {

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

	/** Test01 */
	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {
		// ログイン画面表示確認
		webDriver.get("http://localhost:8080/lms/");

		WebElement cssElement = webDriver.findElement(By.cssSelector(".col-lg-2.control-label"));
		assertEquals("ログインID", cssElement.getText(), "ログインIDが画面に表示されていること");

		//待ち処理
		final WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(60));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginId")));

		//エビデンス取得
		WebDriverUtils.getEvidence(new Object() {
		}, "case1");
	}

	/** Test02 */
	@Test
	@Order(2)
	@DisplayName("テスト02 DBに登録されていないユーザーでログイン")
	void test02() {
		//DB未登録のログインID入力、IDが正しく入力されていることを確認
		WebElement idElement = webDriver.findElement(By.id("loginId"));
		idElement.clear();
		idElement.sendKeys("StudentABC01");
		assertEquals("StudentABC01", idElement.getAttribute("value"), "IDで指定した要素の値が正しく入力されていること");

		//DB未登録のパスワード入力、パスワードが正しく入力されていることを確認
		WebElement passIdElement = webDriver.findElement(By.id("password"));
		passIdElement.clear();
		passIdElement.sendKeys("StudentABC01");
		assertEquals("StudentABC01", passIdElement.getAttribute("value"), "IDで指定した要素の値が正しく入力されていること");

		//ログインボタンを取得しクリック
		WebElement btnCssElement = webDriver.findElement(By.cssSelector(".btn.btn-primary"));
		btnCssElement.click();

		//待ち処理
		final WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(60));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".help-inline.error")));

		//ログイン失敗時のエラーメッセージを取得
		WebElement errorClassElement = webDriver.findElement(By.cssSelector(".help-inline.error"));
		assertEquals("* ログインに失敗しました。", errorClassElement.getText(), "エラーメッセージが正しいこと");

		//エビデンス取得
		WebDriverUtils.getEvidence(new Object() {
		}, "case02_02");

	}

}
