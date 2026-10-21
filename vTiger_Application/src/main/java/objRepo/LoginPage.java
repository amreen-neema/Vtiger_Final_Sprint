package objRepo;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

	WebDriver driver;
	public LoginPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}


	// Username
	@FindBy(name = "user_name")
	private WebElement username;
	public WebElement getUsername() {
		return username ;
	}


	// Password
	@FindBy(name = "user_password")
	private WebElement password;
	public WebElement getPassword( ) {
		return password;
	}


	// Login button
	@FindBy(id = "submitButton")
	private WebElement loginbtn;
	public WebElement getLoginbtn() {
		return loginbtn;
	}


	// Error message
	
	@FindBy(xpath = "//div[@class='errorMessage']")
	private WebElement errorMessage;

	public String getErrorMessage() {
	    return errorMessage.getText().trim();
	}


	// Complete login
	public void login(String un, String pwd) throws Exception {
		Thread.sleep(2000);
		username.sendKeys(un);
		password.sendKeys(pwd);
		loginbtn.click();
	}
}