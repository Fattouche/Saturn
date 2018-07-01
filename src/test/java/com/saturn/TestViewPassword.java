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

public class TestViewPassword {
    private static TestHelper helper;
    private static List<String> expectedPasswords;
    private static String siteName;

    @BeforeClass
    public static void setUp() throws Exception {
        helper = new TestHelper();
        helper.driver.get(helper.url);
        siteName = "Site" + RandomStringUtils.randomAlphanumeric(8);
        helper.login();
        helper.createSaturnVaultAccount(siteName);
    }
    
    //Test that we can see the specific details for a site
	@Test
	public void viewPasswordTest(){
		List<List<String>> passwords = helper.listSaturnVaultAccountsOnPageOnPage(1, false);
        assertFalse(passwords.isEmpty());
        boolean found = false;
        for (List<String> items : passwords) {
            assertTrue(isInteger(items.get(0)));
            if (items.get(2).equals(helper.defaultLogin)) {
                assertTrue(items.get(1).contains("Site"));
                found = true;
            }
        }
        assertTrue(found);
    }

    // Test that all of the expected columns are shown to the user
    @Test
    public void checkColumnsTest() {
        List<String> columns = helper.getSaturnVaultColumns();
        assertTrue(columns.containsAll(helper.expectedColumns()));
    }

    public static boolean isInteger(String s) {
        boolean isValidInteger = false;
        try {
            Integer.parseInt(s);

            // s is a valid integer

            isValidInteger = true;
        } catch (NumberFormatException ex) {
            // s is not an integer
        }

        return isValidInteger;
    }

    @AfterClass
    public static void tearDown() throws Exception {
        if (helper.isElementPresent(By.cssSelector("#site-" + siteName))) {
            helper.deleteSaturnVaultAccount(siteName, false);
        }
        helper.tearDown();
    }
}