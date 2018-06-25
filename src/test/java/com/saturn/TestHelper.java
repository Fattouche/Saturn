package com.saturn;

import java.io.File;
import java.net.URL;
import java.util.concurrent.TimeUnit;

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
<<<<<<< HEAD
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
=======
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.*;
import java.io.File;
import java.util.concurrent.TimeUnit;
import java.net.URL;
import java.util.Map;
>>>>>>> Add tests for viewing vault

public class TestHelper {
	public WebDriver driver;
	public String url;
	public boolean acceptNextAlert = true;
	public String validUsername = "j.oberoi@saturn.com";
	public String validPassword = "pizzaman";
	public String defaultLogin = "speaker";
	public String defaultPass = "guitar";

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
		driver.get(url);
		driver.findElement(By.id("login")).click();
		driver.findElement(By.id("username")).sendKeys(username);
		driver.findElement(By.id("password")).sendKeys(password);

		driver.findElement(By.id("password")).submit();
	}

	public void createSaturnVaultAccount(String siteName){
		createSaturnVaultAccount(siteName, defaultLogin, defaultPass);
	}

	public void createSaturnVaultAccount(String siteName, String login, String password){
		driver.get(url+"/#/saturn-vault/new");


		driver.findElement(By.id("field_site")).sendKeys(siteName);
		driver.findElement(By.id("field_login")).sendKeys(login);
		driver.findElement(By.id("field_password")).sendKeys(password);

		driver.findElement(By.cssSelector("form[name='editForm']")).submit();

		// wait for the submission to finish
		new WebDriverWait(driver, 3).until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("form[name='editForm']")));
	}
	
	public void deleteSaturnVaultAccount(String siteName, boolean cancel){
		closeForm("editForm");
		closeForm("deleteForm");
		
		driver.findElement(By.cssSelector("#site-" + siteName + " .delete-vault")).click();
		if(!cancel){
			driver.findElement(By.cssSelector("form[name=deleteForm] .delete-button")).click();
		} else {
			driver.findElement(By.cssSelector("form[name=deleteForm] .cancel-button")).click();
		}

	}

	public void closeForm(String name){
		if(isElementPresent(By.cssSelector("form[name='"+name+"']"))){
			driver.findElement(By.cssSelector("form[name='"+name+"'] .cancel-button")).click();
			new WebDriverWait(driver, 3).until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("form[name='"+name+"']")));
		}

	}

	public List<List<String>> listSaturnVaultAccounts(){
		driver.get(url+"/#/saturn-vault");
		WebElement tablePasswords = driver.findElement(By.cssSelector(".table-responsive .jh-table.table.table-striped tbody"));
		List<WebElement> passwords = tablePasswords.findElements(By.tagName("tr"));
		List<List<String>> stringPasswords = new ArrayList<List<String>>();
		for(WebElement password: passwords){
			List<WebElement> columns=password.findElements(By.tagName("td"));
			List<String> item = new ArrayList<String>();
			for(WebElement column : columns)
            {
				item.add(column.getText());
			}
			stringPasswords.add(item);
		}
		return stringPasswords;
	}

	public List<String> getSaturnVaultColumns(){
		driver.get(url+"/#/saturn-vault");
		WebElement tablePasswords = driver.findElement(By.cssSelector(".table-responsive .jh-table.table.table-striped thead"));
		List<WebElement> columnNames = tablePasswords.findElements(By.tagName("th"));
		List<String> names = new ArrayList<String>();
		for(WebElement name: columnNames){
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
