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

public class TestDeleteAccount {
	private TestHelper helper;
	public String siteName;

	@Before
	public void setUp() throws Exception {
		helper = new TestHelper();
		helper.driver.get(helper.url);
		siteName = "Site"+ RandomStringUtils.randomAlphanumeric(8);
	}

	@Test
	public void deleteSaturnVaultAccountTest(){
		helper.login();
		helper.createSaturnVaultAccount(siteName);
		// dont hit the cancel button
		helper.deleteSaturnVaultAccount(siteName, false);
		//		after deleting the vault, the row representing it is not there.
		assertFalse(helper.isElementPresent(By.cssSelector("#site-" + siteName + " .delete-vault")));
	}
	public void deleteSaturnVaultAccountButCancelTest(){
		helper.login();

		helper.createSaturnVaultAccount(siteName);
		// hit the cancel button
		helper.deleteSaturnVaultAccount(siteName, true);
		//		after canceling  the vault deletion, the row representing it is  there.
		assertTrue(helper.isElementPresent(By.cssSelector("#site-" + siteName + " .delete-vault")));
	}
	
	
	@After
	public void tearDown() throws Exception {
		if(helper.isElementPresent(By.cssSelector("site-" + siteName))){
			// cleanup - delete the site
			helper.deleteSaturnVaultAccount(siteName, false);
		}
		helper.tearDown();
	}
}
