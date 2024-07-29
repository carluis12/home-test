package dataDto.checkout;

import lombok.Data;

@Data
public class Payment {

    private String nameOnCard;
    private String cardNumber;
    private String expMonth;
    private String expYear;
    private String cvv;

}
