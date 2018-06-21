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

	public void createSaturnVaultAccount(String siteName){
		helper.driver.get(helper.url+"/#/saturn-vault/new");

		String login = "juju";
		String password = "mozzarella";

		helper.driver.findElement(By.id("field_site")).sendKeys(siteName);
		helper.driver.findElement(By.id("field_login")).sendKeys(login);
		helper.driver.findElement(By.id("field_password")).sendKeys(password);

		helper.driver.findElement(By.cssSelector("form[name='editForm']")).submit();
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

	public void deleteSaturnVaultAccount(String siteName, boolean cancel){

		helper.driver.findElement(By.cssSelector(".site-" + siteName + " .delete-vault")).click();
		if(!cancel){
			helper.driver.findElement(By.cssSelector("form[name=deleteForm] .delete-button")).click();
		} else {
			helper.driver.findElement(By.cssSelector("form[name=deleteForm] .cancel-button")).click();
		}

	}

	@Test
	public void deleteSaturnVaultAccountTest(){
		helper.login();
		String siteName = "Site"+ RandomStringUtils.randomAlphanumeric(8);
		helper.createSaturnVaultAccount(siteName);
		helper.deleteSaturnVaultAccount(siteName, false);
		//		after deleting the vault, the row representing it is not there.
		assertFalse(helper.isElementPresent(By.cssSelector(".site-" + siteName + " .delete-vault")));

		siteName += "-change";
		helper.createSaturnVaultAccount(siteName);
		helper.deleteSaturnVaultAccount(siteName, true);
		//		after canceling  the vault deletion, the row representing it is  there.
		assertTrue(helper.isElementPresent(By.cssSelector(".site-" + siteName + " .delete-vault")));

	}
	// TODO - add tests for show passwordd???

	@After
	public void tearDown() throws Exception {
		helper.tearDown();
	}
}
