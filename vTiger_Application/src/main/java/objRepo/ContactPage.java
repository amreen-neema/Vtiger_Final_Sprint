package objRepo;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ContactPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By contactsLink = By.xpath(
        "//a[contains(@href,'module=Contacts') "
        + "or normalize-space()='Contacts']"
    );

    private final By searchField = By.name("search_text");

    public ContactPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(
            driver, Duration.ofSeconds(15)
        );
    }

    // Navigate to Contacts
    public void clickContacts() {
        wait.until(
            ExpectedConditions.elementToBeClickable(contactsLink)
        ).click();

        wait.until(ExpectedConditions.or(
            ExpectedConditions.visibilityOfElementLocated(searchField),
            ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//table//tr[td]")
            )
        ));
    }

    // Contact row locator
    private By contactRow(String contactId) {
        return By.xpath(
            "//tr[td[normalize-space()='" + contactId + "']]"
        );
    }

    // Check if contact ID appears in results
    public boolean isContactListed(String contactId) {
        try {
            return !driver.findElements(contactRow(contactId)).isEmpty();
        } catch (StaleElementReferenceException e) {
            return !driver.findElements(contactRow(contactId)).isEmpty();
        }
    }

    // Search by contact ID
    public boolean searchContact(String contactId) {

        WebElement field = wait.until(
            ExpectedConditions.visibilityOfElementLocated(searchField)
        );

        field.clear();
        field.sendKeys(contactId);
        field.sendKeys(Keys.ENTER);

        By row = contactRow(contactId);

        By noResults = By.xpath(
            "//*[contains(translate(normalize-space(.),"
            + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ',"
            + "'abcdefghijklmnopqrstuvwxyz'),'no records found')"
            + " or contains(translate(normalize-space(.),"
            + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ',"
            + "'abcdefghijklmnopqrstuvwxyz'),'no contact found')]"
        );

        try {
            wait.until(d ->
                !d.findElements(row).isEmpty()
                || !d.findElements(noResults).isEmpty()
            );
        } catch (TimeoutException e) {
            return !driver.findElements(row).isEmpty();
        }

        return !driver.findElements(row).isEmpty();
    }

    // Open a contact using its ID

public void openContact(String contactId) {

    By contactLink = By.xpath(
        "//tr[td[normalize-space()='" + contactId
        + "']]/td[1]//a"
    );

    WebElement link = wait.until(
        ExpectedConditions.elementToBeClickable(contactLink)
    );

    link.click();

    wait.until(ExpectedConditions.or(
        ExpectedConditions.urlContains("action=DetailView"),
        ExpectedConditions.visibilityOfElementLocated(
            By.name("lastname")
        )
    ));

    System.out.println("Opened contact: " + contactId);
}


    // Open Edit Contact form
    public void clickEdit() {

        By editButton = By.xpath(
            "//input[@name='Edit']"
            + " | //input[@value='Edit']"
            + " | //a[normalize-space()='Edit']"
            + " | //button[normalize-space()='Edit']"
        );

        wait.until(
            ExpectedConditions.elementToBeClickable(editButton)
        ).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.name("lastname")
        ));
    }

    // Fill contact details
    public void fillContactDetails(
            String firstName,
            String lastName,
            String title,
            String department,
            String email,
            String phone,
            String mobile,
            String birthday) {

        enterValue(By.name("firstname"), firstName);
        enterValue(By.name("lastname"), lastName);
        enterValue(By.name("title"), title);
        enterValue(By.name("department"), department);
        enterValue(By.name("email"), email);
        enterValue(By.name("phone"), phone);
        enterValue(By.name("mobile"), mobile);
        enterValue(By.name("birthday"), birthday);
    }

    private void enterValue(By locator, String value) {

        WebElement field = wait.until(
            ExpectedConditions.visibilityOfElementLocated(locator)
        );

        field.clear();

        if (value != null && !value.isEmpty()) {
            field.sendKeys(value);
        }
    }

    // Save contact form

public void saveContact() {

    By saveButton = By.xpath(
        "//input[translate(@value,'SAVE','save')='save']"
        + " | //input[translate(@title,'SAVE','save')='save']"
        + " | //button[normalize-space()='Save']"
        + " | //button[contains(normalize-space(.),'Save')]"
        + " | //input[contains(@class,'save')]"
    );

    WebElement save = wait.until(
        ExpectedConditions.elementToBeClickable(saveButton)
    );

    ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
        "arguments[0].scrollIntoView({block:'center'});",
        save
    );

    try {
        save.click();
    } catch (org.openqa.selenium.ElementClickInterceptedException e) {
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
            "arguments[0].click();",
            save
        );
    }

    System.out.println("Save button clicked.");
}

    // Confirm a successful save and return to Contacts

public void saveAndReturnToContacts() {

    saveContact();

    wait.until(ExpectedConditions.or(
        ExpectedConditions.urlContains("action=DetailView"),
        ExpectedConditions.urlContains("action=ListView"),
        ExpectedConditions.visibilityOfElementLocated(
            By.name("search_text")
        )
    ));

    clickContacts();

    System.out.println(
        "Contact saved. Returned to Contacts page."
    );
}

    // Clear email and submit the form

public boolean submitEmptyEmailAndCheckError() {

    // Ensure Email remains empty
    WebElement emailField = wait.until(
        ExpectedConditions.visibilityOfElementLocated(
            By.name("email")
        )
    );

    emailField.clear();

    // Click Save
    saveContact();

    // Check for a JavaScript alert
    try {
        Alert alert = new WebDriverWait(
            driver, Duration.ofSeconds(2)
        ).until(ExpectedConditions.alertIsPresent());

        String alertText = alert.getText();

        boolean emailError =
            alertText.toLowerCase().contains("email")
            && (alertText.toLowerCase().contains("empty")
                || alertText.toLowerCase().contains("required")
                || alertText.toLowerCase().contains("enter"));

        alert.accept();

        if (emailError) {
            return true;
        }

    } catch (TimeoutException ignored) {
        // Check inline and browser validation instead.
    }

    // Check for a visible email validation message
    By emailError = By.xpath(
        "//*[contains(translate(normalize-space(.),"
        + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ',"
        + "'abcdefghijklmnopqrstuvwxyz'),'email')"
        + " and (contains(translate(normalize-space(.),"
        + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ',"
        + "'abcdefghijklmnopqrstuvwxyz'),'required')"
        + " or contains(translate(normalize-space(.),"
        + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ',"
        + "'abcdefghijklmnopqrstuvwxyz'),'empty')"
        + " or contains(translate(normalize-space(.),"
        + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ',"
        + "'abcdefghijklmnopqrstuvwxyz'),'enter'))]"
    );

    try {
        wait.until(d -> {
            for (WebElement element : d.findElements(emailError)) {
                try {
                    if (element.isDisplayed()
                            && !element.getText().trim().isEmpty()) {
                        return true;
                    }
                } catch (StaleElementReferenceException ignored) {
                    // Recheck the current page.
                }
            }

            return false;
        });

        return true;

    } catch (TimeoutException ignored) {
        // Check HTML5 required-field validation.
    }

    return (Boolean) ((JavascriptExecutor) driver).executeScript(
        "var e = document.querySelector('[name=\"email\"]');"
        + "return e != null && e.value.trim() === ''"
        + " && !e.checkValidity();"
    );
}

    // Delete a contact
public String deleteOneContactFromList(String[] contactIds) {

    for (String contactId : contactIds) {

        if (contactId == null || contactId.trim().isEmpty()) {
            continue;
        }

        contactId = contactId.trim();

        if (!searchContact(contactId)) {
            continue;
        }

        By deleteLink = By.xpath(
            "//tr[td[normalize-space()='" + contactId
            + "']]//a[normalize-space()='del']"
        );

        try {
            wait.until(
                ExpectedConditions.elementToBeClickable(deleteLink)
            ).click();

            try {
                Alert alert = new WebDriverWait(
                    driver, Duration.ofSeconds(3)
                ).until(ExpectedConditions.alertIsPresent());

                alert.accept();

            } catch (TimeoutException ignored) {
                // No JavaScript confirmation alert appeared.
            }

            wait.until(
                ExpectedConditions.invisibilityOfElementLocated(
                    contactRow(contactId)
                )
            );

            return contactId;

        } catch (
            TimeoutException | StaleElementReferenceException e
        ) {
            // Try the next Excel-provided candidate.
        }
    }

    throw new NoSuchElementException(
        "No Excel-configured contact could be deleted. "
        + "Verify that at least one contact ID exists in the CRM "
        + "and that its delete link is available."
    );
}
public void selectContactForMassEdit(String contactId) {

    By checkbox = By.xpath(
        "//tr[td[normalize-space()='" + contactId
        + "']]//input[@type='checkbox']"
    );

    WebElement check = wait.until(
        ExpectedConditions.elementToBeClickable(checkbox)
    );

    if (!check.isSelected()) {
        check.click();
    }
}

public void clickMassEdit() {

    By massEditButton = By.xpath(
        "//input[@value='Mass Edit' or @title='Mass Edit']"
        + " | //button[normalize-space()='Mass Edit']"
        + " | //a[normalize-space()='Mass Edit']"
        + " | //input[contains(@src,'massedit')]"
    );

    wait.until(
        ExpectedConditions.elementToBeClickable(massEditButton)
    ).click();

    System.out.println("Mass Edit opened.");
}


public void fillMassEditDetailsExceptEmail() {

    // Enable and fill the fields in the Mass Edit form.
    enterMassEditField("firstname", "Sundar");
    enterMassEditField("lastname", "shetty");
    enterMassEditField("title", "Employee");
    enterMassEditField("department", "sales");
    enterMassEditField("phone", "2345678901");
    enterMassEditField("mobile", "2345678901");

    // Enable Email editing but deliberately leave it empty.
    enableMassEditField("email");

    WebElement email = wait.until(
        ExpectedConditions.visibilityOfElementLocated(
            By.name("email")
        )
    );

    email.clear();
}


private void enableMassEditField(String fieldName) {

    By field = By.name(fieldName);

    WebElement input = wait.until(
        ExpectedConditions.presenceOfElementLocated(field)
    );

    java.util.List<WebElement> checkboxes =
        input.findElements(By.xpath(
            "./preceding-sibling::input[@type='checkbox']"
            + " | ./ancestor::tr[1]//input[@type='checkbox']"
        ));

    for (WebElement checkbox : checkboxes) {

        if (!checkbox.isSelected()) {

            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});",
                checkbox
            );

            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].click();",
                checkbox
            );
        }
    }
}


private void enterMassEditField(
        String fieldName, String value) {

    enableMassEditField(fieldName);

    WebElement input = wait.until(
        ExpectedConditions.visibilityOfElementLocated(
            By.name(fieldName)
        )
    );

    input.clear();
    input.sendKeys(value);
}


public boolean submitMassEditWithEmptyEmail() {

    // Email must remain selected for updating, but its value is empty.
    WebElement emailField = wait.until(
        ExpectedConditions.visibilityOfElementLocated(
            By.name("email")
        )
    );
    emailField.clear();

    // The Mass Edit form has a Save button at the bottom.
    By saveLocator = By.xpath(
        "//input[@type='button' and " +
        "contains(translate(@value,'SAVE','save'),'save')]" +
        " | //input[@type='submit' and " +
        "contains(translate(@value,'SAVE','save'),'save')]" +
        " | //button[normalize-space()='Save']"
    );

    WebElement saveButton = wait.until(
        ExpectedConditions.elementToBeClickable(saveLocator)
    );

    ((JavascriptExecutor) driver).executeScript(
        "arguments[0].scrollIntoView({block:'center'});",
        saveButton
    );

    saveButton.click();

    // Check for the application's email validation message.
    By errorLocator = By.xpath(
        "//*[contains(translate(normalize-space(.)," +
        "'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')," +
        "'email') and (" +
        "contains(translate(normalize-space(.)," +
        "'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')," +
        "'empty') or " +
        "contains(translate(normalize-space(.)," +
        "'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')," +
        "'required') or " +
        "contains(translate(normalize-space(.)," +
        "'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')," +
        "'enter'))]"
    );

    try {
        wait.until(d -> {
            for (WebElement element : d.findElements(errorLocator)) {
                if (element.isDisplayed()
                        && !element.getText().trim().isEmpty()) {
                    return true;
                }
            }
            return false;
        });
        return true;

    } catch (TimeoutException e) {
        return false;
    }
}


}
