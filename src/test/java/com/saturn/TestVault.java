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

import java.io.File;
import java.util.concurrent.TimeUnit;
import java.net.URL;
import java.util.Map;

import static org.junit.Assert.*;

public class TestVault {
	private TestHelper helper;

	@Before
	public void setUp() throws Exception {
		helper = new TestHelper();
		helper.driver.get(helper.url);
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
		String siteName = "Site"+ RandomStringUtils.randomAlphanumeric(8);
		helper.createSaturnVaultAccount(siteName);

		//after creation ensure the account is in the list
		helper.driver.findElement(By.cssSelector("table > tbody > tr")); // implicit wait
		WebElement siteNameCell = helper.driver.findElement(By.xpath("//td[contains(text(),'" + siteName + "')]"));
		assertEquals(siteName, siteNameCell.getText());
	}

	@Test
	public void deleteSaturnVaultAccountTest(){
		helper.login();
		String siteName = "Site"+ RandomStringUtils.randomAlphanumeric(8);
		helper.createSaturnVaultAccount(siteName);
		helper.deleteSaturnVaultAccount(siteName, false);
		//		after deleting the vault, the row representing it is not there.
		assertFalse(helper.isElementPresent(By.cssSelector("#site-" + siteName + " .delete-vault")));

		siteName += "-change";
		helper.createSaturnVaultAccount(siteName);
		helper.deleteSaturnVaultAccount(siteName, true);
		//		after canceling  the vault deletion, the row representing it is  there.
		assertTrue(helper.isElementPresent(By.cssSelector("#site-" + siteName + " .delete-vault")));

	}

	@Test
	public void showThenHidePassword() {
		helper.login();
		String siteName = "Site"+ RandomStringUtils.randomAlphanumeric(8);
		helper.createSaturnVaultAccount(siteName);

		// Assert that no saturnpass-password elements exist that are showing plaintext
		assertFalse(helper.isElementPresent(By.cssSelector("input[type=text].saturnpass-password")));

		// Click the button to view the password we just created
		// The row is id'd by "site-[siteName]", and the visibility toggler
		// has class .toggle-password-visibility
		// Note that a space in CSS selectors represents a parent->child relationship
		helper.driver.findElement(By.cssSelector("#site-" + siteName + " .toggle-password-visible")).click();

		// Assert that our created password now shows in plaintext
		assertTrue(helper.isElementPresent(By.cssSelector("#site-" + siteName + " input[type=text].saturnpass-password")));

		helper.deleteSaturnVaultAccount(siteName, false);
	}

	@After
	public void tearDown() throws Exception {
		helper.tearDown();
	}
}