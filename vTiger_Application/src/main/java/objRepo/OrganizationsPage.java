package objRepo;

import java.time.Duration;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class OrganizationsPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By organizationsLink = By.xpath(
        "//a[contains(@href,'Accounts') or normalize-space()='Organizations']"
    );

    private final By searchField = By.name("search_text");

    public OrganizationsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(
            driver, Duration.ofSeconds(15)
        );
    }

    // Open Organizations module
    public void clickOrganizations() {

        wait.until(
            ExpectedConditions.elementToBeClickable(organizationsLink)
        ).click();

        wait.until(ExpectedConditions.or(
            ExpectedConditions.visibilityOfElementLocated(searchField),
            ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//table//tr[td]")
            )
        ));
    }

    // Check whether an organization is listed
    public boolean isOrganizationListed(String organizationNumber) {

        By organizationRow = By.xpath(
            "//tr[td[normalize-space()='"
            + organizationNumber + "']]"
        );

        try {
            return !driver.findElements(organizationRow).isEmpty()
                && driver.findElement(organizationRow).isDisplayed();
        } catch (StaleElementReferenceException e) {
            return !driver.findElements(organizationRow).isEmpty();
        }
    }

    // FT: Delete the first available configured organization



public String deleteOneOrganizationFromList(String... organizationNumbers) {

    for (String number : organizationNumbers) {

        if (number == null || number.trim().isEmpty()) {
            continue;
        }

        number = number.trim();

        // Search for the organization.
        WebElement search = wait.until(
            ExpectedConditions.visibilityOfElementLocated(searchField)
        );

        search.clear();
        search.sendKeys(number);
        search.sendKeys(org.openqa.selenium.Keys.ENTER);

        By organizationRow = By.xpath(
            "//tr[td[normalize-space()='" + number + "']]"
        );

        try {
            wait.until(
                ExpectedConditions.visibilityOfElementLocated(organizationRow)
            );
        } catch (TimeoutException e) {
            continue;
        }

        By deleteLink = By.xpath(
            "//tr[td[normalize-space()='" + number
            + "']]//a[normalize-space()='del']"
        );

        try {
            wait.until(
                ExpectedConditions.elementToBeClickable(deleteLink)
            ).click();

            // Handle a JavaScript alert immediately after clicking Delete.
            try {
                Alert alert = new WebDriverWait(
                    driver, Duration.ofSeconds(5)
                ).until(ExpectedConditions.alertIsPresent());

                alert.accept();

            } catch (TimeoutException ignored) {
                // No JavaScript alert appeared.
            }

            // Verify deletion only after handling the alert.
            wait.until(
                ExpectedConditions.invisibilityOfElementLocated(organizationRow)
            );

            return number;

        } catch (TimeoutException | StaleElementReferenceException e) {
            // Try the next candidate.
        }
    }

    throw new NoSuchElementException(
        "No supplied organization ID could be deleted."
    );
}

    // ST: Search organization

public void searchOrganization(String organizationNumber) {

    WebElement searchField = wait.until(
        ExpectedConditions.visibilityOfElementLocated(
            By.name("search_text")
        )
    );

    searchField.clear();
    searchField.sendKeys(organizationNumber);
    searchField.sendKeys(org.openqa.selenium.Keys.ENTER);

    By organizationRow = By.xpath(
        "//tr[td[normalize-space()='"
        + organizationNumber + "']]"
    );

    wait.until(
        ExpectedConditions.visibilityOfElementLocated(
            organizationRow
        )
    );

    System.out.println(
        "Smoke Test Passed: Found " + organizationNumber
    );
}

    // ST: Click organization name

public void clickOrganization(String organizationNumber) {

    By organizationLink = By.xpath(
        "//tr[td[normalize-space()='" + organizationNumber + "']]//a"
    );

    WebElement link = wait.until(
        ExpectedConditions.elementToBeClickable(organizationLink)
    );

    link.click();

    wait.until(ExpectedConditions.or(
        ExpectedConditions.urlContains("action=DetailView"),
        ExpectedConditions.visibilityOfElementLocated(
            By.name("accountname")
        )
    ));

    System.out.println(
        "Opened organization: " + organizationNumber
    );
}

    // IT: Select first organization using a fresh locator

// IT: Select first organization
public void selectFirstOrganization() {

    By checkboxLocator = By.xpath(
        "(//table//tr[td]//input[@type='checkbox'])[last()]"
    );

    WebElement checkbox = wait.until(
        ExpectedConditions.presenceOfElementLocated(checkboxLocator)
    );

    ((JavascriptExecutor) driver).executeScript(
        "arguments[0].scrollIntoView({block:'center'});",
        checkbox
    );

    if (!checkbox.isSelected()) {
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].click();",
            checkbox
        );
    }
}

    // IT: Open Mass Edit
    public void clickMassEdit() {

        By massEditButton = By.xpath(
            "//input[@value='Mass Edit' or @title='Mass Edit']"
            + " | //button[normalize-space()='Mass Edit']"
            + " | //a[normalize-space()='Mass Edit']"
        );

        wait.until(
            ExpectedConditions.elementToBeClickable(massEditButton)
        ).click();

        wait.until(ExpectedConditions.or(
            ExpectedConditions.visibilityOfElementLocated(
                By.name("website")
            ),
            ExpectedConditions.visibilityOfElementLocated(
                By.name("phone")
            ),
            ExpectedConditions.visibilityOfElementLocated(
                By.name("employees")
            )
        ));
    }

    // IT: Enter Mass Edit values
    public void enterMassEditValue(
            String fieldName, String value) {

        By locator;

        switch (fieldName.toLowerCase()) {

            case "website":
                locator = By.name("website");
                break;

            case "phone":
                locator = By.name("phone");
                break;

            case "employees":
                locator = By.name("employees");
                break;

            default:
                throw new IllegalArgumentException(
                    "Unsupported field: " + fieldName
                );
        }

        WebElement field = wait.until(
            ExpectedConditions.visibilityOfElementLocated(locator)
        );

        field.clear();
        field.sendKeys(value);
    }

    // IT: Save Mass Edit

public void saveMassEdit() {

    By saveButton = By.xpath(
        "//*[@value='Save' or @title='Save' "
        + "or normalize-space(.)='Save' "
        + "or contains(@class,'save')]"
    );

    WebElement save = wait.until(
        ExpectedConditions.elementToBeClickable(saveButton)
    );

    ((JavascriptExecutor) driver).executeScript(
        "arguments[0].scrollIntoView({block:'center'});",
        save
    );

    save.click();

    wait.until(ExpectedConditions.or(
        ExpectedConditions.urlContains("action=ListView"),
        ExpectedConditions.urlContains("action=DetailView"),
        ExpectedConditions.invisibilityOfElementLocated(saveButton)
    ));

    System.out.println(
        "Integration Test Passed: Mass Edit saved."
    );
}


    // Open individual Edit Organization form
    public void clickEditOrganization() {

        By editButton = By.xpath(
            "//input[@name='Edit' and contains(@class,'edit')]"
            + " | //input[@value='Edit']"
            + " | //a[normalize-space()='Edit']"
        );

        wait.until(
            ExpectedConditions.elementToBeClickable(editButton)
        ).click();

        wait.until(ExpectedConditions.or(
            ExpectedConditions.urlContains("action=EditView"),
            ExpectedConditions.visibilityOfElementLocated(
                By.name("accountname")
            )
        ));
    }

    // Save individual organization details
    public void saveOrganizationDetails() {

        By saveLocator = By.xpath(
            "//input[@type='button' and "
            + "contains(@class,'save') and @value='Save']"
            + " | //input[@type='submit' and @value='Save']"
            + " | //button[normalize-space()='Save']"
        );

        WebElement saveButton = wait.until(
            ExpectedConditions.elementToBeClickable(saveLocator)
        );

        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block:'center'});",
            saveButton
        );

        saveButton.click();

        wait.until(ExpectedConditions.or(
            ExpectedConditions.urlContains("action=DetailView"),
            ExpectedConditions.visibilityOfElementLocated(
                By.name("accountname")
            )
        ));

        System.out.println(
            "Organization details saved successfully."
        );
    }
}
