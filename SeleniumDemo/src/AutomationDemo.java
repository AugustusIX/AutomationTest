import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@SuppressWarnings("unused")
public class AutomationDemo {
	public static WebDriver driver;
	public static String debuggerAdd;
	
	private static final StringBuilder sb = new StringBuilder();
	
	public static void main(String[] args) throws InterruptedException {
		//use config class
		Configuration configuration = new Configuration();
		
        // Retrieve config values for credentials
		String username = configuration.getUsername();
        String password = configuration.getPassword();
        
        // START of initialization
        String npi_path = "E:/WORK/SAB/CSV test files/NPI/";
        String ztt_path = "E:/WORK/SAB/CSV test files/Terr/";
        
        String b1 = "Chrome";
        String b2 = "Edge";
        String b3 = "Firefox";
        
        String p1 = "Home";
        String p2 = "Definitions";
        String p3 = "Datasets";
        String p4 = "Dashboards";
        
        String homePT = "Home - MapLab";
        String definitionPT = "Definition Manager - MapLab";
        String datasetPT = "Dataset Manager - MapLab";
        String dashboardPT = "Dashboards - MapLab";
        String fileUpPT = "File Upload - MapLab";
        String dsbPT = "Select Cohort - MapLab";
        String dsCreatePT = "Generate new dashboard - MapLab";
        
        // EDIT this per run
        String dataset_title = "ZTT FileUp" + " " + getDate();
        String dataset_desc = "Description here";
        String dataset_type = "npi";
        String csvFile = "Territory test V3" + ".csv";
        // END of data initialization
        
        // EXECUTE ACTIONS BELOW
        driver = chooseBrowser(b2);
		Thread.sleep(3000);
		
		//Choose login method
//        oktaLogin(username, password);
//        regularLogin(username, password);
        
        //create dataset via File Upload
		goToMain(driver, p3);
        fileUpload(driver, dataset_title, dataset_desc, dataset_type, ztt_path + csvFile);
        
//        waitForPageTitle(driver, "Dataset Manager - MapLab");
        
        // Close the browser
//        driver.quit();
        
        // END OF AUTOMATION
	}
	
	private static void oktaLogin (String username, String password) throws InterruptedException {
      driver.findElement(By.xpath("/html/body/main/section/div/div/div/div[2]/form[1]/button")).click();
      waitForPageTitle(driver, "Komodo Health - Sign In");
      driver.findElement(By.id("okta-signin-username")).sendKeys(username);
      driver.findElement(By.id("okta-signin-password")).sendKeys(password);
      driver.findElement(By.id("okta-signin-submit")).click();
      
      Thread.sleep(8000);
      waitForPageTitle(driver, "Komodo | We breathe life into data");
      Thread.sleep(3000);
      driver.findElement(By.id("onetrust-accept-btn-handler")).click();
      driver.findElement(By.xpath("//div[@data-testid='switcher.account.2']")).click();
      driver.findElement(By.xpath("//button[@data-testid='switcher.submit']")).click();
      waitForPageTitle(driver, "Home - MapLab");
      
      Thread.sleep(3000);
	}
	
	private static void regularLogin (String username, String password) throws InterruptedException {
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        System.out.println("username & password inputted");
        driver.findElement(By.name("action")).click();
        System.out.println("Logging in...");
        
        waitForPageTitle(driver, "Home - MapLab");
        Thread.sleep(2000);
        driver.findElement(By.id("onetrust-accept-btn-handler")).click();
        System.out.println("Home page loaded & cookie accepted");
        
        Thread.sleep(2000);
	}

	public static WebDriver chooseBrowser(String browser) {
		WebDriver driver = null;
        if (isPortAvailable(9222)) {
        	if ("Chrome".equals(browser)) {
				ChromeOptions browserOptions = new ChromeOptions(); //set options to Chrome
				browserOptions.addArguments("--incognito"); //open in incognito/private window
				browserOptions.addArguments("--remote-debugging-port=9222");
				
				driver = new ChromeDriver(browserOptions);
				driver.manage().window().maximize();
				
			} else if ("Edge".equals(browser)) {
				EdgeOptions browserOptions = new EdgeOptions(); //set options to Edge
				browserOptions.addArguments("--inprivate"); //open in incognito/private window
				browserOptions.addArguments("--remote-debugging-port=9222");
				browserOptions.setExperimentalOption("useAutomationExtension", false);
				browserOptions.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
				
				driver = new EdgeDriver(browserOptions);
				driver.manage().window().maximize();
				driver.get("https://apps-dev.komodohealth.com/maplab/");
				
				
				// getCapabilities will return all browser capabilities
				Capabilities cap=((EdgeDriver) driver).getCapabilities();

				// asMap method will return all capability in MAP
				Map<String, Object> myCap=cap.asMap();
				
				Object edgeOptions = myCap.get("ms:edgeOptions");
				
				if (edgeOptions instanceof Map) {
			    	Object debuggerAddress = ((Map<?, ?>) edgeOptions).get("debuggerAddress");
			        if (debuggerAddress != null) {
			        	debuggerAdd = debuggerAddress.toString();
			        	
			        } else {
			        	System.out.println("Debugger Address not found in capabilities.");
			        }
			    } else {
			    	System.out.println("ms:edgeOptions not found in capabilities.");
			    }
			} else if ("Firefox".equals(browser)) {
				System.setProperty("webdriver.gecko.driver", "E:/Programming files/browser drivers/geckodriver.exe");
				// Create a Firefox profile with private browsing enabled
		        FirefoxProfile profile = new FirefoxProfile();
		        profile.setPreference("browser.privatebrowsing.autostart", true);
		        
		        FirefoxOptions browserOptions = new FirefoxOptions(); //set options to Firefox
		        browserOptions.setProfile(profile);
		        driver = new FirefoxDriver(browserOptions);
		        driver.manage().window().maximize();
			} else {
				throw new IllegalArgumentException("Unsupported browser: " + browser);
	        }
		} else {
			if ("Chrome".equals(browser)) {
				// Create object of ChromeOptions Class
				ChromeOptions opt=new ChromeOptions();

				// pass the debuggerAddress and pass the port along with host. Since I am running test on local so using localhost
				opt.setExperimentalOption("debuggerAddress","localhost:9222 ");

				// pass ChromeOptions object to ChromeDriver constructor
				driver = new ChromeDriver(opt);
				
				System.out.println("isPortAvailable else, if executed");
			} else if ("Edge".equals(browser)) {
				// Create object of ChromeOptions Class
				EdgeOptions opt=new EdgeOptions();

				// pass the debuggerAddress and pass the port along with host. Since I am running test on local so using localhost
				opt.setExperimentalOption("debuggerAddress","localhost:9222 ");

				// pass ChromeOptions object to ChromeDriver constructor
				driver = new EdgeDriver(opt);
				
			} else {
				System.out.println("isPortAvailable else, else executed");
			}
		}
        return driver;
	}
	
	private static void fileUpload(WebDriver wDriver, String dsName, String dsDesc, String type, String filePath) throws InterruptedException {
		driver.findElement(By.xpath("//button[@data-testid='dashboard.NewDatasetButton']")).click(); //click Create Dataset dropdown
		driver.findElement(By.xpath("//li[@data-testid='dashboard.newDatasetButtonMenu.import_data']")).click(); //choose Import Data
		Thread.sleep(2000);
//		System.out.println("File Upload clicked. Waiting for page to load...");
//		waitForPageTitle(wDriver, "File Upload - MapLab");
//		System.out.println("File upload page loaded");
		
		boolean draft = driver.findElement(By.xpath("//div[@data-testid='modal']")).isDisplayed();
		
		if (draft) {
			driver.findElement(By.xpath("//div[@data-testid='modal.actions']//button")).click();
//			fileUp(wDriver, dsName, dsDesc, type, filePath);
		} else {
//			fileUp(wDriver, dsName, dsDesc, type, filePath);
		}
	}
	
	private static void fileUp(WebDriver wDriver, String dsName, String dsDesc, String type, String filePath) throws InterruptedException {
		String st1 = "NPI";
        String st2 = "NPI (no sequence ID)";
        String st3 = "NPI (Old)";
        String st4 = "Sequence ID test";
        String st5 = "ZTT";
        String st6 = "ZTT (no sequence ID)";
        String st7 = "ZTT (Old)";
        
		driver.findElement(By.name("datasetName")).sendKeys(dsName);
		System.out.println("Dataset name inputted");
		driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/form/nav/div/div[2]/div[1]/div[2]/div/div/p/span")).click();
		driver.findElement(By.xpath("//div[@data-testid='PageHeader.description']")).click();
		driver.findElement(By.tagName("textarea")).sendKeys(dsDesc);
		System.out.println("Dataset description inputted");
		Thread.sleep(2000);
		driver.findElement(By.xpath("//button[@data-testid='inline-editing.submit']")).click();
		System.out.println("Dataset description saved");
		driver.findElement(By.xpath("//div[@data-testid='file-upload.components.DatasetSchema.select']")).click();
		System.out.println("Select schema dropdown clicked");
		
		if (st1.equals(type)) {
			driver.findElement(By.xpath("//*[@id=\"menu-datasetSchema\"]/div[3]/ul/li[1]")).click();
			System.out.println("if npi executed");
		} else if (st2.equals(type)) {
			driver.findElement(By.xpath("//*[@id=\"menu-datasetSchema\"]/div[3]/ul/li[2]")).click();
			System.out.println("else if npi old executed");
		} else if (st3.equals(type)) {
			driver.findElement(By.xpath("//*[@id=\"menu-datasetSchema\"]/div[3]/ul/li[3]")).click();
			System.out.println("else if sequence id executed");
		} else if (st4.equals(type)) {
			driver.findElement(By.xpath("//*[@id=\"menu-datasetSchema\"]/div[3]/ul/li[4]")).click();
			System.out.println("else if sequence id executed");
		}else if (st5.equals(type)) {
			driver.findElement(By.xpath("//*[@id=\"menu-datasetSchema\"]/div[3]/ul/li[5]")).click();
			System.out.println("else if ztt executed");
		} else if (st6.equals(type)) {
			driver.findElement(By.xpath("//*[@id=\"menu-datasetSchema\"]/div[3]/ul/li[6]")).click();
			System.out.println("else if ztt old executed");
		} else if (st7.equals(type)) {
			driver.findElement(By.xpath("//*[@id=\"menu-datasetSchema\"]/div[3]/ul/li[7]")).click();
			System.out.println("else if ztt old executed");
		} else {
			System.out.println("Invalid type or wrong args. Check code");
		}
		
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@data-testid='modal.actions']//button")).click();
		System.out.println("Modal accept button clicked");
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@data-testid='uploader.dropzone']//input[@type='file']")).sendKeys(filePath);
		System.out.println("file selected");
		Thread.sleep(2000);
		
		By uploadStatus = By.xpath("//div[@data-testid='status.success']");
		
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBe(uploadStatus, "Complete"));
		
		// Find the dropdown button using the modified XPath expression
        WebElement dropdownButton = driver.findElement(By.xpath("//div[@data-testid='file-upload.FileUpload.select']//div[@role='button']"));

        // Scroll down the page
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0,250)");
        Thread.sleep(2000);

        // Use Actions class to perform the click
        Actions actions = new Actions(driver);
        actions.moveToElement(dropdownButton).click().perform();
		
		System.out.println("mapping schema...");
		
		Thread.sleep(2000);

		if ("npi".equals(type) || "npi no seq".equals(type) || "npi old".equals(type) || "sequence id".equals(type)) {
			driver.findElement(By.xpath("//div[contains(@data-field, 'npi')]//div[@data-testid='file-upload.FileUpload.select']")).click();
			driver.findElement(By.xpath("//div[contains(@data-field, 'npi')]//div[@data-testid='file-upload.FileUpload.select']//li[@data-value='npi']")).click();
		} else if ("ztt".equals(type) || "ztt no seq".equals(type) || "ztt old".equals(type)) {
			driver.findElement(By.xpath("//div[contains(@data-testid, 'ZIP')]//div[@data-testid='file-upload.FileUpload.select']")).click();
			driver.findElement(By.xpath("//div[contains(@data-testid, 'ZIP')]//div[@data-testid='file-upload.FileUpload.select']//li[@data-value='zip']")).click();
			driver.findElement(By.xpath("//div[contains(@data-testid, 'Territory')]//div[@data-testid='file-upload.FileUpload.select']")).click();
			driver.findElement(By.xpath("//div[contains(@data-testid, 'Territory')]//div[@data-testid='file-upload.FileUpload.select']//li[@data-value='territory']")).click();
		} else {
			System.out.println("Error starting in line 232");
		}
		
		Thread.sleep(3000);
		driver.findElement(By.xpath("//button[@data-testid='file-upload.FileUpload.generate-dataset']")).click();
		
		List<WebElement> fileupWarningModal = driver.findElements(By.xpath("//div[@data-testid='file-upload.components.PartialProceedModal']"));
		if (!fileupWarningModal.isEmpty()) {
			driver.findElement(By.xpath("//input[@value='option2']")).click();
			Thread.sleep(2000);
			driver.findElement(By.xpath("//div[@data-testid=''modal.actions]//button[contains(text(), 'Skip rows')]")).click();
		}
	}
	
	private static void viaCohortDef (WebDriver wDriver) {
		driver.findElement(By.xpath("//button[@data-testid='dashboard.NewDatasetButton']")).click(); //click Create Dataset dropdown
		System.out.println("Created Dataset dropdown clicked");
		
		driver.findElement(By.xpath("//li[@data-testid='dashboard.newDatasetButtonMenu.use-cohort-definition']")); //choose Use Cohort Definition
		waitForPageTitle(wDriver, "Select cohort - MapLab");
	}
	
	private static void fileupChangeFile(String filePath) {
		driver.findElement(By.xpath("//button[@data-testid='file-upload.components.FileChanger.change-file']//input[@type='file']")).sendKeys(filePath);		
	}
	
	private static void fileUpChangeSchema(String type) throws InterruptedException {
		driver.findElement(By.xpath("//div[@data-testid='file-upload.components.DatasetSchema.select']")).click();
		System.out.println("Select schema dropdown clicked");
		
		if ("npi".equals(type)) {
			driver.findElement(By.xpath("//*[@id=\"menu-datasetSchema\"]/div[3]/ul/li[1]")).click();
			System.out.println("if npi executed");
		} else if ("npi old".equals(type)) {
			driver.findElement(By.xpath("//*[@id=\"menu-datasetSchema\"]/div[3]/ul/li[2]")).click();
			System.out.println("else if npi old executed");
		} else if ("sequence id".equals(type)) {
			driver.findElement(By.xpath("//*[@id=\"menu-datasetSchema\"]/div[3]/ul/li[3]")).click();
			System.out.println("else if sequence id executed");
		} else if ("ztt".equals(type)) {
			driver.findElement(By.xpath("//*[@id=\"menu-datasetSchema\"]/div[3]/ul/li[4]")).click();
			System.out.println("else if ztt executed");
		} else if ("ztt old".equals(type)) {
			driver.findElement(By.xpath("//*[@id=\"menu-datasetSchema\"]/div[3]/ul/li[5]")).click();
			System.out.println("else if ztt old executed");
		} else {
			System.out.println("Invalid type or wrong args. Check code");
		}
		
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@data-testid='modal.actions']//button")).click();
		System.out.println("Modal accept button clicked");
	}
	
	private static void waitForPageTitle(WebDriver wDriver, String title) {
	    WebDriverWait wait = new WebDriverWait(wDriver, Duration.ofSeconds(15));
	    wait.until(ExpectedConditions.titleIs(title));
	}
	
	private static void goToMain(WebDriver driver, String listPage) {
		if (listPage.equals("Home")) {
			driver.findElement(By.xpath("//a[@data-testid='appbar.navigation.home']")).click();
		} else if (listPage.equals("Definitions")) {
			driver.findElement(By.xpath("//a[@data-testid='appbar.navigation.definitions']")).click();
		} else if (listPage.equals("Datasets")) {
			driver.findElement(By.xpath("//a[@data-testid='appbar.navigation.datasets']")).click();
		} else if (listPage.equals("Dashboard")) {
			driver.findElement(By.xpath("//a[@data-testid='appbar.navigation.dashboards']")).click();
		} else {
			System.out.println("Error in goToMain");
		}
	}
	
	// Method to check if a port is available (not in use)
    private static boolean isPortAvailable(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
	private static String getDate() {
    	DateFormat  dateFormat = new SimpleDateFormat("MdYYHHmmss");
		Date dateNow = new Date();
		
		String setdate = dateFormat.format(dateNow);
		
		return setdate;
	} 
	
	private static void printProgress(int seconds, String description) throws InterruptedException {
        int totalIterations = seconds * 10; // Assuming each iteration is 100 milliseconds
        for (int i = 0; i <= totalIterations; i++) {
            sb.setLength(0);
            for (int j = 0; j < i * 100 / totalIterations; j++) {
                sb.append("=");
            }
            Thread.sleep(100);
            double percentage = ((double) i / totalIterations) * 100;
            System.out.print(description + ": [" + String.format("%-100s", sb.toString()) + "] " + (int) percentage + "%");
            System.out.print("\r");
        }
    }
//	private static void waitForEnterKey() {
//        try {
//            System.in.read();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
	
//	private static boolean isButtonClicked(WebDriver driver, WebElement button) {
//  JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
//
//  // Execute JavaScript to check the click property of the button
//  return (Boolean) jsExecutor.executeScript("return arguments[0].clicked;", button);
//}
}

class Configuration {
	
    private Properties properties;

    public Configuration() {
        this.properties = new Properties();
        loadProperties();
    }

    private void loadProperties() {
        try (FileInputStream input = new FileInputStream("E:/Programming files/config1.properties")) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately, e.g., log it or throw a custom exception
        }
    }

    public String getUsername() {
        return properties.getProperty("username");
    }

    public String getPassword() {
        return properties.getProperty("password");
    }
}
