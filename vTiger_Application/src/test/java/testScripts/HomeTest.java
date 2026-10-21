package testScripts;

import java.io.IOException;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import genericUtility.BaseClass;
import genericUtility.ExcelUtility;
import genericUtility.ScreenshotsUtility;
import objRepo.HomePage;

public class HomeTest extends BaseClass {

    @Test(priority = 1, groups = "FT")
    public void addModule() throws IOException {

        ExcelUtility excel = new ExcelUtility();
        HomePage home = new HomePage(driver);

        String windowTitle = excel.getDataFromExcel("Home", 1, 1);
        int reportNameIndex = (int) Double.parseDouble(
            excel.getDataFromExcel("Home", 1, 2)
        );
        int reportTypeIndex = (int) Double.parseDouble(
            excel.getDataFromExcel("Home", 1, 3)
        );

        home.clickAddWidget();
        home.clickReportChartsOption();
        home.addWindowTitle();
        home.selectReportnameByIndex(reportNameIndex);
        home.selectRepoertypeByIndex(reportTypeIndex);
        home.clicksave();

        Reporter.log(
            "FT: Add widget submitted. Excel title: " + windowTitle,
            true
        );
    }

    @Test(priority = 2, groups = "NT")
    public void addModuleWithoutMandatory() throws IOException {

        ExcelUtility excel = new ExcelUtility();
        HomePage home = new HomePage(driver);

        String windowTitle = excel.getDataFromExcel("Home", 2, 1);
        int reportNameIndex = (int) Double.parseDouble(
            excel.getDataFromExcel("Home", 2, 2)
        );
        int reportTypeIndex = (int) Double.parseDouble(
            excel.getDataFromExcel("Home", 2, 3)
        );

        home.clickAddWidget();
        home.clickReportChartsOption();

        // Leave the mandatory title field empty.
        home.selectReportnameByIndex(reportNameIndex);
        home.selectRepoertypeByIndex(reportTypeIndex);
        
        ScreenshotsUtility.captureScreenshot(
        	    driver, "NT_WidgetWithoutTitle"
        	);

        home.clicksave();

        Reporter.log("NT: Submitted widget without title. Excel title value: " + windowTitle,true);
        
       
    }
}