package Models;

public class InvoiceLine {
    String itemName;
    double itemPrice;
    int count;
    double itemTotal;

    public double getItemTotal(){
        return itemPrice * count;
    }
}
