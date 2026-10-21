package genericUtility;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class WebDriverUtility {

	WebDriver driver = null;
	public WebDriver launchBrowser(String browser) {
		if(browser.equalsIgnoreCase("chrome")) {
			final Map<String, Object> chromePrefs = new HashMap<>();

			chromePrefs.put("credentials_enable_service", false);
			chromePrefs.put("profile.password_manager_enabled", false);
			chromePrefs.put("profile.password_manager_leak_detection", false);

			final ChromeOptions chromeOptions = new ChromeOptions();
			chromeOptions.setExperimentalOption("prefs", chromePrefs);
			driver = new ChromeDriver(chromeOptions);
		}

		else if(browser.equalsIgnoreCase("edge")) {
			driver = new EdgeDriver();
		}
		else if(browser.equalsIgnoreCase("firefox")) {
			driver = new FirefoxDriver();
		}
		else {
            throw new RuntimeException(
                    "Invalid browser name: " + browser
            );
        }

		driver.manage().window().maximize();
		genericWait(driver);

		return driver;
	}

	public void genericWait(WebDriver d) {
		d.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
	}
	
	public void takeScreenshot(WebDriver driver, String fileName) throws IOException {

	    TakesScreenshot ts = (TakesScreenshot) driver;

	    File source = ts.getScreenshotAs(OutputType.FILE);

	    File folder = new File("./screenshots");

	    if (!folder.exists()) {
	        folder.mkdirs();
	    }

	    File destination = new File(folder, fileName + ".png");

	    FileUtils.copyFile(source, destination);

	    System.out.println("Screenshot saved at: " + destination.getAbsolutePath());
	}
	
	
}