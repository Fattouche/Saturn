package com.saturn;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.TakesScreenshot;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import java.io.File;
import java.util.*;
import static org.junit.Assert.*;
import java.lang.Integer;

public class TestSortPasswords {
    private TestHelper helper;
    // private List<String> sites;
    String[] sites = { "Asite", "Bsite", "Csite", "Dsite" };
    String[] logins = { "Alogin", "Blogin", "Clogin", "Dlogin" };
    String[] passwords = { "Apassword", "Bpassword", "Cpassword", "Dpassword" };
    private int num_accounts = sites.length;
    private boolean first_test = true;
    private boolean last_test = false;;

    private String siteName;
    private List<List<String>> existingAccounts;

    @Before
    public void setUp() throws Exception {
        if (first_test) {
            helper = new TestHelper();
            helper.driver.get(helper.url);

            helper.login();

            existingAccounts = helper.listSaturnVaultAccounts(false);

            // delete all existing saturn vaults
            for (List<String> temp : existingAccounts) {
                helper.deleteSaturnVaultAccount(temp.get(1), false);
            }

            // Create saturn vaults
            // int len = sites.length;
            for (int i = 0; i < num_accounts; i++) {
                helper.createSaturnVaultAccountNotUsingDefault(sites[i], logins[i], passwords[num_accounts - 1 - i]);
            }
            first_test = false;
        }

    }

    @Test
    public void sortPasswordAccountsByID() throws Exception {
        last_test = true;
        By id_sort = By.cssSelector("#id_sort");
        assertTrue(helper.isElementPresent(id_sort));

        // helper.driver.findElement(id_sort).click();

        existingAccounts = helper.listSaturnVaultAccounts(true);

        String first_id = "0";
        String last_id = "0";
        int len = existingAccounts.size();
        for (int i = 0; i < len; i++) {
            if (i == 0) {
                first_id = (existingAccounts.get(i)).get(0);
            }
            if (i == len - 1) {
                last_id = (existingAccounts.get(i)).get(0);

            }
        }

        assertTrue(Integer.parseInt(first_id) < Integer.parseInt(last_id));
        helper.driver.findElement(id_sort).click();

        existingAccounts = helper.listSaturnVaultAccounts(true);

        len = existingAccounts.size();
        for (int i = 0; i < len; i++) {
            if (i == 0) {
                first_id = (existingAccounts.get(i)).get(0);
            }
            if (i == len - 1) {
                last_id = (existingAccounts.get(i)).get(0);

            }
        }

        assertTrue(Integer.parseInt(first_id) > Integer.parseInt(last_id));

    }

    // @Test
    // public void SortPasswordAccountsBySites() throws Exception {
    // }

    // @Test
    // public void SortPasswordAccountsByLogin() throws Exception {
    // }

    // @Test
    // public void SortPasswordAccountsByPassword() throws Exception {
    // }

    // @Test
    // public void SortPasswordAccountsByDateCreated() throws Exception {
    // }

    // @Test
    // public void SortPasswordAccountsByDateModified() throws Exception {
    // }

    @After
    public void tearDown() throws Exception {
        if (last_test) {
            // Delete all created Saturn Vaults
            for (int i = 0; i < num_accounts; i++) {
                if (helper.isElementPresent(By.cssSelector("#site-" + sites[i]))) {
                    helper.deleteSaturnVaultAccount(sites[i], false);
                }
            }
            helper.tearDown();
        }
    }
}