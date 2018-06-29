package com.saturn;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.*;

import static org.junit.Assert.*;

import java.io.File;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;


public class TestPasswordStrength {
    private static TestHelper helper;
    private static List<String> expectedColumns;
    private static String siteName;

    private static String red = "#ff0000";
    private static String orange = "#ff9900";
    private static String yellow = "#ffff00";
    private static String lightGreen = "#99ff00";
    private static String green = "#00ff00";

	@BeforeClass
	public static void setUp() throws Exception {
        helper = new TestHelper();
        helper.driver.get(helper.url);
        helper.login();
    }
    
    //Verify that the password strength meter is working
	@Test
	public void passwordStrengthTest() throws Exception{
        helper.driver.get(helper.url+"/#/saturn-vault/new");
        helper.driver.findElement(By.cssSelector("#generate_modal")).click();
        assertPasswordColorFromLength(red, "1");
        assertPasswordColorFromLength(green, "60");
        assertPasswordColorFromString(red, "password");
        assertPasswordColorFromString(orange, "password1");
        assertPasswordColorFromString(lightGreen, "pa$$word12");
        assertPasswordColorFromString(green, "Strongpa$$word!1");
    }

    public void assertPasswordColorFromLength(String expectedColor, String length){
        WebElement textBox = helper.driver.findElement(By.cssSelector("#field_length"));
        textBox.clear();
        textBox.sendKeys(length);
        helper.driver.findElement(By.cssSelector("#generate_button")).click();
        assertBarColor(expectedColor);
    }

    public void assertPasswordColorFromString(String expectedColor, String password) throws Exception{
        WebElement textBox =helper.driver.findElement(By.cssSelector("#field_password"));
        textBox.clear();
        textBox.sendKeys(password);
        assertBarColor(expectedColor);
    }

    //Screenshot stuff
    // helper.driver.findElement(By.cssSelector("#generate_button")).click();
    // TakesScreenshot scrShot =((TakesScreenshot)helper.driver);
    // File SrcFile=scrShot.getScreenshotAs(OutputType.FILE);
    // File scrFile = ((TakesScreenshot)helper.driver).getScreenshotAs(OutputType.FILE);
    // FileUtils.copyFile(scrFile, new File("/saturn/screenshot.png"));

    public void assertBarColor(String expectedColor){
        WebElement bar = helper.driver.findElement(By.cssSelector("#strengthBar .point:nth-child(1)"));
        String barColor = bar.getCssValue("background-color");

        String[] hexValue = barColor.replace("rgb(", "").replace(")", "").split(",");
        int hexValue1=Integer.parseInt(hexValue[0]);
        hexValue[1] = hexValue[1].trim();
        int hexValue2=Integer.parseInt(hexValue[1]);
        hexValue[2] = hexValue[2].trim();
        int hexValue3=Integer.parseInt(hexValue[2]);
        
        String actualColor = String.format("#%02x%02x%02x", hexValue1, hexValue2, hexValue3);
        
        assertEquals(expectedColor, actualColor);
    }
	
	@AfterClass
	public static void tearDown() throws Exception {
        if(helper.isElementPresent(By.cssSelector("#site-" + siteName))){
			helper.deleteSaturnVaultAccount(siteName, false);
		}
        helper.tearDown();
	}
}