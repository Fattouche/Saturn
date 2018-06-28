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
  
public class TestShowHidePasswords {
	private TestHelper helper;
	public String siteName;

  @Before
	public void setUp() throws Exception {
		helper = new TestHelper();
		helper.driver.get(helper.url);
		siteName = "CoolSite" + RandomStringUtils.randomAlphanumeric(8);

    helper.login();
		
		if(!helper.isElementPresent(By.cssSelector("#site-" + siteName))){
			helper.createSaturnVaultAccount(siteName);
		}
	}

  @Test
	public void showThenHidePassword() {
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

  @After
	public void tearDown() throws Exception {
		if(helper.isElementPresent(By.cssSelector("#site-" + siteName))){
			helper.deleteSaturnVaultAccount(siteName, false);
		}
		helper.tearDown();
	}

}
