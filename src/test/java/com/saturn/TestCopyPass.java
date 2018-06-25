package com.saturn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;


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
		try {
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			Clipboard clipboard = toolkit.getSystemClipboard();
			return (String) clipboard.getData(DataFlavor.stringFlavor);
		} catch(Exception e){
			System.err.println(e.getMessage());
			return "";
		}
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
