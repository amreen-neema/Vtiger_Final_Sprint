package testScripts;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import genericUtility.BaseClass;
import genericUtility.ExcelUtility;
import genericUtility.ScreenshotsUtility;
import objRepo.ContactPage;

public class ContactTest extends BaseClass {

	@Test(priority = 1, groups = {"FT", "YT"})
	public void verifyDeleteContact() throws Exception {

	    ExcelUtility excel = new ExcelUtility();
	    ContactPage contacts = new ContactPage(driver);

	    contacts.clickContacts();

	    // Read deletion candidates from Excel.
	    // Contacts sheet: rows 1–6, column 1 = ContactId
	    String[] contactIds = new String[6];

	    for (int i = 0; i < contactIds.length; i++) {
	        contactIds[i] = excel.getDataFromExcel(
	            "Contacts", i + 1, 1
	        ).trim();
	    }

	    String deletedId = contacts.deleteOneContactFromList(contactIds);

	    Assert.assertFalse(
	        contacts.isContactListed(deletedId),
	        "Contact was not deleted: " + deletedId
	    );

	    Reporter.log("FT: Deleted contact " + deletedId, true);
	}
    @Test(priority = 2, groups = "ST")
    public void verifyContactSearch() throws Exception {

        ExcelUtility excel = new ExcelUtility();
        ContactPage contacts = new ContactPage(driver);

        // Excel SmokeSearch rows: 11 through 16.
        for (int row = 13; row <= 16; row++) {

            String contactId =
                excel.getDataFromExcel("Contacts", row, 1);

            contacts.clickContacts();

            Assert.assertTrue(
                contacts.searchContact(contactId),
                "Contact was not found: " + contactId
            );

            Reporter.log(
                "ST: Contact found: " + contactId,
                true
            );
        }
    }

    @Test(priority = 3, groups = "IT")
    public void verifyEditContactAndSave() throws Exception {

        ExcelUtility excel = new ExcelUtility();
        ContactPage contacts = new ContactPage(driver);

        String contactId = excel.getDataFromExcel("Contacts", 17, 1);
        String firstName = excel.getDataFromExcel("Contacts", 17, 2);
        String lastName = excel.getDataFromExcel("Contacts", 17, 3);
        String title = excel.getDataFromExcel("Contacts", 17, 4);
        String department = excel.getDataFromExcel("Contacts", 17, 5);
        String email = excel.getDataFromExcel("Contacts", 17, 6);
        String phone = excel.getDataFromExcel("Contacts", 17, 7);
        String mobile = excel.getDataFromExcel("Contacts", 17, 8);
        String birthday = excel.getDataFromExcel("Contacts", 17, 9);

        contacts.clickContacts();

        Assert.assertTrue(
            contacts.searchContact(contactId),
            "Contact was not found: " + contactId
        );

        contacts.selectContactForMassEdit(contactId);
        contacts.clickMassEdit();

        contacts.fillContactDetails(
            firstName,
            lastName,
            title,
            department,
            email,
            phone,
            mobile,
            birthday
        );

        contacts.saveAndReturnToContacts();

        Reporter.log(
            "IT: Contact update submitted for " + contactId,
            true
        );
    }

    @Test(priority = 4, groups = "NT")
    public void verifyEmptyEmailValidation() throws Exception {

        ExcelUtility excel = new ExcelUtility();
        ContactPage contacts = new ContactPage(driver);

        String contactId = excel.getDataFromExcel("Contacts", 18, 1);

        contacts.clickContacts();

        Assert.assertTrue(
            contacts.searchContact(contactId),
            "Contact was not found: " + contactId
        );

        contacts.selectContactForMassEdit(contactId);
        contacts.clickMassEdit();
        contacts.fillMassEditDetailsExceptEmail();

        Assert.assertTrue(
            contacts.submitMassEditWithEmptyEmail(),
            "Expected an error stating that Email is empty."
        );

        Reporter.log("NT: Empty-email validation checked for " + contactId, true);
        
        ScreenshotsUtility.captureScreenshot(
        	    driver, "NT_EmptyEmail"
        	);
    }
}