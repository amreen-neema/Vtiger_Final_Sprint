package testScripts;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import genericUtility.BaseClass;
import genericUtility.ExcelUtility;
import objRepo.OrganizationsPage;

public class OrganizationsTest extends BaseClass {


@Test(priority = 1, groups = {"FT", "YT"})
public void verifyDeleteOrganization() throws Exception {

    ExcelUtility excel = new ExcelUtility();
    OrganizationsPage organizations = new OrganizationsPage(driver);

    organizations.clickOrganizations();

    String[] organizationNumbers = new String[6];

    // Excel rows 1–6, column 1 contains deletion candidates.
    for (int i = 0; i < organizationNumbers.length; i++) {
        organizationNumbers[i] =
            excel.getDataFromExcel("Organizations", i + 1, 1);
    }

    String deletedId =
        organizations.deleteOneOrganizationFromList(organizationNumbers);

    Assert.assertFalse(
        organizations.isOrganizationListed(deletedId),
        "Organization still exists after deletion: " + deletedId
    );

    Reporter.log("FT: Deleted organization " + deletedId, true);
}


    @Test(priority = 2, groups = "ST")
    public void verifyOrganizationSearch() throws Exception {

        ExcelUtility excel = new ExcelUtility();
        OrganizationsPage organizations = new OrganizationsPage(driver);

        String organizationNumber =
            excel.getDataFromExcel("Organizations", 7, 1);

        organizations.clickOrganizations();
        organizations.searchOrganization(organizationNumber);

        Assert.assertTrue(
            organizations.isOrganizationListed(organizationNumber),
            "Organization was not found: " + organizationNumber
        );

        organizations.clickOrganization(organizationNumber);

        Assert.assertTrue(
            driver.getCurrentUrl().contains("module=Accounts"),
            "Organization details page did not open."
        );

        Reporter.log(
            "ST: Searched and opened " + organizationNumber,
            true
        );
    }

    @Test(priority = 3, groups = "IT")
    public void verifyMassEditOrganizationAndSave() throws Exception {

        ExcelUtility excel = new ExcelUtility();
        OrganizationsPage organizations = new OrganizationsPage(driver);

        String website = excel.getDataFromExcel("Organizations", 8, 2);
        String phone = excel.getDataFromExcel("Organizations", 8, 3);
        String employees = excel.getDataFromExcel("Organizations", 8, 4);

        organizations.clickOrganizations();
        organizations.selectFirstOrganization();
        organizations.clickMassEdit();

        organizations.enterMassEditValue("Website", website);
        organizations.enterMassEditValue("Phone", phone);
        organizations.enterMassEditValue("Employees", employees);

        organizations.saveMassEdit();

        Reporter.log("IT: Organization Mass Edit submitted.", true);
    }
}