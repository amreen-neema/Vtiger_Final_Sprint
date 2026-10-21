package testScripts;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import genericUtility.BaseClass;
import genericUtility.ExcelUtility;
import genericUtility.ScreenshotsUtility;
import objRepo.LoginPage;

public class LoginTest extends BaseClass {

    @Test(priority = 1, groups = "FT")
    public void validLogin() throws Exception {

        ExcelUtility excel = new ExcelUtility();

        String username = fu.getDataFromPropertyFile("username");
        String password = fu.getDataFromPropertyFile("password");

        // Reset the session and open the login page.
        driver.manage().deleteAllCookies();
        driver.get(fu.getDataFromPropertyFile("url"));

        LoginPage login = new LoginPage(driver);
        login.login(username, password);

        Assert.assertFalse(
            driver.findElements(By.xpath("//div[@class='errorMessage']")).size() > 0,
            "Valid login failed."
        );

        Reporter.log("FT: Valid login completed.", true);
    }

    @Test(priority = 2, groups = "NT")
    public void invalidUsername() throws Exception {

        ExcelUtility excel = new ExcelUtility();

        // Login sheet: row 2, column 1.
        String username = excel.getDataFromExcel("Login", 2, 1);
        String password = fu.getDataFromPropertyFile("password");

        driver.manage().deleteAllCookies();
        driver.get(fu.getDataFromPropertyFile("url"));

        LoginPage login = new LoginPage(driver);
        login.login(username, password);

        Assert.assertTrue(
            driver.findElements(By.xpath("//div[@class='errorMessage']")).size() > 0
                || driver.getCurrentUrl().contains("action=Login"),
            "The application did not reject the invalid username."
        );

        Reporter.log("NT: Invalid username was rejected.", true);
        
        ScreenshotsUtility.captureScreenshot(
        	    driver, "NT_InvalidUsername"
        	);
    }
    
       
}