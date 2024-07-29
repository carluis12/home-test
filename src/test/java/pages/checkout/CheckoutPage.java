package pages.checkout;

import dataDto.checkout.Checkout;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.home.SiteMenu;
import pages.WebPage;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class represents the Checkout page of the application
 */
@Slf4j
@Getter
public class CheckoutPage extends WebPage {

    private final SiteMenu menu;

    @FindBy(id = "fname")
    private WebElement fullName;

    @FindBy(id = "email")
    private WebElement email;

    @FindBy(id = "adr")
    private WebElement address;

    @FindBy(id = "city")
    private WebElement city;

    @FindBy(id = "state")
    private WebElement state;

    @FindBy(id = "zip")
    private WebElement zip;

    //Payment Information
    @FindBy(id = "cname")
    private WebElement nameOnCard;

    @FindBy(id = "ccnum")
    private WebElement cardNumber;

    @FindBy(id = "expmonth")
    private WebElement expMonth;

    @FindBy(id = "expyear")
    private WebElement expYear;

    @FindBy(id = "cvv")
    private WebElement cvv;

    @FindBy(name = "sameadr")
    private WebElement sameAddress;

    @FindBy(xpath = "//button[@class='btn']")
    private WebElement continueBtn;

    @FindBy(xpath = "//*[@class='col-25']//*[@class='container']")
    private WebElement cartContainer;


    public CheckoutPage(WebDriver driver, String path) {
        super(driver);
        driver.get(this.url + path);
        this.menu = new SiteMenu(driver);
    }

    public void fillOutBillingAddress(Checkout checkout) {
        this.fullName.sendKeys(checkout.getFullName());
        this.email.sendKeys(checkout.getEmail());
        this.address.sendKeys(checkout.getAddress());
        this.city.sendKeys(checkout.getCity());
        this.state.sendKeys(checkout.getState());
        this.zip.sendKeys(checkout.getZipCode());
    }

    public void fillOutPaymentInformation(Checkout checkout) {
        this.nameOnCard.sendKeys(checkout.getPayment().getNameOnCard());
        this.cardNumber.sendKeys(checkout.getPayment().getCardNumber());
        selectExpMonth(checkout.getPayment().getExpMonth());
        this.expYear.sendKeys(checkout.getPayment().getExpYear());
        this.cvv.sendKeys(checkout.getPayment().getCvv());
    }

    public void clickContinue() {
        this.continueBtn.click();
    }

    public void fillOutCheckoutAndContinue(Checkout checkout) {
        this.fillOutBillingAddress(checkout);
        this.clickSameAddress(checkout.isSameBillingAddress());
        this.fillOutPaymentInformation(checkout);
        this.clickContinue();
    }

    public OrderPage fillOutCheckoutFormAndContinue(Checkout checkout) {
        this.fillOutCheckoutAndContinue(checkout);
        return new OrderPage(driver);
    }

    public double getTotalPrice() {
        WebElement totalPriceElement = this.cartContainer
                .findElement(By.xpath("//p[contains(text(), 'Total')]/span[@class='price']"));
        String totalPriceText = totalPriceElement.getText();
        log.info("Total Cart Price: " + totalPriceText);
        return Double.parseDouble(totalPriceText.replace("$", ""));
    }

    public double calculateTotalPrice() {
        double totalPrice = this.getProductPrices().stream().mapToDouble(Double::doubleValue).sum();
        log.info("Total price calculated: " + totalPrice);
        return totalPrice;
    }

    private List<Double> getProductPrices() {
        List<WebElement> priceElements = this.cartContainer
                .findElements(By.xpath("//*[not(contains(., 'Total'))]/span[@class='price']"));
        return priceElements.stream().map(WebElement::getText)
                .filter(text -> text.matches("\\$\\d+"))
                .map(text -> Double.parseDouble(text.replace("$", "")))
                .collect(Collectors.toList());
    }

    private void clickSameAddress(boolean sameAddress) {
        if (sameAddress != this.sameAddress.isSelected()) {
            this.sameAddress.click();
            log.info("Checkbox was " + (sameAddress ? "not selected, clicked to select it." : "selected, clicked to deselect it."));
        } else {
            log.info("Checkbox was already " + (sameAddress ? "selected, no click performed." : "deselected, no click performed."));
        }
    }

    private void selectExpMonth(String month) {
        try {
            selectByText(getExpMonth(), month);
        } catch (Exception e) {
            log.error("Error selecting the expiration month: " + e.getMessage());
        }
    }
}
