package pages.home;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.WebPage;

@Getter
public class SiteMenu extends WebPage {

    @FindBy(xpath = "//*[descendant-or-self::text()='Home']")
    private WebElement home;

    @FindBy(xpath = "//*[descendant-or-self::text()='Form']")
    private WebElement form;

    @FindBy(xpath = "//*[descendant-or-self::text()='Grid']")
    private WebElement grid;

    @FindBy(xpath = "//*[descendant-or-self::text()='Search']")
    private WebElement search;

    public SiteMenu(WebDriver driver) {
        super(driver);
    }
}
