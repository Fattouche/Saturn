package com.saturn;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.util.concurrent.TimeUnit;
import java.net.URL;
import java.util.Map;

import com.saturn.TestDeleteAccount;

import static org.junit.Assert.*;

public class TestVault {
	private TestHelper helper;
	public String siteName;

	@Before
	public void setUp() throws Exception {
		helper = new TestHelper();
		helper.driver.get(helper.url);
		siteName = "Site"+ RandomStringUtils.randomAlphanumeric(8);
	}

	@Test
	public void headerIsCorrect() throws Exception {
		assertEquals("SATURN SECURITY SYSTEM", helper.driver.findElement(By.tagName("h1")).getText().toUpperCase());
	}

	@Test
	public void logsUserWithCorrectCredentials() {
		assertFalse(helper.isElementPresent(By.id("account-menu"))); // starts logged out
		helper.login(helper.validUsername, helper.validPassword);
		assertTrue(helper.isElementPresent(By.id("account-menu")));
	}

	@Test
	public void doesNotLogUserWithIncorrectCredentials() {
		assertFalse(helper.isElementPresent(By.id("account-menu"))); // starts logged out
		helper.login(helper.validUsername, helper.validPassword+"toMakeInvalid");
		assertFalse(helper.isElementPresent(By.id("account-menu")));
	}

	@Test
	public void createSaturnVaultAccountTest() {
		helper.login();
		helper.createSaturnVaultAccount(siteName);

		//after creation ensure the account is in the list
		new WebDriverWait(helper.driver, 3).until(ExpectedConditions.visibilityOfElementLocated(By.id("#site-" + siteName)));
		WebElement siteNameCell = helper.driver.findElement(By.id("#site-" + siteName));
		assertEquals(siteName, siteNameCell.getText());
	}

	@Test
	public void cancelSaturnVaultAccountCreationTest() {
		helper.login();

		String cancelledSiteName = "Site"+ RandomStringUtils.randomAlphanumeric(12);

		helper.driver.get(helper.url+"/#/saturn-vault/new");

		helper.driver.findElement(By.id("field_site")).sendKeys(cancelledSiteName);
		helper.driver.findElement(By.id("field_login")).sendKeys("test_user");
		helper.driver.findElement(By.id("field_password")).sendKeys("test_password");

		helper.driver.findElement(By.cssSelector(".cancel-button")).click();

		// Assert that password account was not created
		assertFalse(helper.isElementPresent(By.cssSelector("#site-" + cancelledSiteName)));
	}

	@Test
	public void saturnVaultInvalidAccountCreationTest() {
		helper.login();

		String cancelledSiteName = "Site"+ RandomStringUtils.randomAlphanumeric(12);

		helper.driver.get(helper.url+"/#/saturn-vault/new");

		helper.driver.findElement(By.id("field_site")).sendKeys(cancelledSiteName);
		// Do not put a login
		helper.driver.findElement(By.id("field_password")).sendKeys("test_password");

		// Assert that submit is disabled
		assertTrue(helper.isElementPresent(By.cssSelector("button[disabled=disabled][type=submit]")));
	}
	
	@After
	public void tearDown() throws Exception {
		if(helper.isElementPresent(By.cssSelector("#site-" + siteName))){
			helper.deleteSaturnVaultAccount(siteName, false);
		}
		helper.tearDown();
	}
}
