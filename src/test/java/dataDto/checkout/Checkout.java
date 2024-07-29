package dataDto.checkout;

import lombok.Data;

@Data
public class Checkout {

    private String fullName;
    private String email;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private boolean sameBillingAddress;
    private Payment payment;

}
