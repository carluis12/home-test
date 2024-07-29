package tests;

import config.dataprovider.TestsDataProvider;
import config.setup.BrowserConfig;
import dataDto.TestData;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.home.HomePage;
import pages.login.LoginPage;

/**
 * Class that contains the tests related to the login functionality.
 */
public class LoginTests extends BrowserConfig {


    @Test(testName = "Login Tests", description = "Login successfully",
            groups = {"LOGIN", "REGRESSION"},
            dataProvider = "TEST_DATA", dataProviderClass = TestsDataProvider.class)
    public void loginSuccessfully(TestData data) throws Exception {
        LoginPage login = new LoginPage(getDriver(), data.getPath());
        HomePage home = login.doLoginSuccessfully(data.getLogin().getUser(), data.getLogin().getPassword());
        Assert.assertEquals(home.getUsername(), data.getLogin().getUser(), "Username is not correct");
    }

    @Test(testName = "Login Tests", description = "Login with wrong credentials",
            groups = {"LOGIN", "REGRESSION"},
            dataProvider = "TEST_DATA", dataProviderClass = TestsDataProvider.class)
    public void loginWrongCredentials(TestData data) {
        LoginPage login = new LoginPage(getDriver(), data.getPath());
        login.doLoginWrongCredentials(data.getLogin().getUser(), data.getLogin().getPassword());
        Assert.assertTrue(login.getErrorMsg().isDisplayed(), "Error message is not displayed");
    }

    @Test(testName = "Login Tests", description = "Login with empty credentials",
            groups = {"LOGIN", "REGRESSION"},
            dataProvider = "TEST_DATA", dataProviderClass = TestsDataProvider.class)
    public void loginEmptyCredentials(TestData data) {
        LoginPage login = new LoginPage(getDriver(), data.getPath());
        login.doLoginWrongCredentials(data.getLogin().getUser(), data.getLogin().getPassword());
        Assert.assertTrue(login.getErrorMsg().isDisplayed(), "Error message is not displayed");
    }

}
