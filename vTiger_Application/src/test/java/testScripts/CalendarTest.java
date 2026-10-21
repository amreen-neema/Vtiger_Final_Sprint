package testScripts;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import genericUtility.BaseClass;
import genericUtility.ExcelUtility;
import genericUtility.ScreenshotsUtility;
import objRepo.CalendarPage;

public class CalendarTest extends BaseClass {
	@Test(priority = 1, groups = {"FT", "YT"})
	public void verifyCreateCalendarEvent() throws Exception {
        ExcelUtility excel = new ExcelUtility();
        CalendarPage calendar = new CalendarPage(driver);

        String eventName = excel.getDataFromExcel("Calendar", 1, 1);
        String eventDate = excel.getDataFromExcel("Calendar", 1, 2);
        String description = excel.getDataFromExcel("Calendar", 1, 3);
        String location = excel.getDataFromExcel("Calendar", 1, 4);
        String status = excel.getDataFromExcel("Calendar", 1, 5);
        String startHour = excel.getDataFromExcel("Calendar", 1, 6);
        String startMinute = excel.getDataFromExcel("Calendar", 1, 7);
        String endHour = excel.getDataFromExcel("Calendar", 1, 8);
        String endMinute = excel.getDataFromExcel("Calendar", 1, 9);

        calendar.clickCalendar();
        calendar.clickAddEvent();
        calendar.enterEventName(eventName);
        calendar.enterDescription(description);
        calendar.enterLocation(location);
        calendar.selectStatus(status);
        calendar.enterStartDate(eventDate);
        calendar.enterEndDate(eventDate);
        calendar.selectStartTime(startHour, startMinute);
        calendar.selectEndTime(endHour, endMinute);
        calendar.clickSave();

        Assert.assertTrue(
            calendar.isEventFormClosed(),
            "Event form did not close after saving."
        );

        Reporter.log("FT: Calendar event submitted.", true);
    }

    @Test(priority = 2, groups = "ST")
    public void verifyCalendarModuleLoads() {

        CalendarPage calendar = new CalendarPage(driver);
        calendar.clickCalendar();

        Assert.assertTrue(
            driver.getCurrentUrl().contains("module=Calendar"),
            "Calendar module did not open."
        );

        Reporter.log("ST: Calendar module opened.", true);
    }

    @Test(priority = 3, groups = "IT")
    public void verifyHomeToCalendarNavigation() {

        CalendarPage calendar = new CalendarPage(driver);
        calendar.clickCalendar();

        Assert.assertTrue(
            driver.getCurrentUrl().contains("module=Calendar"),
            "Calendar navigation failed."
        );

        Reporter.log("IT: Calendar navigation verified.", true);
    }

    @Test(priority = 4, groups = "NT")
    public void verifyEventNameIsMandatory() throws Exception {

        ExcelUtility excel = new ExcelUtility();
        CalendarPage calendar = new CalendarPage(driver);

        String eventDate = excel.getDataFromExcel("Calendar", 2, 2);
        String description = excel.getDataFromExcel("Calendar", 2, 3);
        String location = excel.getDataFromExcel("Calendar", 2, 4);
        String status = excel.getDataFromExcel("Calendar", 2, 5);
        String startHour = excel.getDataFromExcel("Calendar", 2, 6);
        String startMinute = excel.getDataFromExcel("Calendar", 2, 7);
        String endHour = excel.getDataFromExcel("Calendar", 2, 8);
        String endMinute = excel.getDataFromExcel("Calendar", 2, 9);

        calendar.clickCalendar();
        calendar.clickAddEvent();
        calendar.leaveEventNameEmpty();
        calendar.enterDescription(description);
        calendar.enterLocation(location);
        calendar.selectStatus(status);
        calendar.enterStartDate(eventDate);
        calendar.enterEndDate(eventDate);
        calendar.selectStartTime(startHour, startMinute);
        calendar.selectEndTime(endHour, endMinute);

        String alertMessage = calendar.clickSaveAndGetAlert();

        Assert.assertTrue(
            alertMessage.toLowerCase().contains("name")
                || alertMessage.toLowerCase().contains("subject"),
            "Expected an event-name validation alert, but received: "
                + alertMessage
        );

        Reporter.log("NT: Empty event name rejected.", true);
        
        ScreenshotsUtility.captureScreenshot(
        	    driver, "NT_EmptyEventName"
        	);
    }
}