package Data;

import java.util.ArrayList;
import java.util.Date;

public class PurchaseInvoice extends Invoice{
    private int customerID;
    private Date dateRented;
    private ArrayList<CustomerPurchaseItem> items = new ArrayList<>();

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public Date getDateRented() {
        return dateRented;
    }

    public void setDateRented(Date dateRented) {
        this.dateRented = dateRented;
    }

    public ArrayList<CustomerPurchaseItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<CustomerPurchaseItem> items) {
        this.items = items;
    }
}
