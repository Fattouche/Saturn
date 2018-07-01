package com.saturn;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
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
	private static TestHelper helper;
    private static List<String> expectedPasswords;
    private static String siteName;
	private static WebDriverWait wait;
	private static boolean acceptNextAlert = true;
    private static StringBuffer verificationErrors = new StringBuffer();

	@BeforeClass
	public static void setUp() throws Exception {
		helper = new TestHelper();
        helper.driver.get(helper.url);
        siteName = "Site"+ RandomStringUtils.randomAlphanumeric(8);
        wait = new WebDriverWait(helper.driver,100);
        helper.login();
		helper.open_Gen_Pass_form();
	}

    @Test
    public void Genrate_Password_form() throws Exception {
        WebElement pwGenForm = (new WebDriverWait(helper.driver, 10, 500))
                .until(ExpectedConditions.elementToBeClickable(
                        By.cssSelector("form[name=\"pdwGenForm\"]"))
                );
        assertEquals(true, pwGenForm.isDisplayed());
    }


    @Test
    public void password_with_Repetition() throws Exception {
    	helper.driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
    	WebElement password_Field = helper.driver.findElement(By.id("field_password"));
    	WebElement length_Field = helper.driver.findElement(By.id("field_length"));
    	helper.driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
    	wait.until(ExpectedConditions.attributeToBeNotEmpty(password_Field, "value"));
    	validate_length(password_Field.getAttribute("value"),length_Field.getAttribute("value"));
    }

 
    @Test
    public void password_without_Repetition() throws Exception{
   		helper.driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
    	WebElement password_Field = helper.driver.findElement(By.id("field_password"));
    	helper.driver.findElement(By.id("field_length")).clear();
    	helper.driver.findElement(By.id("field_length")).sendKeys("10");	//Setting Given Lenght to 10 charaters
    	WebElement length_Field = helper.driver.findElement(By.id("field_length"));
    	helper.driver.findElement(By.id("field_repetition")).click();
    	helper.driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
    	wait.until(ExpectedConditions.attributeToBeNotEmpty(password_Field, "value"));
    	validate_length(password_Field.getAttribute("value"),length_Field.getAttribute("value"));    	
    }

    @Test
    public void password_with_Lowercase_only() throws Exception{
    	helper.driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
    	confirm_click("field_digits");
    	confirm_click("field_upper");
    	confirm_click("field_special");
    	WebElement password_Field = helper.driver.findElement(By.id("field_password"));
    	WebElement length_Field = helper.driver.findElement(By.id("field_length"));
    	helper.driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
    	wait.until(ExpectedConditions.attributeToBeNotEmpty(password_Field, "value"));
    	validate_length(password_Field.getAttribute("value"),length_Field.getAttribute("value"));
    	validate_charaters("Lower",password_Field.getAttribute("value"));      	
    }

    @Test
    public void password_with_Uppercase_only() throws Exception{
    	helper.driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
    	confirm_click("field_lower");
    	confirm_click("field_digits");
    	confirm_click("field_special");
    	WebElement password_Field = helper.driver.findElement(By.id("field_password"));
    	WebElement length_Field = helper.driver.findElement(By.id("field_length"));
    	helper.driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
    	wait.until(ExpectedConditions.attributeToBeNotEmpty(password_Field, "value"));
    	validate_length(password_Field.getAttribute("value"),length_Field.getAttribute("value"));
    	validate_charaters("Upper",password_Field.getAttribute("value"));  
    }

    @Test
    public void password_with_Digits_only() throws Exception{
    	helper.driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
    	confirm_click("field_lower");
    	confirm_click("field_upper");
    	confirm_click("field_special");
    	WebElement password_Field = helper.driver.findElement(By.id("field_password"));
    	WebElement length_Field = helper.driver.findElement(By.id("field_length"));
    	helper.driver.findElement(By.cssSelector("button[ng-click='vm.generate()']")).click();
    	wait.until(ExpectedConditions.attributeToBeNotEmpty(password_Field, "value"));
    	validate_length(password_Field.getAttribute("value"),length_Field.getAttribute("value")); 
    	validate_charaters("Digits",password_Field.getAttribute("value"));   
    }

    @Test
    public void password_with_SpecialChar_only() throws Exception{
    	helper.driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
    	confirm_click("field_lower");
    	confirm_click("field_upper");
    	confirm_click("field_digits");
    	helper.driver.findElement(By.id("field_length")).clear();
    	helper.driver.findElement(By.id("field_length")).sendKeys("5");	//Setting Given Lenght to 5 charaters
    	WebElement password_Field = helper.driver.findElement(By.id("field_password"));
    	WebElement length_Field = helper.driver.findElement(By.id("field_length"));
    	helper.driver.findElement(By.cssSelector("button[ng-click='vm.generate()']")).click();
    	wait.until(ExpectedConditions.attributeToBeNotEmpty(password_Field, "value"));
    	validate_length(password_Field.getAttribute("value"),length_Field.getAttribute("value"));
    	validate_charaters("Special",password_Field.getAttribute("value"));  
    }

    private void validate_length(String password,String length_in_string){
    	int given_length = Integer.parseInt(length_in_string);
    	int password_length = password.length();
    	assertEquals(given_length,password_length);
    }

    private void confirm_click(String field) throws Exception {
    	wait.until(ExpectedConditions.elementToBeClickable(By.id(field)));
    	helper.driver.findElement(By.id(field)).click();
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

	@AfterClass
	public static void tearDown() throws Exception {
		if(helper.isElementPresent(By.cssSelector("#site-" + siteName))){
			helper.deleteSaturnVaultAccount(siteName, false);
		}
        helper.tearDown();
	}
}
