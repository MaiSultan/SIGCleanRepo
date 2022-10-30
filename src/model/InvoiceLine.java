package model;

/*
This class is for recording each line in the invoice
/ */
public class InvoiceLine {

    private String itemName;
    private double itemPrice;
    private int count;

    @Override
    public String toString() {
        return "InvoiceLine{" +
                "itemName='" + itemName + '\'' +
                ", itemPrice=" + itemPrice +
                ", count=" + count +
                ", itemTotal=" + itemTotal +
                '}';
    }

    private double itemTotal;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    /**
     * @return Retrieves the price of single item count
     */
    public double getItemTotal(){
        return itemTotal = itemPrice * count;
    }

    /**
     * This constructor is to initialize a new invoice line given item name, its price and the item count
     */
    public InvoiceLine(String itemName, double itemPrice, int count) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.count = count;
        this.getItemTotal();
    }
}
