package objRepo;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

public class HomePage {
	WebDriver driver;
	public HomePage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	@FindBy(xpath = "//img[contains(@onclick,'fnAddWindow')]")
	private WebElement addWidgetButton;

	public void clickAddWidget() {
	    addWidgetButton.click();
	}
	@FindBy(id = "addReportCharts")
	private WebElement addReportOption;
	
	public void clickReportChartsOption() {
		addReportOption.click();
	}
	
	@FindBy(id="stufftitle_id")
	private WebElement windowTitle;
	public void addWindowTitle() {
		windowTitle.sendKeys("WindowTitle");
	}
	
	@FindBy(id = "selreportchart_id")
    private WebElement showDropdown;
// Select Show option using index
 public void selectReportnameByIndex(int index) {
     new Select(showDropdown).selectByIndex(index);
 }
  
  // Module dropdown inside Add Module popup
    @FindBy(id = "selreportcharttype_id")
    private WebElement moduleDropdown;
    public void selectRepoertypeByIndex(int index) {
        new Select(moduleDropdown).selectByIndex(index);
    }

	@FindBy(id = "savebtn")
	private WebElement Save;
	public void clicksave() {
		Save.click();
	}
	
	
}