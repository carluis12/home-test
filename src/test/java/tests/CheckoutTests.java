package tests;

import config.dataprovider.TestsDataProvider;
import config.setup.BrowserConfig;
import dataDto.TestData;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.checkout.CheckoutPage;
import pages.checkout.OrderPage;

/**
 * Class that contains the tests related to the checkout functionality.
 */
public class CheckoutTests extends BrowserConfig {

    @Test(testName = "Checkout Form Order Success", description = "Checkout form order success",
            groups = {"CHECKOUT", "REGRESSION"},
            dataProvider = "TEST_DATA", dataProviderClass = TestsDataProvider.class)
    public void checkoutFormOrderSuccess(TestData data) {
        CheckoutPage checkout = new CheckoutPage(getDriver(), data.getPath());
        OrderPage order = checkout.fillOutCheckoutFormAndContinue(data.getCheckout());
        Assert.assertFalse(order.getOrderNumber().isEmpty(), "Order number is empty.");
    }


    @Test(testName = "Checkout Form Alert", description = "Validate alert when sameBillingAddress is false",
            groups = {"CHECKOUT", "REGRESSION"},
            dataProvider = "TEST_DATA", dataProviderClass = TestsDataProvider.class)
    public void checkoutFormAlert(TestData data) {
        CheckoutPage checkout = new CheckoutPage(getDriver(), data.getPath());
        checkout.fillOutCheckoutAndContinue(data.getCheckout());
        // Assert that the alert is displayed
        Assert.assertTrue(checkout.isAlertPresent(), "Alert is not present.");
        // Close the alert
        checkout.alertHandler(true);
        Assert.assertFalse(checkout.isAlertPresent(), "Alert is still present.");
    }

    @Test(testName = "Cart Total Test", description = "Validate the cart total",
            groups = {"CHECKOUT", "REGRESSION"},
            dataProvider = "TEST_DATA", dataProviderClass = TestsDataProvider.class)
    public void cartTotal(TestData data) {
        CheckoutPage checkout = new CheckoutPage(getDriver(), data.getPath());
        Assert.assertEquals(checkout.calculateTotalPrice(), checkout.getTotalPrice(), "Cart total is not correct.");

    }
}
