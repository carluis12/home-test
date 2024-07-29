package pages.home;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.WebPage;

/**
 * This class represents the Dashboard page of the application
 */
@Slf4j
@Getter
public class HomePage extends WebPage {

    private SiteMenu menu;

    @FindBy(id = "welcome-message")
    private WebElement welcomeMessage;

    @FindBy(css = "p[data-id='username']")
    private WebElement username;

    public HomePage(WebDriver driver) {
        super(driver);
        this.menu = new SiteMenu(driver);
    }

    public String getWelcomeMessage() {
        return welcomeMessage.getText();
    }

    public String getUsername() {
        return username.getText();
    }

}
