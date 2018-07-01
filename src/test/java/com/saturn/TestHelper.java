package com.saturn;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.lang.InterruptedException;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;

import org.openqa.selenium.*;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.*;
import java.lang.*;

public class TestHelper {
	public WebDriver driver;
	public String url;
	public boolean acceptNextAlert = true;
	public String validUsername = "j.oberoi@saturn.com";
	public String validPassword = "pizzaman";
	public String defaultLogin = "speaker";
	public String defaultPass = "guitar";
	public int timeout = 30;

	public TestHelper() throws Exception {
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
	}

	public void login() {
		login(validUsername, validPassword);
	}

	public void login(String username, String password){
		getWithWait(url, ".menu");

		// If login isn't there, assume already logged in
		if (isElementPresent(By.id("login"))) {
			driver.findElement(By.id("login")).click();
			driver.findElement(By.id("username")).sendKeys(username);
			driver.findElement(By.id("password")).sendKeys(password);

			driver.findElement(By.id("password")).submit();
		}
	}

	public void createSaturnVaultAccount(String siteName) {
		createSaturnVaultAccount(siteName, defaultLogin, defaultPass);
	}

	public void createSaturnVaultAccountNotUsingDefault(String siteName, String login, String password) {
		createSaturnVaultAccount(siteName, login, password);
	}

	public void createSaturnVaultAccount(String siteName, String login, String password){
		getWithWait(url+"/#/saturn-vault/new", "#field_site");

		driver.findElement(By.id("field_site")).sendKeys(siteName);
		driver.findElement(By.id("field_login")).sendKeys(login);
		driver.findElement(By.id("field_password")).sendKeys(password);

		driver.findElement(By.cssSelector("form[name='editForm']")).submit();

		// wait for the submission to finish
		new WebDriverWait(driver, timeout).until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("form[name='editForm']")));
	}

	public void deleteSaturnVaultAccount(String siteName, boolean cancel) {
		closeForm("editForm");
		closeForm("deleteForm");

		driver.findElement(By.cssSelector("#site-" + siteName + " .delete-vault")).click();
		if (!cancel) {
			driver.findElement(By.cssSelector("form[name=deleteForm] .delete-button")).click();
		} else {
			driver.findElement(By.cssSelector("form[name=deleteForm] .cancel-button")).click();
		}

	}

	public void createAccounts(int numberOfAccounts) {
		for (int i = 0; i < numberOfAccounts; i++) {
			String siteName = "Site" + i;
			createSaturnVaultAccount(siteName);
		}
	}

	public List<String> expectedColumns() {
		List<String> expectedColumns = new ArrayList<String>();
		expectedColumns.add("ID");
		expectedColumns.add("Site");
		expectedColumns.add("Login");
		expectedColumns.add("Password");
		expectedColumns.add("Created Date");
		expectedColumns.add("Last Modified Date");
		return expectedColumns;
	}

	public void closeForm(String name){
		if(isElementPresent(By.cssSelector("form[name='"+name+"']"))){
			driver.findElement(By.cssSelector("form[name='"+name+"'] .cancel-button")).click();

			new WebDriverWait(driver, timeout).until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("form[name='"+name+"']")));
		}

	}

	public List<List<String>> listSaturnVaultAccountsOnPageOnPage(int pageNum, boolean skipRefresh){
		if (!skipRefresh) {
			getWithWait(url+"/#/saturn-vault?page=" + pageNum, ".table-responsive");
		}
		WebElement tablePasswords = driver.findElement(By.cssSelector(".table-responsive .jh-table.table.table-striped tbody"));
		List<WebElement> passwords = tablePasswords.findElements(By.tagName("tr"));
		List<List<String>> stringPasswords = new ArrayList<List<String>>();
		for (WebElement password : passwords) {
			List<WebElement> columns = password.findElements(By.tagName("td"));
			List<String> item = new ArrayList<String>();
			for (WebElement column : columns) {
				item.add(column.getText());
			}
			stringPasswords.add(item);
		}
		return stringPasswords;
	}

	public List<String> getSaturnVaultColumns(){
		getWithWait(url+"/#/saturn-vault", ".table-responsive");
		WebElement tablePasswords = driver.findElement(By.cssSelector(".table-responsive .jh-table.table.table-striped thead"));
		List<WebElement> columnNames = tablePasswords.findElements(By.tagName("th"));
		List<String> names = new ArrayList<String>();
		for (WebElement name : columnNames) {
			names.add(name.getText());
		}
		return names;
	}

	public void tearDown() throws Exception {
		this.driver.quit();
	}

	public boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (org.openqa.selenium.NoSuchElementException e) {
			return false;
		}
	}

	public void getWithWait(String URL, String cssSelectorToWaitFor) {
		if (driver.getCurrentUrl().equals(URL)) {
			// A get() will not refresh if you are already at that URL, so
			// refresh explicitly
			driver.navigate().refresh();
		}
		else {
			driver.get(URL);
		}
		new WebDriverWait(driver, timeout).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(cssSelectorToWaitFor)));
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
