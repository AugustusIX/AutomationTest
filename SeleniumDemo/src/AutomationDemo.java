import org.openqa.selenium.By;
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
import java.util.List;
import java.util.Properties;

public class AutomationDemo {
	static WebDriver driver;
	
	public static void main(String[] args) throws InterruptedException {
		//use config class
		Configuration configuration = new Configuration();
		
        // Retrieve config values for credentials
		String username = configuration.getUsername();
        String password = configuration.getPassword();	
        
        // Initialize WebDriver instance
        WebDriver existingDriver = null;
        
        chooseBrowser("edge", existingDriver);
		System.out.println("browser opened");
		
        //--AUTOMATION START--- open website
        driver.get("https://apps-dev.komodohealth.com/studio/");
        System.out.println("website loaded");
        
        // Pause before executing next code
        waitForPageTitle(driver, "Log in | Komodo App Suite Login");
        System.out.println("Initiating log in...");
        
        oktaLogin(username, password);
//        regularLogin(username, password);
        
        Thread.sleep(2000);
        
        //Navigate to home/definitions/datasets/dashboards
        goToMain("datasets");
        //create dataset args = driver, name, desc, npi/npi old/etc, path
        fileUpload(driver, "Automated FileUp", "Automation testing", "npi", "E:/WORK/SAB/CSV test files/NPI/Valid NPIs 99mb.csv");
        
        waitForPageTitle(driver, "Dataset Manager - MapLab");
        
        goToMain("datasets"); 
        // Close the browser
//        driver.quit();
        // --- END OF AUTOMATION ---
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
      driver.findElement(By.xpath("/html/body/div[4]/div[3]/div/div[2]/ul/div[2]/div")).click();
      driver.findElement(By.xpath("/html/body/div[4]/div[3]/div/div[3]/button[2]")).click();
      waitForPageTitle(driver, "Home - MapLab");
	}
	
	@SuppressWarnings("unused")
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
	}

	public static void chooseBrowser(String browser, WebDriver existingDriver) {
		if (existingDriver == null) {
			if ("chrome".equals(browser)) {
				ChromeOptions browserOptions = new ChromeOptions(); //set options to Chrome
				browserOptions.addArguments("--incognito"); //open in incognito/private window
				driver = new ChromeDriver(browserOptions);
				driver.manage().window().maximize();
				
			} else if ("edge".equals(browser)) {
				EdgeOptions browserOptions = new EdgeOptions(); //set options to Edge
				browserOptions.addArguments("--inprivate"); //open in incognito/private window
				driver = new EdgeDriver(browserOptions);
				driver.manage().window().maximize();
				
			} else if ("firefox".equals(browser)) {
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
			System.out.println("existingDriver is null");
		} else {
			driver = existingDriver;
			System.out.println("has existingDriver");
		}
	}
	
	private static void waitForPageTitle(WebDriver wDriver, String title) {
	    WebDriverWait wait = new WebDriverWait(wDriver, Duration.ofSeconds(15));
	    wait.until(ExpectedConditions.titleIs(title));
	}
	
//	private static boolean isButtonClicked(WebDriver driver, WebElement button) {
//        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
//
//        // Execute JavaScript to check the click property of the button
//        return (Boolean) jsExecutor.executeScript("return arguments[0].clicked;", button);
//    }
	
	private static void goToMain(String listPage) {
		if (listPage.equals("home")) {
			driver.findElement(By.xpath("//a[@data-testid='appbar.navigation.home']")).click();
			System.out.println("Home in Navbar clicked");
		} else if (listPage.equals("definitions")) {
			driver.findElement(By.xpath("//a[@data-testid='appbar.navigation.definitions']")).click();
			System.out.println("Definitions in Navbar clicked");
		} else if (listPage.equals("datasets")) {
			driver.findElement(By.xpath("//a[@data-testid='appbar.navigation.datasets']")).click();
			System.out.println("Datasets in Navbar clicked");
		} else if (listPage.equals("dashboard")) {
			driver.findElement(By.xpath("//a[@data-testid='appbar.navigation.dashboards']")).click();
			System.out.println("Dashboard in Navbar clicked");
		} else {
			System.out.println("Error in goToMain");
		}
	}
	
	private static void fileUpload(WebDriver wDriver, String dsName, String dsDesc, String type, String filePath) throws InterruptedException {
		List<WebElement> draft = driver.findElements(By.xpath("//div[@data-testid='modal']"));
		
		if (!draft.isEmpty()) {
			driver.findElement(By.xpath("//div[@data-testid='modal.actions']//button")).click();
		}
		
		driver.findElement(By.xpath("//button[@data-testid='dashboard.NewDatasetButton']")).click(); //click Create Dataset dropdown
		System.out.println("Created Dataset dropdown clicked");
		
		driver.findElement(By.xpath("//li[@data-testid='dashboard.newDatasetButtonMenu.import_data']")).click(); //choose Import Data
		System.out.println("File Upload clicked. Waiting for page to load...");
		waitForPageTitle(wDriver, "File Upload - MapLab");
		System.out.println("File upload page loaded");
		
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
		Thread.sleep(2000);
		driver.findElement(By.xpath("//div[@data-testid='uploader.dropzone']//input[@type='file']")).sendKeys(filePath);
		System.out.println("file selected");
		Thread.sleep(2000);
		
		By uploadStatus = By.xpath("//div[@data-testid='status.success']");
		
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.textToBe(uploadStatus, "Complete"));
		System.out.println("Code test successful");
		
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
		driver.findElement(By.xpath("//*[@id=\"menu-\"]/div[3]/ul/li[2]")).click();
		System.out.println("Schema mapped");
		Thread.sleep(2000);
		
		List<WebElement> singleColCheck = driver.findElements(By.xpath("//div[contains(@data-field, 'npi')]"));
		if ("npi".equals(type) || "npi old".equals(type) || "sequence id".equals(type)) {
			if (!singleColCheck.isEmpty()) {
				driver.findElement(By.xpath("//div[@data-testid='file-upload.FileUpload.select']//input")).sendKeys("NPI");
			} else {
				driver.findElement(By.xpath("//div[contains(@data-field, 'npi')]//div[@data-testid='file-upload.FileUpload.select']//input")).sendKeys("NPI");
			}
		} else if ("ztt".equals(type) || "ztt old".equals(type)) {
			driver.findElement(By.xpath("//div[contains(@data-testid, 'zip')]//div[@data-testid='file-upload.FileUpload.select']//input")).sendKeys("Zip");
		} else {
			System.out.println("Error starting in line 232");
		}
		
		driver.findElement(By.xpath("//button[@data-testid='file-upload.FileUpload.generate-dataset']")).click();
		
		List<WebElement> fileupWarningModal = driver.findElements(By.xpath("//div[@data-testid='file-upload.components.PartialProceedModal']"));
		if (!fileupWarningModal.isEmpty()) {
			driver.findElement(By.xpath("//input[@value='option2']")).click();
			Thread.sleep(2000);
			driver.findElement(By.xpath("//div[@data-testid=''modal.actions]//button[contains(text(), 'Skip rows')]")).click();
		}
	}
	
	@SuppressWarnings("unused")
	private static void viaCohortDef (WebDriver wDriver) {
		driver.findElement(By.xpath("//button[@data-testid='dashboard.NewDatasetButton']")).click(); //click Create Dataset dropdown
		System.out.println("Created Dataset dropdown clicked");
		
		driver.findElement(By.xpath("//li[@data-testid='dashboard.newDatasetButtonMenu.use-cohort-definition']")); //choose Use Cohort Definition
		waitForPageTitle(wDriver, "Select cohort - MapLab");
	}
	
	@SuppressWarnings("unused")
	private static void fileupChangeFile(String filePath) {
		driver.findElement(By.xpath("//button[@data-testid='file-upload.components.FileChanger.change-file']//input[@type='file']")).sendKeys(filePath);		
	}
	
	@SuppressWarnings("unused")
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
//	private static void waitForEnterKey() {
//        try {
//            System.in.read();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
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
