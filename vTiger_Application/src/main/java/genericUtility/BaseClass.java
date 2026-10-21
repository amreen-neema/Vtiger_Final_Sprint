package genericUtility;

import org.openqa.selenium.WebDriver;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import objRepo.LoginPage;


public class BaseClass {

    public WebDriver driver = null;
    public WebDriverUtility wu = new WebDriverUtility();
    public FileUtility fu = new FileUtility();

    @BeforeSuite(alwaysRun = true)
    public void dbConnection() {
        Reporter.log("Before Suite", true);
    }

    @BeforeTest(alwaysRun = true)
    public void bt() {
        Reporter.log("Before Test", true);
    }

    @Parameters("browser")
    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(@Optional("chrome") String browser)
            throws Exception {

        Reporter.log("Before Method - " + browser, true);

        driver = wu.launchBrowser(browser);

        driver.get(fu.getDataFromPropertyFile("url"));

        LoginPage lp = new LoginPage(driver);

        lp.login(
            fu.getDataFromPropertyFile("username"),
            fu.getDataFromPropertyFile("password")
        );
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod() {
        Reporter.log("After Method", true);

        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                Reporter.log(
                    "Browser cleanup: " + e.getMessage(), true
                );
            } finally {
                driver = null;
            }
        }
    }
    @AfterClass(alwaysRun = true)
    public void ac() {
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception e) {
                Reporter.log(
                    "AfterClass cleanup: " + e.getMessage(), true
                );
            } finally {
                driver = null;
            }
        }
    }
    
    @AfterTest(alwaysRun = true)
    public void at() 
    {
        Reporter.log("After Test", true);
    }

    @AfterSuite(alwaysRun = true)
    public void closeConnection() {
        Reporter.log("After Suite", true);
    }
}