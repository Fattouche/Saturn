package com.saturn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class TestCopyPass {
	private TestHelper helper;
	public String siteName1;
	public String siteName2;

	@Before
	public void setUp() throws Exception {
		helper = new TestHelper();
		helper.driver.get(helper.url);
	}

	@Test
	public void clickCopiesPassword() {
		siteName1 = RandomStringUtils.randomAlphanumeric(8);
		siteName2 = RandomStringUtils.randomAlphanumeric(8);
		helper.login();
		helper.createSaturnVaultAccount(siteName1);

		String site2Pass = "voldey";
		helper.createSaturnVaultAccount(siteName2, "harry", site2Pass);
		
		// password element exists for both
		By saturnPass1 = By.cssSelector("#site-" + siteName1 + " .saturnpass-password");
		assertTrue(helper.isElementPresent(saturnPass1));
		By saturnPass2 = By.cssSelector("#site-" + siteName2 + " .saturnpass-password");
		assertTrue(helper.isElementPresent(saturnPass2));

		// click on the first password
		helper.driver.findElement(saturnPass1).click();

		String contents1 = getClipboardContents();
		System.out.println("Actual Pass: '" + helper.defaultPass +  "' --- content of clipboard: '" + contents1 + "'");
		// assert the clipboard contents match the first password
		assertEquals(contents1, helper.defaultPass);
		
		// click on the second password
		helper.driver.findElement(saturnPass2).click();
		
		String contents2 = getClipboardContents();
		System.out.println("Actual Pass: '" + site2Pass +  "' --- content of clipboard: '" + contents2 + "'");
		// assert the clipboard contents match the second password
		assertEquals(contents2, site2Pass);
	}
	
	public String getClipboardContents(){
		// paste the clipboard contents in an input box
		// we have to do this because running the tests headless does not allow us to access the clipboard in the traditional way, using the clipboard class
		// we use the site-name input field of the new saturn vault page instead
		helper.driver.get(helper.url+"/#/saturn-vault/new");
		assertTrue(helper.isElementPresent(By.id("field_site")));
		WebElement e = helper.driver.findElement(By.id("field_site"));
		// click the input box and paste into it
		e.click();
		e.sendKeys(Keys.CONTROL + "v");
		String text = e.getAttribute("value");	
		// dismiss the modal 
		helper.driver.findElement(By.cssSelector("form[name=editForm] .cancel-button")).click();
			// wait for the modal to leave
		new WebDriverWait(helper.driver, 3).until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("form[name='editForm']")));

		return text;
	}
	
	
	@After
	public void tearDown() throws Exception {
		if(helper.isElementPresent(By.cssSelector("#site-" + siteName1))){
			helper.deleteSaturnVaultAccount(siteName1, false);
		}
		if(helper.isElementPresent(By.cssSelector("#site-" + siteName2))){
			helper.deleteSaturnVaultAccount(siteName2, false);
		}
		helper.tearDown();
	}
}
