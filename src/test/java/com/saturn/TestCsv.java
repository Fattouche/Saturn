package com.saturn;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.Assert.*;

public class TestCsv {
	private TestHelper helper;
	private String siteName;
	private String siteUrl = "http://testurl.com/";
	private String siteFull;

	@Before
	public void setUp() throws Exception {
		helper = new TestHelper();
		helper.driver.get(helper.url);
		siteName = "Site"+ RandomStringUtils.randomAlphanumeric(8);
		siteFull = siteName + " (" + siteUrl + ")";
	}

	// Creates a CSV file in the tmp directory so Selenium can access it
	public File createSiteCsv() throws IOException {
		File csvFile = File.createTempFile("testimport", ".csv");
		PrintWriter writer = new PrintWriter(csvFile);
		writer.println("name, url, username, password");
		writer.println("\"" + siteName + "\",\"" + siteUrl + "\",\"" + helper.defaultLogin + "\",\"" + helper.defaultPass + "\"");
		writer.close();

		return csvFile;
	}

	@Test
	public void importButtonTest() {
		helper.login();
		helper.driver.get(helper.url + "/#/saturn-vault");

		helper.driver.findElement(By.cssSelector(".import-csv")).click();

		//after creation ensure the account is in the list
		new WebDriverWait(helper.driver, 3).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("form[name='importForm']")));
		WebElement form = helper.driver.findElement(By.cssSelector("form[name='importForm']"));

		assertEquals(helper.url + "/#/saturn-vault/import", helper.driver.getCurrentUrl());
		assertTrue(form.isDisplayed());
	}

	@Test
	public void importCsvTest() throws Exception {
		helper.login();
		helper.driver.get(helper.url + "/#/saturn-vault/import?sort=createdDate,desc");

		File csvFile = createSiteCsv();

		helper.driver.findElement(By.id("upload_file")).sendKeys(csvFile.getAbsolutePath());
		helper.driver.findElement(By.cssSelector("form[name='importForm']")).submit();

		//after creation ensure the account is in the list
		new WebDriverWait(helper.driver, 3).until(ExpectedConditions.visibilityOfElementLocated(By.id("site-" + siteFull)));
		WebElement siteNameCell = helper.driver.findElement(By.cssSelector("tr[id='site-" + siteFull + "'] > td:nth-child(2)"));

		assertEquals(siteFull, siteNameCell.getText());

		csvFile.delete();
	}

	@Test
	public void cancelImportTest() throws Exception {
		helper.login();
		helper.driver.get(helper.url + "/#/saturn-vault/import?sort=createdDate,desc");
		File csvFile = createSiteCsv();

		helper.driver.findElement(By.id("upload_file")).sendKeys(csvFile.getAbsolutePath());
		helper.driver.findElement(By.cssSelector("button.btn-cancel")).click();

		new WebDriverWait(helper.driver, 3).until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("form[name='importForm']")));

		assertFalse(helper.isElementPresent(By.id("site-" + siteFull)));

		csvFile.delete();
	}

	@Test
	public void exportCsvTest() {
		helper.login();
		helper.createSaturnVaultAccount(siteName);
		
		helper.driver.get(helper.url + "/#/saturn-vault");

		// Find the export button and get link
		String exportUrl = helper.driver.findElement(By.cssSelector(".export-csv")).getAttribute("href");

		// Selenium doesn't reliable way to handle file downloads, so we'll use JS
		JavascriptExecutor js = (JavascriptExecutor)helper.driver;
		String csvContent = js.executeScript(
			"var req = new XMLHttpRequest();" +
			"req.open('GET', '" + exportUrl + "', false);" +
			"req.send();" +
			"return req.response;"
		).toString();

		assertTrue(csvContent.contains("\"" + siteName + "\",\"" + helper.defaultPass + "\",\"\",\"" + helper.defaultLogin + "\""));
	}
	
	@After
	public void tearDown() throws Exception {
		helper.driver.get(helper.url + "/#/saturn-vault?sort=createdDate,desc");
		
		if(helper.isElementPresent(By.cssSelector("#site-" + siteName))){
			helper.deleteSaturnVaultAccount(siteName, false);
		}
		
		if(helper.isElementPresent(By.cssSelector("[id='site-" + siteFull + "']"))){
			helper.deleteSaturnVaultAccount(siteFull, false);
		}

		helper.tearDown();
	}
}
