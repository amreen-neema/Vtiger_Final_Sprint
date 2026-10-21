package objRepo;

import java.time.Duration;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CalendarPage {

    WebDriver driver;
    WebDriverWait wait;

    public CalendarPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    // Calendar navigation
    @FindBy(xpath = "//a[@href='index.php?module=Calendar&action=index']")
    private WebElement calendarLink;

    // Add button
    @FindBy(className = "calAddButton")
    private WebElement addEventButton;

    // Meeting option
    @FindBy(id = "addmeeting")
    private WebElement meetingOption;

    // Event form fields
    @FindBy(name = "subject")
    private WebElement eventNameField;

    @FindBy(name = "description")
    private WebElement descriptionField;

    @FindBy(name = "location")
    private WebElement locationField;

    @FindBy(name = "eventstatus")
    private WebElement statusDropdown;

    // Start and end dates
    @FindBy(name = "date_start")
    private WebElement startDateField;

    @FindBy(name = "due_date")
    private WebElement endDateField;

    // Start and end time dropdowns
    @FindBy(name = "starthr")
    private WebElement startHourDropdown;

    @FindBy(name = "startmin")
    private WebElement startMinuteDropdown;

    @FindBy(name = "endhr")
    private WebElement endHourDropdown;

    @FindBy(name = "endmin")
    private WebElement endMinuteDropdown;

    // Save button
    @FindBy(name = "eventsave")
    private WebElement saveButton;

    public void clickCalendar() {
        wait.until(
            ExpectedConditions.elementToBeClickable(calendarLink)
        ).click();
    }

    public void clickAddEvent() {
        wait.until(
            ExpectedConditions.elementToBeClickable(addEventButton)
        ).click();

        wait.until(
            ExpectedConditions.elementToBeClickable(meetingOption)
        ).click();

        wait.until(
            ExpectedConditions.visibilityOf(eventNameField)
        );
    }

    public void enterEventName(String name) {
        eventNameField.clear();
        eventNameField.sendKeys(name);
    }

    public void leaveEventNameEmpty() {
        eventNameField.clear();
    }

    public void enterDescription(String description) {
        descriptionField.clear();
        descriptionField.sendKeys(description);
    }

    public void enterLocation(String location) {
        locationField.clear();
        locationField.sendKeys(location);
    }

    public void selectStatus(String status) {
        new Select(statusDropdown).selectByVisibleText(status);
    }

    public void enterStartDate(String date) {
        startDateField.clear();
        startDateField.sendKeys(date);
    }

    public void enterEndDate(String date) {
        endDateField.clear();
        endDateField.sendKeys(date);
    }

    public void selectStartTime(String hour, String minute) {
        new Select(startHourDropdown).selectByVisibleText(hour);
        new Select(startMinuteDropdown).selectByVisibleText(minute);
    }

    public void selectEndTime(String hour, String minute) {
        new Select(endHourDropdown).selectByVisibleText(hour);
        new Select(endMinuteDropdown).selectByVisibleText(minute);
    }

    public void clickSave() {
        wait.until(
            ExpectedConditions.elementToBeClickable(saveButton)
        ).click();
    }

    // Click Save and capture a JavaScript alert, if displayed
    public String clickSaveAndGetAlert() {
        wait.until(
            ExpectedConditions.elementToBeClickable(saveButton)
        ).click();

        try {
            WebDriverWait alertWait =
                new WebDriverWait(driver, Duration.ofSeconds(3));

            Alert alert = alertWait.until(
                ExpectedConditions.alertIsPresent()
            );

            String message = alert.getText();
            alert.accept();

            return message;

        } catch (org.openqa.selenium.TimeoutException e) {
            return "";
        }
    }

    public boolean isEventFormClosed() {
        try {
            wait.until(
                ExpectedConditions.or(
                    ExpectedConditions.urlContains("module=Calendar"),
                    ExpectedConditions.invisibilityOf(eventNameField)
                )
            );
            return true;
        } catch (org.openqa.selenium.TimeoutException e) {
            return false;
        }
    }
}
