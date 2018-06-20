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

public class TestSelenium {
	private WebDriver driver;
	private String url;
	private boolean acceptNextAlert = true;
	private String validUsername = "j.oberoi@saturn.com";
	private String validPassword = "pizzaman";

	@Before
	public void setUp() throws Exception {
		String driverName = System.getenv("SATURN_DRIVER");
		if (driverName == null) {
			driverName = "remote";
		}

		url = System.getenv("SATURN_URL");
		if (url == null) {
			url = "http://saturn:8080";
		}
        System.out.println("Driver Name: " + driverName + "\nUrl: " + url);
		setDriver(driverName);
		driver.get(url);
	}

	private void login() {
		login(validUsername, validPassword);
	}

	private void login(String username, String password){
		driver.get(url);
		driver.findElement(By.id("login")).click();
		driver.findElement(By.id("username")).sendKeys(username);
		driver.findElement(By.id("password")).sendKeys(password);

		driver.findElement(By.id("password")).submit();
	}


	@Test
	public void headerIsCorrect() throws Exception {
		assertEquals("SATURN SECURITY SYSTEM", driver.findElement(By.tagName("h1")).getText().toUpperCase());
	}

	@Test
	public void logsUserWithCorrectCredentials() {
		assertFalse(isElementPresent(By.id("account-menu"))); // starts logged out
		login(validUsername, validPassword);
		assertTrue(isElementPresent(By.id("account-menu")));
	}

	@Test
	public void doesNotLogUserWithIncorrectCredentials() {
		assertFalse(isElementPresent(By.id("account-menu"))); // starts logged out
		login(validUsername, validPassword+"toMakeInvalid");
		assertFalse(isElementPresent(By.id("account-menu")));
	}

	public void createSaturnVaultAccount(String siteName){
		driver.get(url+"/#/saturn-vault/new");

		String login = "juju";
		String password = "mozzarella";

		driver.findElement(By.id("field_site")).sendKeys(siteName);
		driver.findElement(By.id("field_login")).sendKeys(login);
		driver.findElement(By.id("field_password")).sendKeys(password);

		driver.findElement(By.cssSelector("form[name='editForm']")).submit();
	}

	@Test
	public void createSaturnVaultAccountTest() {
		login();
		String siteName = "Site"+ RandomStringUtils.randomAlphanumeric(8);
		createSaturnVaultAccount(siteName);

		//after creation ensure the account is in the list
		driver.findElement(By.cssSelector("table > tbody > tr")); // implicit wait
		WebElement siteNameCell = driver.findElement(By.xpath("//td[contains(text(),'" + siteName + "')]"));
		assertEquals(siteName, siteNameCell.getText());
	}

	public void deleteSaturnVaultAccount(String siteName, boolean cancel){

		driver.findElement(By.cssSelector(".site-" + siteName + " .delete-vault")).click();
		if(!cancel){
			driver.findElement(By.cssSelector("form[name=deleteForm] .delete-button")).click();
		} else {
			driver.findElement(By.cssSelector("form[name=deleteForm] .cancel-button")).click();
		}

	}

    @Test
    public void deleteSaturnVaultAccountTest(){
		login();
		String siteName = "Site"+ RandomStringUtils.randomAlphanumeric(8);
		createSaturnVaultAccount(siteName);
		deleteSaturnVaultAccount(siteName, false);
		//		after deleting the vault, the row representing it is not there.
		assertFalse(isElementPresent(By.cssSelector(".site-" + siteName + " .delete-vault")));

		siteName += "-change";
		createSaturnVaultAccount(siteName);
		deleteSaturnVaultAccount(siteName, true);
		//		after canceling  the vault deletion, the row representing it is  there.
		assertTrue(isElementPresent(By.cssSelector(".site-" + siteName + " .delete-vault")));

    }

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private void setDriver(String driverName) {
		driverName = driverName.toLowerCase();

		long timeout = 5;

		switch (driverName) {
			case "firefox":
				System.setProperty("webdriver.gecko.driver", "/opt/dgsdfgsdgdsgsdf");
				driver = new FirefoxDriver(new FirefoxBinary(new File("/usr/bin/firefox-esr")), new FirefoxProfile());
				driver.manage().window().maximize();
				break;
			case "chrome":
				ChromeOptions options = new ChromeOptions();
				options.addArguments("--start-maximized");
				driver = new ChromeDriver(options);
				break;
			case "remote":
				DesiredCapabilities capabilities = new DesiredCapabilities();
				capabilities.setBrowserName("firefox");
				
				String remoteUrlString = System.getenv("SELENIUM_URL");
				if (remoteUrlString == null) {
					remoteUrlString = "http://selenium:4444/wd/hub";
				}

				URL remoteUrl;
				try {
					remoteUrl = new URL(remoteUrlString);
				} catch (Exception e) {
					return; // This is bad but w/e hopefully the url is valid
				}

				driver = new RemoteWebDriver(remoteUrl, capabilities);
				break;
			default:
				driver = new HtmlUnitDriver();
				((HtmlUnitDriver) driver).setJavascriptEnabled(true);
				timeout = 20; // HtmlUnitDriver is slower than Firefox and Chrome
		}

		driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(timeout, TimeUnit.SECONDS);
	}
}
