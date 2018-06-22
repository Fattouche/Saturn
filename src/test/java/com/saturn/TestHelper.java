package com.saturn;

import org.apache.commons.lang3.RandomStringUtils;
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

public class TestHelper {
	public WebDriver driver;
	public String url;
	public boolean acceptNextAlert = true;
	public String validUsername = "j.oberoi@saturn.com";
	public String validPassword = "pizzaman";

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
		driver.get(url+"/#/saturn-vault/new");

		String login = "juju";
		String password = "mozzarella";

		driver.findElement(By.id("field_site")).sendKeys(siteName);
		driver.findElement(By.id("field_login")).sendKeys(login);
		driver.findElement(By.id("field_password")).sendKeys(password);

		driver.findElement(By.cssSelector("form[name='editForm']")).submit();
	}

	public void deleteSaturnVaultAccount(String siteName, boolean cancel){

		driver.findElement(By.cssSelector("#site-" + siteName + " .delete-vault")).click();
		if(!cancel){
			driver.findElement(By.cssSelector("form[name=deleteForm] .delete-button")).click();
		} else {
			driver.findElement(By.cssSelector("form[name=deleteForm] .cancel-button")).click();
		}

	}

	// TODO - add tests for show passwordd???

	public void tearDown() throws Exception {
		this.driver.quit();
	}

	public boolean isElementPresent(By by) {
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
