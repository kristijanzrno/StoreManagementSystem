package Data;

import java.util.Date;

public class CustomerPurchaseItem extends PurchaseItem{
    private String dateRented;
    private String dateReturned;

    public float calculateRentPrice(){
        return 0f;
    }
}
