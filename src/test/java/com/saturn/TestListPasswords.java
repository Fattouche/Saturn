package com.saturn;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.*;
import static org.junit.Assert.*;

public class TestListPasswords {
    private TestHelper helper;
    private List<String> expectedColumns;
    private String siteName;

    @Before
    public void setUp() throws Exception {
        helper = new TestHelper();
        initExpectedColumns();
        helper.driver.get(helper.url);
        siteName = "Site" + RandomStringUtils.randomAlphanumeric(8);
        helper.login();
        helper.createSaturnVaultAccount(siteName);
    }

    // List all saturn vault passwords/accounts and test that we can see the
    // expected columns
    @Test
    public void listPasswordsTest() {
        List<List<String>> passwords = helper.listSaturnVaultAccounts(false);
        System.out.println(Arrays.toString(passwords.toArray()));
        assertFalse(passwords.isEmpty());
        List<String> columns = helper.getSaturnVaultColumns();
        assertTrue(columns.containsAll(expectedColumns));
    }

    private void initExpectedColumns() {
        expectedColumns = new ArrayList<String>();
        expectedColumns.add("ID");
        expectedColumns.add("Site");
        expectedColumns.add("Login");
        expectedColumns.add("Password");
        expectedColumns.add("Created Date");
        expectedColumns.add("Last Modified Date");
    }

    @After
    public void tearDown() throws Exception {
        if (helper.isElementPresent(By.cssSelector("#site-" + siteName))) {
            helper.deleteSaturnVaultAccount(siteName, false);
        }
        helper.tearDown();
    }
}