package com.seleniumtraining.pages;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.seleniumtraining.utility.BrowserFactory;
import com.seleniumtraining.utility.ConfigDataProvider;

public class BaseTest {
	public WebDriver driver;
	public ConfigDataProvider config = new ConfigDataProvider();
	
	@BeforeClass
	public void setup() {
		driver = BrowserFactory.startBrowser(driver, config.getBrowser(), config.getURL());
	}
	
	@AfterClass
	public void tearDown() {
		BrowserFactory.quitBrowser(driver);
	}
}
