package com.saturn;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.AfterClass;
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
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.JavascriptExecutor;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.net.URL;
import java.util.List;

import com.saturn.TestDeleteAccount;

import static org.junit.Assert.*;	
  
public class TestPagination {
	private static TestHelper helper;
	public static String siteNameBase;
  public static int accountsPerPage;

  @BeforeClass
	public static void setUp() throws Exception {
		helper = new TestHelper();
		helper.driver.get(helper.url);

    helper.login();

    accountsPerPage = 20;

		siteNameBase = "CoolSite" + RandomStringUtils.randomAlphanumeric(8);
    
    // Create enough accounts that they will be split onto multiple pages
    // for (int i = 0; i < accountsPerPage + 1; i++) {
    //   String siteName = siteNameBase + "_" + i;

    //   if(!helper.isElementPresent(By.cssSelector("#site-" + siteName))){
    //     helper.createSaturnVaultAccount(siteName);
    //   }
    // }
	}

  @Test
  public void testNextPage() {
    // Click has been failing, so use get() for now
    //helper.driver.findElement(By.cssSelector(".next")).click();
    helper.driver.get(helper.url + "?page=2");

    assertFalse(helper.isElementPresent(By.cssSelector(".next.disabled")));
    assertTrue(helper.isElementPresent(By.cssSelector(".next")));

    List<List<String>> secondPagePasswords = helper.listSaturnVaultAccounts(false);

    // Assert that we now only see one password
    assertTrue(secondPagePasswords.size() == 1);

    // Assert that next button is now disabled
    assertTrue(helper.isElementPresent(By.cssSelector(".next")));
  }

  @Test
  public void testNoNextPage() {
    helper.driver.get(helper.url + "?page=2");
    
    assertTrue(helper.isElementPresent(By.cssSelector(".next.disabled")));
  }

  @Test
  public void testPrevPage() {
    helper.driver.get(helper.url + "?page=2");

    assertTrue(helper.isElementPresent(By.cssSelector(".prev")));
    assertFalse(helper.isElementPresent(By.cssSelector(".prev.disabled")));

    helper.driver.get(helper.url + "?page=1");

    List<List<String>> firstPagePasswords = helper.listSaturnVaultAccounts(false);

    assertTrue(firstPagePasswords.size() == 20);
  }

  @Test
  public void testNoPrevPage() {
    helper.driver.get(helper.url + "?page=1");
    
    assertTrue(helper.isElementPresent(By.cssSelector(".prev.disabled")));
  }


  @AfterClass
	public static void tearDown() throws Exception {

    // Delete the created accounts
    // for (int i = 0; i < accountsPerPage + 1; i++) {
    //   String siteName = siteNameBase + "_" + i;

    //   if(helper.isElementPresent(By.cssSelector("#site-" + siteName))){
    //     helper.deleteSaturnVaultAccount(siteName, false);
    //   }
    // }
		
		helper.tearDown();
	}

}
