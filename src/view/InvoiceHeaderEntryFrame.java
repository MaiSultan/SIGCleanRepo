package view;

import controller.InvoiceActionsListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class InvoiceHeaderEntryFrame extends JFrame {

    String dateValue;
    String customerValue;
    public JTextField tfDateEntry;
    public JTextField tfCustomerEntry;
    public String getDateFieldValue(){
        return dateValue;
    }

    public  void SetDateFieldValue(String dateValue){
       this.dateValue  =  dateValue;
    }

    public String getCustomerFieldValue(){
        return dateValue;
    }

    public  void SetCustomerFieldValue(String customerValue){
        this.customerValue  =  customerValue;
    }

    public InvoiceHeaderEntryFrame(){
        super("Invoice Header Entry");

     DrawInvoiceHeaderEntryUI();




    }


    public void DrawInvoiceHeaderEntryUI(){
        InvoiceActionsListener invoiceActionsListener = new InvoiceActionsListener();
        //Create FlowLayout and set its basic properties
        setLayout(new GridLayout(2,2));
        setTitle("Invoice Header Entry");
        setSize(300,200);
        setLocation(400,300);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);


        Label lblDate = new Label("Invoice Date");
        tfDateEntry = new JTextField(10);
        Label lblCustomer = new Label("Customer Name");
        tfCustomerEntry = new JTextField(10);

        /*JButton btnSaveHeader = new JButton("Save");
        btnSaveHeader.setActionCommand("InsertIntoHeaderTable");//Add new row
        btnSaveHeader.addActionListener(invoiceActionsListener);*/

/*
        JButton btnCancelHeader = new JButton("Cancel");
        btnCancelHeader.setActionCommand("CloseHeadEntry");//Add new row
        btnCancelHeader.addActionListener(invoiceActionsListener);*/

        JPanel myPanel = new JPanel();
        myPanel.add(lblDate);
        myPanel.add(tfDateEntry);
        myPanel.add(Box.createHorizontalStrut(15)); // a spacer
        myPanel.add(lblCustomer);
        myPanel.add(tfCustomerEntry);

        add(myPanel);
        String result = JOptionPane.showInputDialog(null, myPanel,
                "New Invoice", JOptionPane.OK_CANCEL_OPTION);

        switch(Integer.parseInt(result)){

            case JOptionPane.OK_OPTION:
                SetDateFieldValue(tfDateEntry.getText());
                SetDateFieldValue(tfCustomerEntry.getText());
            System.out.println("x value: " + tfDateEntry.getText());
            System.out.println("y value: " + tfCustomerEntry.getText());

                String[] invoiceData  = {getDateFieldValue(), getCustomerFieldValue()};
                InvoiceFrame invoiceFrame = new InvoiceFrame();
                invoiceFrame.invoiceHeaderModel.addRow(invoiceData);
                System.out.println("The Invoice Header model is " + invoiceFrame.invoiceHeaderModel);
                //invoiceHeaderModel.addRow(invoiceData);
                invoiceFrame.invoiceHeaderTable.setModel(invoiceFrame.invoiceHeaderModel);

            break;
           /* case JOptionPane.CANCEL_OPTION:
                JOptionPane.*/
        }
/*
        add(lblDate);
        add(tfDateEntry);
        add(lblCustomer);
        add(tfCustomerEntry);
        add(btnSaveHeader);
        add(btnCancelHeader);
        pack();*/

            //String[] newInvoiceData = {tfDateEntry.getText(),tfCustomerEntry.getText() };





    }




}
