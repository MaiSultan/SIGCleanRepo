package controller;

import model.InvoiceHeader;
import model.InvoiceHeaderModel;
import view.InvoiceFrame;
import view.InvoiceHeaderEntryFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.Arrays;

public class InvoiceActionsListener implements ActionListener {

    /**
     * Invoked when an action occurs.
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {


        switch(e.getActionCommand()) {
            case "NewInvoice":
                InvoiceFrame invoiceFrame = new InvoiceFrame();
                //invoiceHeaderEntryFrame.setVisible(true);
                Label lblDate = new Label("Invoice Date");
                JTextField tfDateEntry = new JTextField(10);
                Label lblCustomer = new Label("Customer Name");
                JTextField tfCustomerEntry = new JTextField(10);
                JPanel myPanel = new JPanel();
                myPanel.add(lblDate);
                myPanel.add(tfDateEntry);
                myPanel.add(Box.createHorizontalStrut(15)); // a spacer
                myPanel.add(lblCustomer);
                myPanel.add(tfCustomerEntry);

                int result = JOptionPane.showConfirmDialog(null, myPanel,
                        "New Invoice", JOptionPane.OK_CANCEL_OPTION);


                switch(result) {
                    case JOptionPane.OK_OPTION:
                        String[] invoiceData = new String[0];
                        int rowCount = 0;
                        String tfDateValue = tfDateEntry.getText();
                        String tfCustomerValue = tfCustomerEntry.getText();
                        System.out.println("Invoice Date's value: " + tfDateValue);
                        System.out.println("Invoice Customer's value: " + tfCustomerValue);


                        DefaultTableModel invoiceHeaderTableModel= (DefaultTableModel) invoiceFrame.invoiceHeaderTable.getModel();
                        rowCount= invoiceHeaderTableModel.getRowCount();
                        if(invoiceHeaderTableModel != null){

                            rowCount ++;
                        }
                        System.out.println("Row count is : " + rowCount);
                        invoiceData = new String[]{String.valueOf(rowCount), tfDateValue, tfCustomerValue};
                        //invoiceFrame.invoiceHeaderModel.addRow(invoiceData);
                        System.out.println("The Invoice Header model is " + invoiceFrame.invoiceHeaderModel);
                        invoiceHeaderTableModel.addRow(invoiceData);
                        invoiceFrame.invoiceHeaderTable.setModel(invoiceFrame.invoiceHeaderModel);

                        break;
           /* case JOptionPane.CANCEL_OPTION:
                JOptionPane.*/
                }
                break;

            case "InsertIntoHeaderTable":


                InvoiceHeaderEntryFrame invoiceHeaderEntryFrame = new InvoiceHeaderEntryFrame();

               /* Component[] headerEntryComps = invoiceHeaderEntryFrame.getComponents();
                JTextField tfDate = (JTextField) headerEntryComps[1];
                JTextField tfCustomer = (JTextField) headerEntryComps[3];*/

                InvoiceFrame invoiceFrame1 = new InvoiceFrame();
                String dateValue = invoiceHeaderEntryFrame.tfDateEntry.getText();
                System.out.println(dateValue);
                //String customerValue = invoiceHeaderEntryFrame.getCustomerFieldValue();
                String customerValue = invoiceHeaderEntryFrame.tfCustomerEntry.getText();
                System.out.println(customerValue);

                if(dateValue.equals("") || customerValue.equals("")){
                    JOptionPane.showMessageDialog(invoiceHeaderEntryFrame, "Please enter all invoice data");
                }
                //String dateValue = invoiceHeaderEntryFrame.getDateFieldValue();


                //InvoiceHeaderModel invoiceHeaderModel = new InvoiceHeaderModel();
                String[] invoiceData1  = {dateValue, customerValue};
                //Object[] columnNames = new String[]{invoiceHeaderModel.getColumnName(1), invoiceHeaderModel.getColumnName(2)};


                //DefaultTableModel tblInvoiceHeaderModel = new DefaultTableModel((Object[][]) invoiceData, columnNames);
                //DefaultTableModel invoiceHeaderModel = (DefaultTableModel) invoiceFrame.invoiceHeaderTable.getModel();

                invoiceFrame1.invoiceHeaderModel.addRow(invoiceData1);
                System.out.println("The Invoice Header model is " + invoiceFrame1.invoiceHeaderModel);
                //invoiceHeaderModel.addRow(invoiceData);
                invoiceFrame1.invoiceHeaderTable.setModel(invoiceFrame1.invoiceHeaderModel);
                invoiceHeaderEntryFrame.dispose();


               /* InvoiceHeaderModel invoiceHeaderModel = (InvoiceHeaderModel) invoiceFrame.invoiceHeaderTable.getModel();
                System.out.println(invoiceHeaderModel);
                InvoiceHeader invoice = new InvoiceHeader(0, dateValue, customerValue);
                invoiceHeaderModel.addInvoice(invoice);*/
                //invoiceFrame.invoiceHeaderTable.

                break;

            case "CloseHeadEntry":
                break;

            case "DeleteInvoice":
                break;


            case"DeleteItem":
                break;
        }
    }
}
