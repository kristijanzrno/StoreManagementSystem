package Data;


public class CustomerPurchaseItem extends PurchaseItem{
    private String dateRented;
    private String dateReturned;

    public String getDateRented() {
        return dateRented;
    }

    public void setDateRented(String dateRented) {
        this.dateRented = dateRented;
    }

    public String getDateReturned() {
        return dateReturned;
    }

    public void setDateReturned(String dateReturned) {
        this.dateReturned = dateReturned;
    }

    public float calculateRentPrice(){
        return 0f;
    }
}
