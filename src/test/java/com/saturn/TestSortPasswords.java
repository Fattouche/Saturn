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
    // String[] sites = { "Asite", "Bsite", "Csite", "Dsite" };
    // String[] logins = { "Alogin", "Blogin", "Clogin", "Dlogin" };
    // String[] passwords = { "Apassword", "Bpassword", "Cpassword", "Dpassword" };
    // private int num_accounts = sites.length;
    // private boolean first_test = true;
    // private boolean last_test = false;
    private List<List<String>> existingAccounts;
    private static String[] sites = { "Asite", "Bsite", "Csite", "Dsite" };
    private static int num_accounts = sites.length;

    @BeforeClass
    public static void setUpClass() throws Exception {

        TestHelper helper;
        String[] sites = { "Asite", "Bsite", "Csite", "Dsite" };
        String[] logins = { "Alogin", "Blogin", "Clogin", "Dlogin" };
        String[] passwords = { "Apassword", "Bpassword", "Cpassword", "Dpassword" };
        int num_accounts = sites.length;
        List<List<String>> existingAccounts;

        // if (first_test == true) {
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
            helper.createSaturnVaultAccountNotUsingDefault(sites[i], logins[i], passwords[i]);
        }

        helper.tearDown();

    }

    @Before
    public void setUp() throws Exception {
        helper = new TestHelper();
        helper.driver.get(helper.url);
        helper.login();
    }

    @Test
    public void sortPasswordAccountsByID() throws Exception {
        System.out.println("Running sortPasswordAccountsById");

        int id_index = 0;

        // ensure ascending
        getListAndAssertBy(true, true, id_index);

        // find and click
        By id_sort = By.cssSelector("#id_sort");
        assertTrue(helper.isElementPresent(id_sort));
        helper.driver.findElement(id_sort).click(); // single click because it auto starts at ID ascending

        // ensure descending
        getListAndAssertBy(false, true, id_index);

    }

    // @Test
    public void SortPasswordAccountsBySites() throws Exception {
        System.out.println("Running sortPasswordAccountsBySite");

        // System.out.println("In site");
        // last_test = true;
        int site_index = 1;

        // ensure ascending
        getListAndAssertBy(true, false, site_index);

        // find and click
        By site_sort = By.cssSelector("#site_sort");
        assertTrue(helper.isElementPresent(site_sort));
        helper.driver.findElement(site_sort).click(); // double click because first always ascends
        helper.driver.findElement(site_sort).click();

        // ensure descending
        getListAndAssertBy(false, false, site_index);

    }

    // @Test
    public void SortPasswordAccountsByLogin() throws Exception {
        System.out.println("Running sortPasswordAccountsByLogin");

        // last_test = true;
        int login_index = 2;

        // ensure ascending
        getListAndAssertBy(true, false, login_index);

        // find and click
        By login_sort = By.cssSelector("#login_sort");
        assertTrue(helper.isElementPresent(login_sort));
        helper.driver.findElement(login_sort).click();
        helper.driver.findElement(login_sort).click();

        // ensure descending
        getListAndAssertBy(false, false, login_index);

    }

    // @Test
    public void SortPasswordAccountsByPassword() throws Exception {
        System.out.println("Running sortPasswordAccountsByPassword");

        // last_test = true;
        int password_index = 1;

        // ensure ascending
        getListAndAssertBy(true, false, password_index);

        // find and click
        By password_sort = By.cssSelector("#password_sort");
        assertTrue(helper.isElementPresent(password_sort));
        helper.driver.findElement(password_sort).click();
        helper.driver.findElement(password_sort).click();

        // ensure descending
        getListAndAssertBy(false, false, password_index);
    }

    // @Test
    public void SortPasswordAccountsByDateCreated() throws Exception {

        int created_index = 1;

        // ensure ascending
        getListAndAssertBy(true, false, created_index);

        // find and click
        By created_sort = By.cssSelector("#created_sort");
        assertTrue(helper.isElementPresent(created_sort));
        helper.driver.findElement(created_sort).click();
        helper.driver.findElement(created_sort).click();

        // ensure descending
        getListAndAssertBy(false, false, created_index);
    }

    // @Test
    public void SortPasswordAccountsByDateModified() throws Exception {
        int modified_index = 1;

        // ensure ascending
        getListAndAssertBy(true, false, modified_index);

        // find and click
        By modified_sort = By.cssSelector("#modified_sort");
        assertTrue(helper.isElementPresent(modified_sort));
        helper.driver.findElement(modified_sort).click();
        helper.driver.findElement(modified_sort).click();

        // ensure descending
        getListAndAssertBy(false, false, modified_index);
    }

    public void getListAndAssertBy(boolean ascending, boolean isNum, int index) throws Exception {
        boolean stay_on_page = ascending ? false : true;
        existingAccounts = helper.listSaturnVaultAccounts(stay_on_page);

        // if(!isNum){
        String first = existingAccounts.get(0).get(index);
        String last = existingAccounts.get(existingAccounts.size() - 1).get(index);

        // ascending or descending
        boolean stringStatement = ascending ? first.toLowerCase().charAt(0) < last.toLowerCase().charAt(0)
                : first.toLowerCase().charAt(0) > last.toLowerCase().charAt(0);

        boolean statement = isNum
                ? (ascending ? Integer.parseInt(first) < Integer.parseInt(last)
                        : Integer.parseInt(first) > Integer.parseInt(last))
                : stringStatement;

        // }

        // System.out.println(first);
        // System.out.println(last);
        // System.out.println(isNum);
        // System.out.println(ascending);
        // System.out.println(statement);

        assertTrue(statement);
    }

    @After
    public void tearDown() throws Exception {
        helper.tearDown();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        // if (last_test == true) {
        // Delete all created Saturn Vaults
        TestHelper teardown_helper = new TestHelper();
        teardown_helper.driver.get(teardown_helper.url);

        teardown_helper.login();
        teardown_helper.driver.get(teardown_helper.url + "/#/saturn-vault");

        for (int i = 0; i < num_accounts; i++) {
            if (teardown_helper.isElementPresent(By.cssSelector("#site-" + sites[i]))) {
                teardown_helper.deleteSaturnVaultAccount(sites[i], false);
            }
        }
        teardown_helper.tearDown();
        // }
    }
}