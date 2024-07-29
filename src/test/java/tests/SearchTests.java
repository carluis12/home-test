package tests;

import config.dataprovider.TestsDataProvider;
import config.setup.BrowserConfig;
import dataDto.TestData;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.search.SearchPage;

/**
 * Class that contains the tests related to the search functionality.
 */
public class SearchTests extends BrowserConfig {

    @Test(testName = "Search Success", description = "Search successfully",
            groups = {"SEARCH", "REGRESSION"},
            dataProvider = "TEST_DATA", dataProviderClass = TestsDataProvider.class)
    public void searchSuccess(TestData data) {
        SearchPage search = new SearchPage(getDriver(), data.getPath());
        search.searchFor(data.getSearch().getWord());
        Assert.assertEquals(search.getSearchResultText(), data.getSearch().getResult());

    }

    @Test(testName = "Search Empty", description = "Validate search with empty field",
            groups = {"SEARCH", "REGRESSION"},
            dataProvider = "TEST_DATA", dataProviderClass = TestsDataProvider.class)
    public void searchEmpty(TestData data) {
        SearchPage search = new SearchPage(getDriver(), data.getPath());
        search.searchFor(data.getSearch().getWord());
        Assert.assertEquals(search.getSearchResultText(), data.getSearch().getResult());

    }

}
