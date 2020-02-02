package Data;

import java.util.Date;

public class StockItem {
    private int itemID;
    private String itemDescription;
    private String itemType;
    private int itemManufacturerPartNum;
    private String itemPurchaseInfo;
    private String itemDescriptionOfSale;
    private float itemCost;
    private float itemSalesPrice;
    private String itemPreferredSupplier;
    private float itemVAT;
    private String itemlastModified;
    private boolean itemisRentable;
    private int itemQuantity;

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public int getItemManufacturerPartNum() {
        return itemManufacturerPartNum;
    }

    public void setItemManufacturerPartNum(int itemManufacturerPartNum) {
        this.itemManufacturerPartNum = itemManufacturerPartNum;
    }

    public String getItemPurchaseInfo() {
        return itemPurchaseInfo;
    }

    public void setItemPurchaseInfo(String itemPurchaseInfo) {
        this.itemPurchaseInfo = itemPurchaseInfo;
    }

    public String getItemDescriptionOfSale() {
        return itemDescriptionOfSale;
    }

    public void setItemDescriptionOfSale(String itemDescriptionOfSale) {
        this.itemDescriptionOfSale = itemDescriptionOfSale;
    }

    public float getItemCost() {
        return itemCost;
    }

    public void setItemCost(float itemCost) {
        this.itemCost = itemCost;
    }

    public float getItemSalesPrice() {
        return itemSalesPrice;
    }

    public void setItemSalesPrice(float itemSalesPrice) {
        this.itemSalesPrice = itemSalesPrice;
    }

    public String getItemPreferredSupplier() {
        return itemPreferredSupplier;
    }

    public void setItemPreferredSupplier(String itemPreferredSupplier) {
        this.itemPreferredSupplier = itemPreferredSupplier;
    }

    public float getItemVAT() {
        return itemVAT;
    }

    public void setItemVAT(float itemVAT) {
        this.itemVAT = itemVAT;
    }

    public String getItemlastModified() {
        return itemlastModified;
    }

    public void setItemlastModified(String itemlastModified) {
        this.itemlastModified = itemlastModified;
    }

    public boolean isItemisRentable() {
        return itemisRentable;
    }

    public void setItemisRentable(boolean itemisRentable) {
        this.itemisRentable = itemisRentable;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }
}
