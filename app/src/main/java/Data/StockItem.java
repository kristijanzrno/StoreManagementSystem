package Data;

public class StockItem {
    private int itemID;
    private String name;
    private String description;
    private int quantity;
    private String type;
    private int manufacturerPartNum;
    private float cost;
    private float salesPrice;
    private String preferredSupplier;
    private float VAT;
    private String dateCreated;
    private String lastModified;
    private boolean isRentable;

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getManufacturerPartNum() {
        return manufacturerPartNum;
    }

    public void setManufacturerPartNum(int manufacturerPartNum) {
        this.manufacturerPartNum = manufacturerPartNum;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public float getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(float salesPrice) {
        this.salesPrice = salesPrice;
    }

    public String getPreferredSupplier() {
        return preferredSupplier;
    }

    public void setPreferredSupplier(String preferredSupplier) {
        this.preferredSupplier = preferredSupplier;
    }

    public float getVAT() {
        return VAT;
    }

    public void setVAT(float VAT) {
        this.VAT = VAT;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public boolean isRentable() {
        return isRentable;
    }

    public void setRentable(boolean rentable) {
        isRentable = rentable;
    }
}
