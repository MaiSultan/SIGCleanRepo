package view;

import model.HeaderColumns;
import model.InvoiceHeader;
import model.InvoiceLine;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;

public class InvoiceFrame extends JFrame {
private JTable invoiceHeaderTable;
private String[] cols;
private ArrayList<InvoiceLine> invoiceLines = new ArrayList<InvoiceLine>();

private InvoiceHeader invoicesData;

private LayoutManager layoutManager;

Object[][] data;
    public InvoiceFrame(){
        super("Invoice");

        //Start filling invoice lines of the first invoice header
        invoiceLines.add( new InvoiceLine("Blouse", 202.99, 2));
        invoiceLines.add(new InvoiceLine("Jacket", 375.6, 1));

        invoicesData = new InvoiceHeader(1, LocalDateTime.of(2022, Month.DECEMBER, 02, 12, 30), "Ann Ezzat");

        invoicesData.setInvoiceLines(invoiceLines);

        //Prepare JTable data as 2 dimensions object array
        data= new Object[][]{
         {invoicesData.getInvoiceNum(),invoicesData.getInvoiceDate(), invoicesData.getCustomerName(), invoicesData.getInvoiceTotal() }};
        //Columns names
        cols = new String[]{"Invoice Num.", "Date","Customer Name","Total" };

        invoiceHeaderTable = new JTable(data,cols);

        //add the table to the frame
        add(new JScrollPane(invoiceHeaderTable));
        //add(invoiceHeaderTable);

        setTitle("Invoice Header");
        setSize(500,400);
        setLocation(400,400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);



    }


}
