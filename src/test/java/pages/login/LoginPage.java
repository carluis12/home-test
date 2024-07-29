package pages.login;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.home.HomePage;
import pages.WebPage;

/**
 * This class represents the Login page of the application
 */
@Slf4j
@Getter
public class LoginPage extends WebPage {

    @FindBy(id = "username")
    private WebElement username;

    @FindBy(id = "password")
    private WebElement password;

    @FindBy(id = "signin-button")
    private WebElement signInBtn;

    @FindBy(id = "message")
    private WebElement errorMsg;


    public LoginPage(WebDriver driver, String path) {
        super(driver);
        driver.get(this.url + path);
    }

    public HomePage doLoginSuccessfully(String user, String password) throws Exception {
        if (validateLoginForm()) {
            fillLoginForm(user, password);
            return new HomePage(driver);
        }
        log.error("An error occurred while trying to Sign In");
        throw new Exception("An error occurred while trying to Sign In");
    }

    public void doLoginWrongCredentials(String user, String password) {
        fillLoginForm(user, password);
        isElementPresent(getErrorMsg());
    }

    private void fillLoginForm(String user, String password) {
        log.info("Filling in the login form");
        getUsername().sendKeys(user);
        getPassword().sendKeys(password);
        getSignInBtn().click();
    }

    public boolean validateLoginForm() {
        return isElementPresent(getUsername()) &&
                isElementPresent(getPassword());
    }

}
