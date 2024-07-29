package pages.checkout;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.WebPage;
import pages.home.SiteMenu;

/**
 * This class represents the Order page of the application
 */
@Slf4j
@Getter
public class OrderPage extends WebPage {

    private final SiteMenu menu;

    @FindBy(xpath = "//*[@id='order-confirmation']//h1")
    private WebElement orderConfirmation;

    @FindBy(css = "p[data-id='ordernumber']")
    private WebElement orderNumber;

    public OrderPage(WebDriver driver) {
        super(driver);
        this.menu = new SiteMenu(driver);
    }

    public String getOrderConfirmationText() {
        String orderConfirmationText = orderConfirmation.getText();
        log.info("Order confirmation: " + orderConfirmationText);
        return orderConfirmationText;
    }

    public String getOrderNumber() {
        String orderNumberText = orderNumber.getText().replaceAll("\\D+", "");
        log.info("Order number: " + orderNumberText);
        return orderNumberText;
    }
}
