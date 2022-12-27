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


            case "InsertIntoHeaderTable":


                InvoiceHeaderEntryFrame invoiceHeaderEntryFrame = new InvoiceHeaderEntryFrame();

                InvoiceFrame invoiceFrame1 = new InvoiceFrame();
                String dateValue = invoiceHeaderEntryFrame.tfDateEntry.getText();
                System.out.println(dateValue);
                String customerValue = invoiceHeaderEntryFrame.tfCustomerEntry.getText();
                System.out.println(customerValue);

                if(dateValue.equals("") || customerValue.equals("")){
                    JOptionPane.showMessageDialog(invoiceHeaderEntryFrame, "Please enter all invoice data");
                }

                String[] invoiceData1  = {dateValue, customerValue};


                invoiceFrame1.invoiceHeaderModel.addRow(invoiceData1);
                System.out.println("The Invoice Header model is " + invoiceFrame1.invoiceHeaderModel);

                invoiceFrame1.invoiceHeaderTable.setModel(invoiceFrame1.invoiceHeaderModel);
                invoiceHeaderEntryFrame.dispose();

                break;

        }
    }
}
