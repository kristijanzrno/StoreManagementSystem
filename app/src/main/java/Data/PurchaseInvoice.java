package Data;

import java.util.ArrayList;
import java.util.Date;

public class PurchaseInvoice extends Invoice{
    private ArrayList<CustomerPurchaseItem> items = new ArrayList<>();
    private boolean hasRentedItems;

    public ArrayList<CustomerPurchaseItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<CustomerPurchaseItem> items) {
        this.items = items;
    }

    public boolean isHasRentedItems() {
        return hasRentedItems;
    }

    public void setHasRentedItems(boolean hasRentedItems) {
        this.hasRentedItems = hasRentedItems;
    }
    public boolean addItem(CustomerPurchaseItem item){
        for(int i = 0; i < items.size(); i++){
            if(items.get(i).getItemID().equals(item.getItemID())){
                items.get(i).setQuantity(items.get(i).getQuantity() + item.getQuantity());
                return true;
            }
        }
        this.items.add(item);
        return false;
    }
}
