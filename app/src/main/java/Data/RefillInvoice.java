package Data;

import java.util.ArrayList;

public class RefillInvoice extends Invoice{
    private String supplierID;
    private String shipmentAddress;
    private ArrayList<PurchaseItem> items;

    public String getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

    public String getShipmentAddress() {
        return shipmentAddress;
    }

    public void setShipmentAddress(String shipmentAddress) {
        this.shipmentAddress = shipmentAddress;
    }

    public ArrayList<PurchaseItem> getItems(){
        return this.items;
    }

    public void setItems(ArrayList<PurchaseItem> items){
        this.items = items;
    }
}
