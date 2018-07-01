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


public class TestListPasswords {
    private static TestHelper helper;
    private static int numAccounts = 10;

	@BeforeClass
	public static void setUp() throws Exception {
        helper = new TestHelper();
        helper.driver.get(helper.url);
        helper.login();
    }
    
    //List all saturn vault passwords/accounts and test that it isn't empty after we made an account
	@Test
	public void listPasswordsTest(){
        helper.createAccounts(numAccounts);
        List<List<String>> passwords = helper.listSaturnVaultAccountsOnPage(1);
        assertEquals(passwords.size(), numAccounts);
        for(int i=0;i<passwords.size();i++){
            assertEquals(passwords.get(i).get(1), "Site"+i);
        }
    }
	
	@AfterClass
	public static void tearDown() throws Exception {
        for(int i=0;i<numAccounts;i++){
            String siteName = "Site"+i;
			helper.deleteSaturnVaultAccount(siteName, false);
		}
        helper.tearDown();
	}
}