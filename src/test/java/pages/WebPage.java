package pages;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import util.constants.Config;
import util.properties.PropertyUtils;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

/**
 * This class represents the WebPage of the application.
 * It contains common methods for all pages and is the parent class of all pages.
 */
@Slf4j
public class WebPage {
    protected WebDriverWait wait;
    protected WebDriver driver;
    protected Properties projectProperties;
    protected String url;
    protected JavascriptExecutor jsExecutor;


    public WebPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        this.waitUntilDocumentReadyStateIsComplete();
        this.jsExecutor = (JavascriptExecutor) driver;
        this.projectProperties = PropertyUtils
                .readProperty(Config.PROPERTIES, new Properties(), this.getClass());
        this.url = "docker".equalsIgnoreCase(System.getenv("environment"))
                ? projectProperties.getProperty("docker.site.url")
                : projectProperties.getProperty("site.url");
    }


    public boolean isElementPresent(WebElement element) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            return element.getSize().height > 0;
        } catch (Exception e) {
            log.error("Element is not present: " + e.getMessage());
            return false;
        }
    }

    public void selectByText(WebElement element, String text) throws Exception {
        Select select = new Select(element);
        List<WebElement> options = select.getOptions();

        for (WebElement opt : options) {
            if (opt.getText().equalsIgnoreCase(text)) {
                select.selectByVisibleText(text);
                return;
            }
        }
        log.error("The " + text + " value is not present in the selectable options");
        throw new Exception("The " + text + " value is not present in the selectable options");
    }

    public boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    public void alertHandler(boolean accept) {
        Alert alert = driver.switchTo().alert();
        String alertText = alert.getText();
        log.info("Alert text: " + alertText);
        if (accept) {
            alert.accept();
        } else {
            alert.dismiss();
        }
    }

    private void waitUntilDocumentReadyStateIsComplete() {
        wait.until((ExpectedCondition<Boolean>) wd ->
        {
            assert wd != null;
            return ((JavascriptExecutor) wd).executeScript("return document.readyState")
                    .equals("complete");
        });
    }

}

