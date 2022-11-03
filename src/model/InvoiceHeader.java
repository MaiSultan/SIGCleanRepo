package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

/*
This class is a parent class for Invoice Lines
/ */
public class InvoiceHeader {
   private int invoiceNum;
   private LocalDateTime invoiceDate;
   private String customerName;

   double invoiceTotal;

    @Override
    public String toString() {
        return "InvoiceHeader{" +
                "invoiceNum=" + invoiceNum +
                ", invoiceDate=" + invoiceDate +
                ", customerName='" + customerName + '\'' +
                ", invoiceTotal=" + invoiceTotal +
                '}';
    }

    private ArrayList<InvoiceLine> invoiceLines;

    public ArrayList<InvoiceLine> getInvoiceLines() {
        return invoiceLines;
    }

    public void setInvoiceLines(ArrayList<InvoiceLine> invoiceLines) {
        this.invoiceLines = invoiceLines;
    }

    public InvoiceHeader(int invoiceNum, LocalDateTime invoiceDate, String customerName) {
        this.invoiceNum = invoiceNum;
        this.invoiceDate = invoiceDate;
        this.customerName = customerName;
    }

    public int getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(int invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public LocalDateTime getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDateTime invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * This method calculates the total price for all items purchased in the invoice
     */
    public double getInvoiceTotal(){
       for(int line = 0; line<invoiceLines.size(); line++)
           invoiceTotal += invoiceLines.get(line).getItemTotal();
       return invoiceTotal;
    }
}
