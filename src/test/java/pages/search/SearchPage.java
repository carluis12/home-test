package pages.search;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import pages.WebPage;

/**
 * This class represents the Search page of the application
 */
@Slf4j
@Getter
public class SearchPage extends WebPage {

    @FindBy(xpath = "//*[@name='searchWord']")
    private WebElement searchField;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement searchButton;

    @FindBy(id = "result")
    private WebElement searchResult;

    public SearchPage(WebDriver driver, String path) {
        super(driver);
        driver.get(this.url + path);
    }

    public void searchFor(String searchWord) {
        log.info("Searching for: " + searchWord);
        getSearchField().sendKeys(searchWord);
        getSearchButton().click();
    }

    public boolean isSearchFieldPresent() {
        return isElementPresent(getSearchField());
    }

    public String getSearchResultText() {
        wait.until((ExpectedCondition<Boolean>) driver ->
                !getSearchResult().getText().equals("searching..."));
        return getSearchResult().getText();
    }

}
