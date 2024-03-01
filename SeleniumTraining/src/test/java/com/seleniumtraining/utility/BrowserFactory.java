package com.seleniumtraining.utility;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class BrowserFactory {
	
	@SuppressWarnings("deprecation")
	public static WebDriver startBrowser(WebDriver driver, String browserName, String appURL) {
		if ("Chrome".equals(browserName)) {
			System.setProperty("webdriver.chrome.driver", "./Drivers/chromedriver.exe");
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--inprivate");
//			options.addArguments("--remote-debugging-port=9222");
			options.addArguments("--remote-allow-origins=*");
			
			driver = new ChromeDriver(options);
		    
		} else if ("Edge".equals(browserName)) {
			System.setProperty("webdriver.edge.driver", "./Drivers/msedgedriver.exe");
			EdgeOptions options = new EdgeOptions();
			options.addArguments("--inprivate");
//			options.addArguments("--remote-debugging-port=9222");
			options.addArguments("--remote-allow-origins=*");
			
			driver = new EdgeDriver(options);
			
		} else if ("Firefox".equals(browserName)) {
			System.setProperty("webdriver.firefox.driver", "./Drivers/geckodriver.exe");
			FirefoxOptions options = new FirefoxOptions();
			options.addArguments("--inprivate");
//			options.addArguments("--remote-debugging-port=9222");
			options.addArguments("--remote-allow-origins=*");
			
			driver = new FirefoxDriver(options);
			
		} else {
			System.out.println("BrowserFactory() else statement executed");
		}
		
		driver.manage().window().maximize();
		driver.get(appURL);
		driver.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
		
		return driver;
	}
	
	public static void quitBrowser(WebDriver driver) {
		System.out.println("This is when they exit the browser, but I ain't gonna do that.");
		//		driver.quit();
	}
}
