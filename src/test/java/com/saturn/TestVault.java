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
		helper.driver.findElement(By.cssSelector("table > tbody > tr")); // implicit wait
		WebElement siteNameCell = helper.driver.findElement(By.xpath("//td[contains(text(),'" + siteName + "')]"));
		assertEquals(siteName, siteNameCell.getText());
	}
	
	@After
	public void tearDown() throws Exception {
		if(helper.isElementPresent(By.cssSelector("#site-" + siteName))){
			helper.deleteSaturnVaultAccount(siteName, false);
		}
		helper.tearDown();
	}
}
