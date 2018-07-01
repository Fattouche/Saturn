package com.saturn;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
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
import java.util.List;
import org.apache.commons.io.FileUtils;
import java.io.IOException;
import java.lang.Thread;
import java.lang.InterruptedException;

import com.saturn.TestDeleteAccount;

import static org.junit.Assert.*;	
  
public class TestShowHidePasswords {
	private static TestHelper helper;
	public static String siteName;

  @BeforeClass
	public static void setUp() throws Exception {
		helper = new TestHelper();
		helper.getWithWait(helper.url, ".menu");
		siteName = "CoolSite" + RandomStringUtils.randomAlphanumeric(8);

    helper.login();
		
		if(!helper.isElementPresent(By.cssSelector("#site-" + siteName))){
			helper.createSaturnVaultAccount(siteName);
		}
	}

  @Test
	public void togglePasswordInList() {
		helper.getWithWait(helper.url+"/#/saturn-vault", ".saturnpass-password");
		
		// Assert that no saturnpass-password elements exist that are showing plaintext
		assertFalse(helper.isElementPresent(By.cssSelector("input[type=text].saturnpass-password")));

		// Click the button to view the password we just created
		// The row is id'd by "site-[siteName]", and the visibility toggler
		// has class .toggle-password-visibility
		// Note that a space in CSS selectors represents a parent->child relationship
		helper.driver.findElement(By.cssSelector("#site-" + siteName + " .toggle-password-visible")).click();

		// Assert that our created password now shows in plaintext
		assertTrue(helper.isElementPresent(By.cssSelector("#site-" + siteName + " input[type=text].saturnpass-password")));

    
		helper.driver.findElement(By.cssSelector("#site-" + siteName + " .toggle-password-visible")).click();

  	// Assert that password is hidden again
		assertFalse(helper.isElementPresent(By.cssSelector("input[type=text].saturnpass-password")));
	}

	@Test
	public void togglePasswordInCreation() {
		helper.getWithWait(helper.url+"/#/saturn-vault/new", "#field_password");

		// Fill in password
		helper.driver.findElement(By.id("field_password")).sendKeys("test_password");

		// Assert that password element is not shown in plaintext
		assertFalse(helper.isElementPresent(By.cssSelector("input[type=text]#field_password")));

		// Click the button to view the password
		helper.driver.findElement(By.cssSelector(".clearfix .toggle-password-visible")).click();

		// Assert that password now shows in plaintext
		assertTrue(helper.isElementPresent(By.cssSelector("input[type=text]#field_password")));

		helper.driver.findElement(By.cssSelector(".clearfix .toggle-password-visible")).click();

  	// Assert that password is hidden again
		assertFalse(helper.isElementPresent(By.cssSelector("input[type=text]#field_password")));

		helper.closeForm("editForm");
	}

	@Test
	public void togglePasswordInEdit() {
		List<List<String>> passwords = helper.listSaturnVaultAccountsOnPage(1, false);

		// Get an ID of a password account, so that we can edit it
		String someAccountId = passwords.get(0).get(0);

		helper.getWithWait(helper.url+"/#/saturn-vault/" + someAccountId + "/edit", "#field_password");

		// Fill in password
		helper.driver.findElement(By.id("field_password")).sendKeys("test_password");

		// Assert that password element is not shown in plaintext
		assertFalse(helper.isElementPresent(By.cssSelector("input[type=text]#field_password")));

		// Click the button to view the password
		helper.driver.findElement(By.cssSelector(".clearfix .toggle-password-visible")).click();

		// Assert that password now shows in plaintext
		assertTrue(helper.isElementPresent(By.cssSelector("input[type=text]#field_password")));

		helper.driver.findElement(By.cssSelector(".clearfix .toggle-password-visible")).click();

  	// Assert that password is hidden again
		assertFalse(helper.isElementPresent(By.cssSelector("input[type=text]#field_password")));
		
		helper.closeForm("editForm");
	}

	@Test
	public void visibilityReset() {
		helper.getWithWait(helper.url+"/#/saturn-vault", "#site-" + siteName);

		// Toggle visibility
		helper.driver.findElement(By.cssSelector("#site-" + siteName + " .toggle-password-visible")).click();

		// Assert that password now shows in plaintext
		assertTrue(helper.isElementPresent(By.cssSelector("#site-" + siteName + " input[type=text].saturnpass-password")));

		// Refresh page
		helper.getWithWait(helper.url+"/#/saturn-vault", "#site-" + siteName);

		// Assert that password is hidden again
		assertFalse(helper.isElementPresent(By.cssSelector("#site-" + siteName + " input[type=text].saturnpass-password")));
	}

  @AfterClass
	public static void tearDown() throws Exception {
		helper.getWithWait(helper.url + "/#/saturn-vault", ".menu");

		if(helper.isElementPresent(By.cssSelector("#site-" + siteName))){
			helper.deleteSaturnVaultAccount(siteName, false);
		}
		helper.tearDown();
	}

}
