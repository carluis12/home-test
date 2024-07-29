package tests;

import config.dataprovider.TestsDataProvider;
import config.setup.BrowserConfig;
import dataDto.TestData;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.grid.GridPage;

/**
 * Class that contains the tests related to the grid functionality.
 */
public class GridTests extends BrowserConfig {

    @Test(testName = "Grid Item Test", description = "Validate the grid item",
            groups = {"GRID", "REGRESSION"},
            dataProvider = "TEST_DATA", dataProviderClass = TestsDataProvider.class)
    public void gridItemTest(TestData data) {
        GridPage grid = new GridPage(getDriver(), data.getPath());
        Assert.assertEquals(grid.getProductNameAtPosition(data.getGridItem().getPosition()),
                data.getGridItem().getName(), "Product name is not correct.");
        Assert.assertEquals(grid.getProductPriceAtPosition(data.getGridItem().getPosition()),
                data.getGridItem().getPrice(), "Product price is not correct.");

    }

    @Test(testName = "Grid All Items Test", description = "Validate all items in the grid",
            groups = {"GRID", "REGRESSION"},
            dataProvider = "TEST_DATA", dataProviderClass = TestsDataProvider.class)
    public void gridAllItems(TestData data) {
        GridPage grid = new GridPage(getDriver(), data.getPath());
        Assert.assertTrue(grid.areAllItemsValid(), "Not all items are valid.");

    }
}
