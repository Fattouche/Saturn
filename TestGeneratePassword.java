package com.saturn;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
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
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.NoSuchElementException;
import java.net.URL;
import java.util.Map;

import static org.junit.Assert.*;
import java.util.*;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestGeneratePassword {
	private WebDriver driver;
	private String url;
	private WebDriverWait wait;
	private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

	@Before
	public void setUp() throws Exception {
		String driverName = System.getenv("SATURN_DRIVER");
		if (driverName == null) {
			driverName = "chrome";
		}

		url = System.getenv("SATURN_URL");
		if (url == null) {
			url = "http://localhost:8080";
		}
        System.out.println("Driver Name: " + driverName + "\nUrl: " + url);
		setDriver(driverName);
		driver.get(url);
		wait = new WebDriverWait(driver,100);

		login();
		open_Gen_Pass_form();
	}

    @Test
    public void Genrate_Password_form() throws Exception {
        WebElement pwGenForm = (new WebDriverWait(driver, 10, 500))
                .until(ExpectedConditions.elementToBeClickable(
                        By.cssSelector("form[name=\"pdwGenForm\"]"))
                );
        assertEquals(true, pwGenForm.isDisplayed());
    }


    @Test
    public void password_with_Repetition() throws Exception {
    	driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
    	WebElement password_Field = driver.findElement(By.id("field_password"));
    	WebElement length_Field = driver.findElement(By.id("field_length"));
    	driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
    	wait.until(ExpectedConditions.attributeToBeNotEmpty(password_Field, "value"));
    	validate_length(password_Field.getAttribute("value"),length_Field.getAttribute("value"));
    }

 
    @Test
    public void password_without_Repetition() throws Exception{
   		driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
    	WebElement password_Field = driver.findElement(By.id("field_password"));
    	driver.findElement(By.id("field_length")).clear();
    	driver.findElement(By.id("field_length")).sendKeys("10");	//Setting Given Lenght to 10 charaters
    	WebElement length_Field = driver.findElement(By.id("field_length"));
    	driver.findElement(By.id("field_repetition")).click();
    	driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
    	wait.until(ExpectedConditions.attributeToBeNotEmpty(password_Field, "value"));
    	validate_length(password_Field.getAttribute("value"),length_Field.getAttribute("value"));    	
    }

    @Test
    public void password_with_Lowercase_only() throws Exception{
    	driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
    	confirm_click("field_digits");
    	confirm_click("field_upper");
    	confirm_click("field_special");
    	WebElement password_Field = driver.findElement(By.id("field_password"));
    	WebElement length_Field = driver.findElement(By.id("field_length"));
    	driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
    	wait.until(ExpectedConditions.attributeToBeNotEmpty(password_Field, "value"));
    	validate_length(password_Field.getAttribute("value"),length_Field.getAttribute("value"));
    	validate_charaters("Lower",password_Field.getAttribute("value"));      	
    }

    @Test
    public void password_with_Uppercase_only() throws Exception{
    	driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
    	confirm_click("field_lower");
    	confirm_click("field_digits");
    	confirm_click("field_special");
    	WebElement password_Field = driver.findElement(By.id("field_password"));
    	WebElement length_Field = driver.findElement(By.id("field_length"));
    	driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
    	wait.until(ExpectedConditions.attributeToBeNotEmpty(password_Field, "value"));
    	validate_length(password_Field.getAttribute("value"),length_Field.getAttribute("value"));
    	validate_charaters("Upper",password_Field.getAttribute("value"));  
    }

    @Test
    public void password_with_Digits_only() throws Exception{
    	driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
    	confirm_click("field_lower");
    	confirm_click("field_upper");
    	confirm_click("field_special");
    	WebElement password_Field = driver.findElement(By.id("field_password"));
    	WebElement length_Field = driver.findElement(By.id("field_length"));
    	driver.findElement(By.cssSelector("button[ng-click='vm.generate()']")).click();
    	wait.until(ExpectedConditions.attributeToBeNotEmpty(password_Field, "value"));
    	validate_length(password_Field.getAttribute("value"),length_Field.getAttribute("value")); 
    	validate_charaters("Digits",password_Field.getAttribute("value"));   
    }

    @Test
    public void password_with_SpecialChar_only() throws Exception{
    	driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
    	confirm_click("field_lower");
    	confirm_click("field_upper");
    	confirm_click("field_digits");
    	driver.findElement(By.id("field_length")).clear();
    	driver.findElement(By.id("field_length")).sendKeys("5");	//Setting Given Lenght to 5 charaters
    	WebElement password_Field = driver.findElement(By.id("field_password"));
    	WebElement length_Field = driver.findElement(By.id("field_length"));
    	driver.findElement(By.cssSelector("button[ng-click='vm.generate()']")).click();
    	wait.until(ExpectedConditions.attributeToBeNotEmpty(password_Field, "value"));
    	validate_length(password_Field.getAttribute("value"),length_Field.getAttribute("value"));
    	validate_charaters("Special",password_Field.getAttribute("value"));  
    }


    private void login() throws Exception {
	    driver.get(url);
	    driver.findElement(By.id("login")).click();
	    driver.findElement(By.id("username")).sendKeys("p.soell@saturn.com");
	    driver.findElement(By.id("password")).sendKeys("stickman123");
	    driver.findElement(By.id("password")).submit();
    }


	private void open_Gen_Pass_form() {
        WebElement element = driver.findElement(By.xpath("//a[contains(text(),'SaturnVault')]"));
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", element);
        WebElement create_new_SV_Button = (new WebDriverWait(driver, 10, 500))
                .until(ExpectedConditions.elementToBeClickable(
                        By.cssSelector("button[ui-sref=\"saturn-vault.new\"]"))
                );
        create_new_SV_Button.click();
        WebElement generate_Button = (new WebDriverWait(driver, 10, 500))
                .until(ExpectedConditions.elementToBeClickable(
                        By.cssSelector("button[ng-click=\"vm.openPwdGenModal()\"]"))
                );
        generate_Button.click();
    }

    private void validate_length(String password,String length_in_string){
    	int given_length = Integer.parseInt(length_in_string);
    	int password_length = password.length();
    	assertEquals(given_length,password_length);
    }

    private void confirm_click(String field) throws Exception {
    	wait.until(ExpectedConditions.elementToBeClickable(By.id(field)));
    	driver.findElement(By.id(field)).click();
    }

    private void validate_charaters(String type, String password) {
    	switch (type) {
			case "Upper":
				assertEquals(true, StringUtils.isAllUpperCase(password));
				break;
			case "Lower":
				assertEquals(true, StringUtils.isAllLowerCase(password));
				break;
			case "Digits":
				assertEquals(true, StringUtils.isNumeric(password));
				break;
			case "Special":
				assertEquals(false, StringUtils.isNumeric(password));
				assertEquals(false, StringUtils.isAllLowerCase(password));
				assertEquals(false, StringUtils.isAllUpperCase(password));
				break;
		}
    }


	private void setDriver(String driverName) {
		driverName = driverName.toLowerCase();

		long timeout = 10;

		switch (driverName) {
			case "firefox":
				System.setProperty("webdriver.gecko.driver", "");
				driver = new FirefoxDriver(new FirefoxBinary(new File("/usr/bin/firefox")), new FirefoxProfile());
				driver.manage().window().maximize();
				break;
			case "chrome":
				System.setProperty("webdriver.chrome.driver","/chromedriver.exe");
				driver = new ChromeDriver();
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

	@After
	public void tearDown() throws Exception {
		driver.quit();
	    String verificationErrorString = verificationErrors.toString();
	    if (!"".equals(verificationErrorString)) {
	      fail(verificationErrorString);
	    }
	}

	private boolean isElementPresent(By by) {
	    try {
	      driver.findElement(by);
	      return true;
	    } catch (NoSuchElementException e) {
	      return false;
	    }
  	}

	private boolean isAlertPresent() {
	    try {
	      driver.switchTo().alert();
	      return true;
	    } catch (NoAlertPresentException e) {
	      return false;
	    }
	}

	private String closeAlertAndGetItsText() {
	    try {
	      Alert alert = driver.switchTo().alert();
	      String alertText = alert.getText();
	      if (acceptNextAlert) {
	        alert.accept();
	      } else {
	        alert.dismiss();
	      }
	      return alertText;
	    } finally {
	      acceptNextAlert = true;
	    }
	}
}
