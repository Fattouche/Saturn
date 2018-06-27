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

public class TestSortPasswords {
    private TestHelper helper;
    // private List<String> sites;
    String[] sites = { "Asite", "Bsite", "Csite", "Dsite" };
    String[] logins = { "Alogin", "Blogin", "Clogin", "Dlogin" };
    String[] passwords = { "Apassword", "Bpassword", "Cpassword", "Dpassword" };
    private int num_accounts = sites.length;

    private String siteName;
    private List<List<String>> existingAccounts;

    @BeforeClass
    public void setUp() throws Exception {
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
    }

    @Test
    public void sortPasswordAccountsByID() throws Exception {

        // By site_sort = By.cssSelector("#site_sort");
        // assertTrue(helper.isElementPresent(site_sort));

        // helper.driver.findElement(site_sort).click();
        // helper.driver.findElement(site_sort).click();

        // Thread.sleep(3000);

        // // existingAccounts = helper.listSaturnVaultAccounts();

        // WebElement tablePasswords = helper.driver
        // .findElement(By.cssSelector(".table-responsive .jh-table.table.table-striped
        // tbody"));
        // List<WebElement> passwords = tablePasswords.findElements(By.tagName("tr"));
        // List<List<String>> stringPasswords = new ArrayList<List<String>>();
        // for (WebElement password : passwords) {
        // List<WebElement> columns = password.findElements(By.tagName("td"));
        // List<String> item = new ArrayList<String>();
        // for (WebElement column : columns) {
        // item.add(column.getText());
        // }
        // stringPasswords.add(item);
        // }

        // for (List<String> temp : stringPasswords) {
        // System.out.println(temp);
        // }

    }

    @Test
    public void SortPasswordAccountsBySites() throws Exception {
    }

    @Test
    public void SortPasswordAccountsByLogin() throws Exception {
    }

    @Test
    public void SortPasswordAccountsByPassword() throws Exception {
    }

    @Test
    public void SortPasswordAccountsByDateCreated() throws Exception {
    }

    @Test
    public void SortPasswordAccountsByDateModified() throws Exception {
    }

    @AfterClass
    public void tearDown() throws Exception {

        // Delete all created Saturn Vaults
        for (int i = 0; i < num_accounts; i++) {
            if (helper.isElementPresent(By.cssSelector("#site-" + sites[i]))) {
                helper.deleteSaturnVaultAccount(sites[i], false);
            }
        }
        helper.tearDown();
    }
}