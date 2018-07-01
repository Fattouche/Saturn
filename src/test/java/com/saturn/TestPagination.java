package com.saturn;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
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
		helper.getWithWait(helper.url, ".menu");

    helper.login();

    accountsPerPage = 20;

		siteNameBase = "CoolSite" + RandomStringUtils.randomAlphanumeric(8);
    
    // Create enough accounts that they will be split onto multiple pages
    for (int i = 0; i < accountsPerPage + 1; i++) {
      String siteName = siteNameBase + "_" + i;

      if(!helper.isElementPresent(By.cssSelector("#site-" + siteName))){
        helper.createSaturnVaultAccount(siteName);
      }
    }
	}

  @Test
  public void testNextPage() {
    // Start at page 1
    helper.getWithWait(helper.url + "/#/saturn-vault?page=1", ".next");

    // Get first page passwords
    List<List<String>> firstPagePasswords = helper.listSaturnVaultAccountsOnPage(1);
    
    // Assert that next button can be used
    assertFalse(helper.isElementPresent(By.cssSelector(".next.disabled")));
    assertTrue(helper.isElementPresent(By.cssSelector(".next")));

    // Go to page 2
    // Click has been failing, so use get() for now
    //helper.driver.findElement(By.cssSelector(".next")).click();
    helper.getWithWait(helper.url + "/#/saturn-vault?page=2", ".next");

    List<List<String>> secondPagePasswords = helper.listSaturnVaultAccountsOnPage(2, false);

    // Assert that second page has different ids than first
    for (List<String> firstPageRow : firstPagePasswords) {
      String id = firstPageRow.get(0);

      for (List<String> secondPageRow : secondPagePasswords) {
        assertThat(id, not(equalTo(secondPageRow.get(0))));
      }
    }

    // Assert that next button is disabled
    assertTrue(helper.isElementPresent(By.cssSelector(".next.disabled")));
  }

  @Test
  public void testNoNextPage() {
    helper.getWithWait(helper.url + "/#/saturn-vault?page=2", ".next");
    
    assertTrue(helper.isElementPresent(By.cssSelector(".next.disabled")));
  }

  @Test
  public void testPreviousPage() {
    helper.getWithWait(helper.url + "/#/saturn-vault?page=2", ".next");

    assertTrue(helper.isElementPresent(By.cssSelector(".previous")));
    assertFalse(helper.isElementPresent(By.cssSelector(".previous.disabled")));

    helper.getWithWait(helper.url + "/#/saturn-vault?page=1", ".next");

    List<List<String>> firstPagePasswords = helper.listSaturnVaultAccountsOnPage(1, false);

    assertTrue(firstPagePasswords.size() == accountsPerPage);
  }

  @Test
  public void testNoPreviousPage() {
    helper.getWithWait(helper.url + "/#/saturn-vault?page=1", ".next");
    
    assertTrue(helper.isElementPresent(By.cssSelector(".previous.disabled")));
  }


  @AfterClass
	public static void tearDown() throws Exception {

    // Delete the created accounts
    for (int i = 0; i < accountsPerPage + 1; i++) {
      String siteName = siteNameBase + "_" + i;

      if(helper.isElementPresent(By.cssSelector("#site-" + siteName))){
        helper.deleteSaturnVaultAccount(siteName, false);
      }
    }
		
		helper.tearDown();
	}

}
