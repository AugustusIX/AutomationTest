package com.seleniumtraining.utility;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

public class BrowserFactory {
	public static String debuggerAdd;
	
	// Method to check if a port is available (not in use)
    private static boolean isPortAvailable(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }
	
	@SuppressWarnings("deprecation")
	public static WebDriver startBrowser(WebDriver driver, String browserName, String appURL) {
		if (isPortAvailable(9222)) {
			if ("Chrome".equals(browserName)) {
				System.setProperty("webdriver.chrome.driver", "./Drivers/chromedriver.exe");
				ChromeOptions options = new ChromeOptions();
				options.addArguments("--incognito");
				options.addArguments("--remote-debugging-port=9222");
				options.addArguments("--remote-allow-origins=*");
				
				driver = new ChromeDriver(options);
			    
			} else if ("Edge".equals(browserName)) {
				System.setProperty("webdriver.edge.driver", "./Drivers/msedgedriver.exe");
				EdgeOptions options = new EdgeOptions();
				options.addArguments("--inprivate");
				options.addArguments("--remote-debugging-port=9222");
				options.addArguments("--remote-allow-origins=*");
				
				driver = new EdgeDriver(options);
				
				// getCapabilities will return all browser capabilities
				Capabilities cap=((EdgeDriver) driver).getCapabilities();

				// asMap method will return all capability in MAP
				Map<String, Object> myCap=cap.asMap();
				
				Object edgeOptions = myCap.get("ms:edgeOptions");
				
				if (edgeOptions instanceof Map) {
			    	Object debuggerAddress = ((Map<?, ?>) edgeOptions).get("debuggerAddress");
			        if (debuggerAddress != null) {
			        	debuggerAdd = debuggerAddress.toString();
			        	System.out.println("Debugger Address from openBrowser(): " + debuggerAddress);
			        	
			        } else {
			        	System.out.println("Debugger Address not found in capabilities.");
			        }
			    } else {
			    	System.out.println("ms:edgeOptions not found in capabilities.");
			    }
				
			} else if ("Firefox".equals(browserName)) {
				System.setProperty("webdriver.firefox.driver", "./Drivers/geckodriver.exe");
				FirefoxProfile profile = new FirefoxProfile();
		        profile.setPreference("browser.privatebrowsing.autostart", true);
		        
				FirefoxOptions options = new FirefoxOptions();
				options.setProfile(profile);
				
				driver = new FirefoxDriver(options);
				
			} else {
				System.out.println("BrowserFactory() else statement executed");
			}
			
			driver.manage().window().maximize();
			driver.get(appURL);
			driver.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
			
//			return driver;
		} else {
			System.out.println("browser is already running.");
			
			if ("Chrome".equals(browserName)) {
				
			} else if ("Edge".equals(browserName)) {
				// Create object of ChromeOptions Class
				EdgeOptions opt=new EdgeOptions();

				// pass the debuggerAddress and pass the port along with host. Since I am running test on local so using localhost
				opt.setExperimentalOption("debuggerAddress","localhost:9222 ");

				// pass ChromeOptions object to ChromeDriver constructor
				driver=new EdgeDriver(opt);
			}  else {
				System.out.println("BrowserFactory() else -> else statement executed");
			}
		}
		return driver;
	}
	
	public static void quitBrowser(WebDriver driver) {
		System.out.println("This is when they exit the browser, but I ain't gonna do that.");
		//		driver.quit();
	}
}
