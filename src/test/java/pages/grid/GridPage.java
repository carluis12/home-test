package pages.grid;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.WebPage;

import java.util.List;

/**
 * This class represents the Grid page of the application
 */
@Slf4j
@Getter
public class GridPage extends WebPage {

    @FindBy(xpath = "//*[@class='item']")
    private List<WebElement> items;

    @FindBy(css = "h4[data-test-id='item-name']")
    private List<WebElement> itemTitles;

    @FindBy(css = "p#item-price")
    private List<WebElement> itemPrices;

    @FindBy(tagName = "img")
    private List<WebElement> itemImages;

    @FindBy(css = "button[data-test-id='add-to-order']")
    private List<WebElement> itemButtons;

    public GridPage(WebDriver driver, String path) {
        super(driver);
        driver.get(this.url + path);
    }

    public boolean areAllItemsValid() {
        return items.stream().noneMatch(item ->
                itemTitles.get(items.indexOf(item)).getText().isEmpty() ||
                        itemPrices.get(items.indexOf(item)).getText().isEmpty() ||
                        itemImages.get(items.indexOf(item)).getAttribute("src").isEmpty() ||
                        itemButtons.get(items.indexOf(item)).getText().isEmpty()
        );
    }

    public String getProductNameAtPosition(int position) {
        if (position < 1 || position > items.size()) {
            log.error("Position out of bounds");
            throw new IllegalArgumentException("Position out of bounds");
        }
        return itemTitles.get(position - 1).getText();
    }

    public String getProductPriceAtPosition(int position) {
        if (position < 1 || position > items.size()) {
            log.error("Position out of bounds");
            throw new IllegalArgumentException("Position out of bounds");
        }
        return itemPrices.get(position - 1).getText();
    }

}