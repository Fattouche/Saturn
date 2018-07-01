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
import java.util.concurrent.TimeUnit;



public class TestUpdatePassword {
	private static TestHelper helper;
    private static List<String> expectedPasswords;
    private static String siteName;
    private static WebDriverWait wait;

	@BeforeClass
	public static void setUp() throws Exception {
		helper = new TestHelper();
        helper.driver.get(helper.url);
        siteName = "Site"+ RandomStringUtils.randomAlphanumeric(8);
        helper.driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        helper.driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        helper.driver.manage().timeouts().setScriptTimeout(5, TimeUnit.SECONDS);
        wait = new WebDriverWait(helper.driver, 5);

        helper.login();
        helper.createSaturnVaultAccount(siteName);
    }

    @Test
    public void update_Site() throws Exception {

        // Open the edit password dialogue box.
        String old_id = helper.driver.findElement(By.xpath("//tr[1]/td[1]")).getText();
        String site = helper.driver.findElement(By.xpath("//tr[1]/td[2]")).getText();
        helper.driver.findElement(By.xpath("//tr[1]/td[7]/div/button[1]")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type=submit]")));

        String new_Site_string = "TestingSite";

        // Input new password data and confirm the edit.
        helper.driver.findElement(By.id("field_site")).clear();
		helper.driver.findElement(By.id("field_site")).sendKeys(new_Site_string);  
                
        helper.driver.findElement(By.cssSelector("button[type=submit]")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//pre[1]")));

        String message = helper.driver.findElement(By.xpath("//pre[1]")).getText();
        String new_id = helper.driver.findElement(By.xpath("//tr[1]/td[1]")).getText();
        String new_site = helper.driver.findElement(By.xpath("//tr[1]/td[2]")).getText();

        assertEquals(old_id, new_id);
        assertEquals(new_site, new_Site_string);
    }

    @Test
    public void empty_Site() throws Exception {
        helper.driver.findElement(By.xpath("//tr[1]/td[7]/div/button[1]")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type=submit]")));

        helper.driver.findElement(By.id("field_site")).clear();
        WebElement submit = helper.driver.findElement(By.cssSelector("button[type=submit]"));
        assertEquals("true", submit.getAttribute("disabled"));
        helper.driver.findElement(By.xpath("//span[contains(@class, 'glyphicon-ban-circle')]")).click();
    }


    @Test
    public void update_Login() throws Exception {
    	// Open the edit password dialogue box.
        String old_id = helper.driver.findElement(By.xpath("//tr[1]/td[1]")).getText();
        String login = helper.driver.findElement(By.xpath("//tr[1]/td[3]")).getText();
        helper.driver.findElement(By.xpath("//tr[1]/td[7]/div/button[1]")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type=submit]")));

        String new_Login_string = "TestingLogin";

        // Input new password data and confirm the edit.
        helper.driver.findElement(By.id("field_login")).clear();
        helper.driver.findElement(By.id("field_login")).sendKeys(new_Login_string);
         
        helper.driver.findElement(By.cssSelector("button[type=submit]")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//pre[1]")));

        String message = helper.driver.findElement(By.xpath("//pre[1]")).getText();
        String new_id = helper.driver.findElement(By.xpath("//tr[1]/td[1]")).getText();
        String new_login = helper.driver.findElement(By.xpath("//tr[1]/td[3]")).getText();

        assertEquals(old_id, new_id);
        assertEquals(new_login, new_Login_string);
    }

    @Test
    public void empty_Login() throws Exception {
        helper.driver.findElement(By.xpath("//tr[1]/td[7]/div/button[1]")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type=submit]")));

        helper.driver.findElement(By.id("field_login")).clear();
        WebElement submit = helper.driver.findElement(By.cssSelector("button[type=submit]"));
        assertEquals("true", submit.getAttribute("disabled"));
        helper.driver.findElement(By.xpath("//span[contains(@class, 'glyphicon-ban-circle')]")).click();
    }

    @Test
    public void update_Password() throws Exception {
    	// Open the edit password dialogue box.
        String old_id = helper.driver.findElement(By.xpath("//tr[1]/td[1]")).getText();
        String password = helper.driver.findElement(By.xpath("//tr[1]/td[4]/div/input")).getAttribute("value");
        helper.driver.findElement(By.xpath("//tr[1]/td[7]/div/button[1]")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type=submit]")));

        String new_Password_string = "TestingPassword";

        // Input new password data and confirm the edit.
        helper.driver.findElement(By.id("field_password")).clear();
		helper.driver.findElement(By.id("field_password")).sendKeys(new_Password_string);
        
        helper.driver.findElement(By.cssSelector("button[type=submit]")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//pre[1]")));

        String message = helper.driver.findElement(By.xpath("//pre[1]")).getText();
        String new_id = helper.driver.findElement(By.xpath("//tr[1]/td[1]")).getText();
        String new_password = helper.driver.findElement(By.xpath("//tr[1]/td[4]/div/input")).getAttribute("value");

        assertEquals(old_id, new_id);
        assertEquals(new_password, new_Password_string); 	
    }

    @Test
    public void empty_Password() throws Exception {
        helper.driver.findElement(By.xpath("//tr[1]/td[7]/div/button[1]")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type=submit]")));

        helper.driver.findElement(By.id("field_password")).clear();
        WebElement submit = helper.driver.findElement(By.cssSelector("button[type=submit]"));
        assertEquals("true", submit.getAttribute("disabled"));
        helper.driver.findElement(By.xpath("//span[contains(@class, 'glyphicon-ban-circle')]")).click();
    }

    @Test
    public void cancel_Update() throws Exception {

        // Open the edit password dialogue box.
        String old_id = helper.driver.findElement(By.xpath("//tr[1]/td[1]")).getText();
        String site = helper.driver.findElement(By.xpath("//tr[1]/td[2]")).getText();
        String login = helper.driver.findElement(By.xpath("//tr[1]/td[3]")).getText();
        String password = helper.driver.findElement(By.xpath("//tr[1]/td[4]/div/input")).getAttribute("value");
        helper.driver.findElement(By.xpath("//tr[1]/td[7]/div/button[1]")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type=submit]")));

        String new_Site_string = "TestingSite";
        String new_Login_string = "TestingLogin";
        String new_Password_string = "TestingPassword";

        // Input new password data and confirm the edit.
        helper.driver.findElement(By.id("field_site")).clear();
		helper.driver.findElement(By.id("field_site")).sendKeys(new_Site_string);
        helper.driver.findElement(By.id("field_login")).clear();
        helper.driver.findElement(By.id("field_login")).sendKeys(new_Login_string);
        helper.driver.findElement(By.id("field_password")).clear();
		helper.driver.findElement(By.id("field_password")).sendKeys(new_Password_string);

        helper.driver.findElement(By.xpath("//span[contains(@class, 'glyphicon-ban-circle')]")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//pre[1]")));

        String message = helper.driver.findElement(By.xpath("//pre[1]")).getText();
        String new_id = helper.driver.findElement(By.xpath("//tr[1]/td[1]")).getText();
        String new_site = helper.driver.findElement(By.xpath("//tr[1]/td[2]")).getText();
        String new_login = helper.driver.findElement(By.xpath("//tr[1]/td[3]")).getText();
        String new_password = helper.driver.findElement(By.xpath("//tr[1]/td[4]/div/input")).getAttribute("value");

        assertEquals(old_id, new_id);
        assertEquals(new_site, new_Site_string);
        assertEquals(new_login, new_Login_string);
        assertEquals(new_password, new_Password_string);
    }


	@AfterClass
	public static void tearDown() throws Exception {
//		if(helper.isElementPresent(By.cssSelector("#site-" + siteName))){
//			helper.deleteSaturnVaultAccount(siteName, false);
//		}
//        helper.tearDown();
	}
}