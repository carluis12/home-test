package dataDto;

import dataDto.checkout.Checkout;
import dataDto.grid.GridItems;
import dataDto.login.Login;
import dataDto.search.Search;
import lombok.Getter;

@Getter
public class TestData extends BaseTestData {

    private Login login;
    private Checkout checkout;
    private GridItems gridItem;
    private Search search;

}
