package view;

import controller.FileOperations;
import controller.InvoiceActionsListener;
import model.InvoiceHeader;
import model.InvoiceLine;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class InvoiceFrame extends JFrame implements ActionListener{
public JTable invoiceHeaderTable;
public JTable tbl_invoiceItems;

private Object[] cols_InvoiceHeader;//Array of objects for Invoice Header columns

    private Object[] cols_InvoiceItems;//Array of objects for Invoice Items columns
private ArrayList<InvoiceLine> invoiceLines = new ArrayList<InvoiceLine>();

private InvoiceHeader invoicesData;

private LayoutManager layoutManager;
public DefaultTableModel invoiceHeaderModel;
public DefaultTableModel invoiceItemsModel ;
public File selectedFile_Items;
public int invNo = 0;
public JTextField tfInvoiceNoVal;
public JTextField tfInvTotalVal;

Object[][] data_InvoiceHeader;
    public InvoiceFrame(){
        super("Invoice");
        //Create GridLayout and set its basic properties
        GridLayout gridLayout = new GridLayout(1,2);
        setLayout(gridLayout);
        setTitle("Invoice Header");
        setSize(1000,700);
        setLocation(300,300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        gridLayout.setVgap(-1);
        gridLayout.setHgap(-1);

        //////////////////////////////////////////
        InvoiceActionsListener invoiceActionsListener = new InvoiceActionsListener();
        /////////////////////////////////////////

        JPanel panelLeft_InvoiceHeader = new JPanel();

        //invoicesData = new InvoiceHeader(1, LocalDate.of(2022, Month.DECEMBER, 02), "Ann Ezzat");
        invoicesData = new InvoiceHeader(1, "02-12-2022", "Ann Ezzat");
        invoicesData.setInvoiceLines(invoiceLines);

        //Invoice Header Columns names
        cols_InvoiceHeader = new Object[]{"No.", "Date","Customer","Total" };
        //Set JTable Invoice Header to dynamic data model
        invoiceHeaderModel = new DefaultTableModel(cols_InvoiceHeader, 0);
        invoiceHeaderTable = new JTable(invoiceHeaderModel);
        invoiceHeaderTable.setRowSelectionAllowed(true);
        invoiceHeaderTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //Create Menu bar
        JMenuBar menuBarInvoice = new JMenuBar();
        JMenu menuFile = new JMenu("File");

        //Set options to Load File Menu Item
        JMenuItem menuItemLoad = new JMenuItem("Load File", 'L');
        menuItemLoad.setAccelerator(KeyStroke.getKeyStroke('L', KeyEvent.CTRL_DOWN_MASK));
        menuItemLoad.setActionCommand("Load");
        menuItemLoad.addActionListener(this);

        //Set options to Load File Menu Item
        JMenuItem menuItemSave = new JMenuItem("Save File", 'S');
        menuItemSave.setAccelerator(KeyStroke.getKeyStroke('S', KeyEvent.CTRL_DOWN_MASK));
        menuItemSave.setActionCommand("Save");
        menuItemSave.addActionListener(this);

        //Add Menu Items to Menu
        menuFile.add(menuItemLoad);
        menuFile.add(menuItemSave);
        //Add Menu to MenuBar
        menuBarInvoice.add(menuFile);
        //Add Menu Bar to the frame
        setJMenuBar(menuBarInvoice);

        //Create Buttons
        JButton btnCreateInvoice = new JButton("Create New Invoice");
        btnCreateInvoice.setActionCommand("NewInvoice");
        //Set Action Listener
        btnCreateInvoice.addActionListener(this);

        JButton btnDeleteInovice = new JButton("Delete Invoice");
        btnDeleteInovice.setActionCommand("DeleteInvoice");
        //Set Action Listener
        btnDeleteInovice.addActionListener(this);


        //add the table to the frame
        //1. Adding Invoice Header Table component to Left panel
        panelLeft_InvoiceHeader.add(new JScrollPane( invoiceHeaderTable));

        panelLeft_InvoiceHeader.add(btnCreateInvoice);
        panelLeft_InvoiceHeader.add(btnDeleteInovice);

        add(panelLeft_InvoiceHeader);

        JLabel invNoLbl = new JLabel("Invoice Number");
        tfInvoiceNoVal = new JTextField("",30);
        tfInvoiceNoVal.setEditable(false);

        JLabel invDateLbl = new JLabel("Invoice Date");
        JTextField tfInvDate = new JTextField(30);

        JLabel custNameLbl = new JLabel("Customer Name");
        JTextField tfCustName = new JTextField(30);

        JLabel invTotalLbl = new JLabel("Invoice Total");
         tfInvTotalVal = new JTextField("",30);
        tfInvTotalVal.setEditable(false);

        JPanel panel_invoiceFields = new JPanel();//new FlowLayout()
        //panel_invoiceFields.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        panel_invoiceFields.add(invNoLbl);
        panel_invoiceFields.add(tfInvoiceNoVal);

        panel_invoiceFields.add(invDateLbl);
        panel_invoiceFields.add(tfInvDate);

        panel_invoiceFields.add(custNameLbl);
        panel_invoiceFields.add(tfCustName);

        panel_invoiceFields.add(invTotalLbl);
        panel_invoiceFields.add(tfInvTotalVal);

        //Invoice Data Columns names
        JPanel panel_invoiceItems = new JPanel();
        Border border_invItems = BorderFactory.createTitledBorder("Invoice Items");
        panel_invoiceItems.setBorder(border_invItems);

        cols_InvoiceItems = new Object[]{"No.", "Item Name","Item Price", "Count", "Item Total" };


        //Set JTable Invoice Items to dynamic data model
        invoiceItemsModel = new DefaultTableModel(cols_InvoiceItems, 0);
        tbl_invoiceItems = new JTable(invoiceItemsModel);
        //TODO Set Invoice Header cells to be non editable

        panel_invoiceItems.add(new JScrollPane(tbl_invoiceItems));
        panel_invoiceFields.add(panel_invoiceItems);

        JButton btnSaveItem = new JButton("Create Item");
        btnSaveItem.setActionCommand("SaveItem");
        btnSaveItem.addActionListener(this);


        JButton btnDeleteItem = new JButton("Delete Item");
        btnDeleteItem.setActionCommand("DeleteItem");
        btnDeleteItem.addActionListener(this);

        panel_invoiceFields.add(btnSaveItem);
        panel_invoiceFields.add(btnDeleteItem);

        add(panel_invoiceFields);

        //Adding a listener to table rows when click on any of them
        final String[] selectedInvoiceNo = {"0"};
        AtomicInteger selectedRowIndex = new AtomicInteger();
        invoiceHeaderTable.getSelectionModel().addListSelectionListener(event -> {
            // do some actions here, for example
            // print first column value from selected row, Invoice No.
            selectedRowIndex.set(invoiceHeaderTable.getSelectedRow());
            selectedInvoiceNo[0] =invoiceHeaderTable.getValueAt(selectedRowIndex.get(), 0).toString();
            System.out.println(selectedInvoiceNo[0]);

            invoiceItemsModel = new DefaultTableModel(cols_InvoiceItems, 0);;
            tbl_invoiceItems.setModel(invoiceItemsModel);//Clear Invoice Items model once a new invoice header selection done

            //Call function to load invoice lines based on selected invoice no.
            FileOperations operations = new FileOperations();
            //Send Invoice Lines file for reading

            ArrayList<InvoiceLine> invoiceLinesArr;
            if(selectedFile_Items== null && selectedFile_Items.length()== 0){
               JOptionPane.showMessageDialog(this,"Invoice items file is empty", "Invoice Items File Error", JOptionPane.ERROR_MESSAGE);

            }

            System.out.println(selectedFile_Items);
            invoiceLinesArr = operations.loadInvoiceLinesByInvoiceNo(selectedInvoiceNo[0], selectedFile_Items);
            int invoiceTotal = 0;
            //Load the read inovice headers in invoice header table
            if(invoiceLinesArr != null && invoiceLinesArr.size()>0) {
                int invLine;


                for (invLine = 0; invLine < invoiceLinesArr.size(); invLine++) {
                    invNo = invoiceLinesArr.get(invLine).getInvoiceNum();
                    invoiceTotal+= (invoiceLinesArr.get(invLine)).getItemTotal();
                    invoiceItemsModel.addRow(new String[]{
                                                String.valueOf(invoiceLinesArr.get(invLine).getInvoiceNum()),
                                                invoiceLinesArr.get(invLine).getItemName(),
                                                String.valueOf(invoiceLinesArr.get(invLine).getItemPrice()),
                                                String.valueOf(invoiceLinesArr.get(invLine).getCount()),
                                                String.valueOf(invoiceLinesArr.get(invLine).getItemTotal())
                                                        });
                }

                tbl_invoiceItems.setModel(invoiceItemsModel);
                System.out.println("The total invoice cost is : "+  invoiceTotal);

                //Set Invoice total price based of the summation of total invoice items inside Invoice Header Table
                invoiceHeaderTable.setValueAt(invoiceTotal,selectedRowIndex.get() , 3);

                //Set Invoice No and Invoice Total in non-editable text fields
                tfInvoiceNoVal.setText(String.valueOf(invNo));
                tfInvTotalVal.setText(String.valueOf(invoiceTotal));
                tfInvDate.setText(invoiceHeaderTable.getValueAt(selectedRowIndex.get(), 1).toString());
                tfCustName.setText(invoiceHeaderTable.getValueAt(selectedRowIndex.get(), 2).toString());

            }
            else{
                //In case of there is no items purchased for the selected invoice
                tfInvoiceNoVal.setText(String.valueOf(invoiceHeaderTable.getValueAt(selectedRowIndex.get(), 0)));
                tfInvTotalVal.setText("0");
                tfInvDate.setText(invoiceHeaderTable.getValueAt(selectedRowIndex.get(), 1).toString());
                tfCustName.setText(invoiceHeaderTable.getValueAt(selectedRowIndex.get(), 2).toString());

            }
        });

    }

    public void AddNewInvoice() throws ParseException {

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


                DefaultTableModel invoiceHeaderTableModel= (DefaultTableModel) invoiceHeaderTable.getModel();
                rowCount= invoiceHeaderTableModel.getRowCount();
                if(invoiceHeaderTableModel != null){

                    rowCount ++;
                }
                System.out.println("Row count is : " + rowCount);


                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.US);
                LocalDate localDate = LocalDate.parse(tfDateValue, formatter);//For reference
                System.out.println("The invoice date after format : " + localDate.toString());
                //String formattedStringInvoiceDate = localDate.format(formatter);

               /* DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                LocalDate invoiceDateFormatted = LocalDate.parse(tfDateValue, df);*/

               /* DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                Date date = df.parse(tfDateValue);*/
               /*String[] dateArray =new String[] {localDate.toString()};
                System.out.println("The invoice date after format : " + dateArray.toString());
                String[] dateArrayReversed =reverseArray(dateArray);*/

                invoiceData = new String[]{String.valueOf(rowCount), tfDateValue, tfCustomerValue};
                //invoiceFrame.invoiceHeaderModel.addRow(invoiceData);
                System.out.println("The Invoice Header model is " + invoiceHeaderModel);
                invoiceHeaderModel.addRow(invoiceData);
                invoiceHeaderTable.setModel(invoiceHeaderModel);

                break;
           /* case JOptionPane.CANCEL_OPTION:
                JOptionPane.*/
        }
    }

    /*
    This method reverse the order of formatted date to be in the form of dd-MM-yyyy
     */
    public static String[] reverseArray(String[] dateArray){
        for (int left = 0, right = dateArray.length - 1;
             left < right; left++, right--) {
            // swap the values at the left and right indices
            String temp = dateArray[left];
            dateArray[left] = dateArray[right];
            dateArray[right] = temp;
        }
        return dateArray;
    }

    public void AddNewItem(int invoiceNo){
        Label lblItemName = new Label("Item Name");
        JTextField tfItemName = new JTextField(10);
        Label lblItemPrice = new Label("Item Price");
        JTextField tfItemPrice = new JTextField(10);
        Label lblItemCount = new Label("Item Count");
        JTextField tfItemCount = new JTextField(10);
        JPanel panelItems = new JPanel();
        panelItems.add(lblItemName);
        panelItems.add(tfItemName);

        panelItems.add(Box.createHorizontalStrut(15)); // a spacer

        panelItems.add(lblItemPrice);
        panelItems.add(tfItemPrice);

        panelItems.add(Box.createHorizontalStrut(15)); // a spacer

        panelItems.add(lblItemCount);
        panelItems.add(tfItemCount);

        int option = JOptionPane.showConfirmDialog(null, panelItems,
                "New Invoice", JOptionPane.OK_CANCEL_OPTION);

        switch(option) {
            case JOptionPane.OK_OPTION:
                //String[] ItemData = new String[0];
                int rowCount = 0;
                String tfItemNameValue = tfItemName.getText();
                String tfItemPriceValue = tfItemPrice.getText();
                String tfItemCountValue = tfItemCount.getText();
                System.out.println("Item Name's value: " + tfItemNameValue);
                System.out.println("Item Price's value: " + tfItemPriceValue);
                System.out.println("Item Count's value: " + tfItemCountValue);

                //DefaultTableModel invoiceItemsTableModel= (DefaultTableModel) tbl_invoiceItems.getModel();
                invoiceItemsModel = (DefaultTableModel)tbl_invoiceItems.getModel();
                rowCount= invoiceItemsModel.getRowCount();
                String invoiceNoSelected= tfInvoiceNoVal.getText();
                if(invoiceItemsModel != null){

                    rowCount ++;
                }
                System.out.println("Row count is : " + rowCount);
                invoiceNo = invoiceHeaderTable.getSelectedRow();
                String[] itemData = new String[]{invoiceNoSelected,
                                                 tfItemNameValue,
                                                 tfItemPriceValue,
                                                 tfItemCountValue,
                                                 String.valueOf(Double.parseDouble(tfItemPriceValue) * Integer.parseInt(tfItemCountValue))};

                System.out.println("The Invoice Items model is " + invoiceItemsModel);
                invoiceItemsModel.addRow(itemData);
                tbl_invoiceItems.setModel(invoiceItemsModel);
                double invoiceTotal=0;

                //Update Invoice Header table with invoice table
                for(int row = 0; row< tbl_invoiceItems.getRowCount(); row++){
                    invoiceTotal+=Double.parseDouble(tbl_invoiceItems.getValueAt(row,4).toString());
                }

                invoiceHeaderTable.setValueAt(invoiceTotal,invoiceNo,3 );
                tfInvTotalVal.setText(String.valueOf(invoiceTotal));
                tbl_invoiceItems.setModel(invoiceItemsModel);


                break;
           /* case JOptionPane.CANCEL_OPTION:
                JOptionPane.*/
        }
    }



    /**
     * Invoked when an action occurs.
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        FileOperations operations = new FileOperations();


        switch(e.getActionCommand())
        {
            case "Load":
                ArrayList<InvoiceHeader> invoiceHeadersArrListToLoad = null;
                JFileChooser fc = new JFileChooser();
                int resultFile1_InvoiceHeader = fc.showOpenDialog(this);
                if(resultFile1_InvoiceHeader == JFileChooser.APPROVE_OPTION) {
                    File selectedFile_InvoiceHeader = fc.getSelectedFile();

                    JFileChooser fc2= new JFileChooser();
                    int resultFile2_Items = fc2.showOpenDialog(this);
                    if(resultFile1_InvoiceHeader == JFileChooser.APPROVE_OPTION) {
                        selectedFile_Items = fc2.getSelectedFile();
                    }
                    try
                    {
                        invoiceHeadersArrListToLoad = operations.readFile(selectedFile_InvoiceHeader);
                    }
                        catch (FileNotFoundException ex)
                        {
                            JOptionPane.showMessageDialog(this, "The system can not find the file specified", "Load File Error",JOptionPane.ERROR_MESSAGE);
                        }
                        catch(IllegalFormatException ex)
                        {
                            JOptionPane.showMessageDialog(this, "Wrong file format, File with CSV extension expected", "Load File Error",JOptionPane.ERROR_MESSAGE);
                        }
                    catch(DateTimeParseException ex){
                        JOptionPane.showMessageDialog(this, "Invoice Date is in a wrong format, format should be dd-MM-yyyy", "Load File Error",JOptionPane.ERROR_MESSAGE);
                    }
                    catch (IllegalArgumentException ex){
                        JOptionPane.showMessageDialog(null, "Wrong file format, expected format is csv","Save File Error",JOptionPane.ERROR_MESSAGE);
                    }
                    //Load the read inovice headers in invoice header table
                    if(invoiceHeadersArrListToLoad != null && invoiceHeadersArrListToLoad.size()>0){
                       for(int invHead=0; invHead< invoiceHeadersArrListToLoad.size(); invHead++ ){
                           invoiceHeaderModel.addRow(new String[]{String.valueOf(invoiceHeadersArrListToLoad.get(invHead).getInvoiceNum()),
                                                     invoiceHeadersArrListToLoad.get(invHead).getInvoiceDate(),
                                                     invoiceHeadersArrListToLoad.get(invHead).getCustomerName() });
                       }

                    }
                }

                break;

            case "Save":
                ArrayList<InvoiceHeader> invoiceHeadersArrToWrite  = new ArrayList<>();
                ArrayList<InvoiceLine> invoiceItemsArrToWrite  = new ArrayList<>();
                JFileChooser fc_InvoiceHeader = new JFileChooser();
                JFileChooser fc_InvoiceItems = new JFileChooser();
                File selectedFile_InvoiceHeader = null;
                File selectedFile_InvoiceItems = null;
                ArrayList<InvoiceHeader> currentInvoicesInFile=null;
                ArrayList<InvoiceLine> currentItemsInFile = null;

                //Select file for InvoiceHeader
                int selectFile_InvoiceHeader = fc_InvoiceHeader.showOpenDialog(this);
                if(selectFile_InvoiceHeader == JFileChooser.APPROVE_OPTION) {
                     selectedFile_InvoiceHeader = fc_InvoiceHeader.getSelectedFile();

                     //Select file for InvoiceLines
                    int selectFile_InvoiceItems = fc_InvoiceItems.showOpenDialog(this);
                    if(selectFile_InvoiceItems == JFileChooser.APPROVE_OPTION) {
                         selectedFile_InvoiceItems = fc_InvoiceItems.getSelectedFile();
                        }
                }

                //Check if the current displayed records of Invoice Header table are existing in the file to which they will be stored
                try {
                      currentInvoicesInFile = operations.readFile(selectedFile_InvoiceHeader);
                } catch (FileNotFoundException ex)
                {
                    JOptionPane.showMessageDialog(this, "The system can not find the file specified", "Load File Error",JOptionPane.ERROR_MESSAGE);
                }
                Vector dataRowInvoiceHeaders =  invoiceHeaderModel.getDataVector();

                InvoiceHeader dataRowInvoiceHeader;
                //Prepare Invoice Headers data to be saved -- Start //
                for(int index =0; index<invoiceHeaderTable.getRowCount(); index++){
                    dataRowInvoiceHeader = new InvoiceHeader(
                            Integer.parseInt(invoiceHeaderTable.getValueAt(index,0).toString()),
                            invoiceHeaderTable.getValueAt(index,1).toString(),
                            invoiceHeaderTable.getValueAt(index,2).toString()) ;
                    if(currentInvoicesInFile!= null && currentInvoicesInFile.size()> 0){
                        //Compare each record of the Invoice Header before saving into file
                        if(!currentInvoicesInFile.contains(dataRowInvoiceHeader))
                        {
                            invoiceHeadersArrToWrite.add(new InvoiceHeader(
                                                        dataRowInvoiceHeader.getInvoiceNum(),
                                                        dataRowInvoiceHeader.getInvoiceDate(),
                                                        dataRowInvoiceHeader.getCustomerName()));


                        }
                    }
                    else{
                        invoiceHeadersArrToWrite.add(
                                new InvoiceHeader(
                                        dataRowInvoiceHeader.getInvoiceNum(),
                                        dataRowInvoiceHeader.getInvoiceDate(),
                                        dataRowInvoiceHeader.getCustomerName())
                        );
                    }

                }
                //Prepare Invoice Headers data to be saved -- End //

                //Prepare Invoice Items data to be saved -- Start //

                InvoiceLine dataRowInvoiceItem;
                for(int index =0; index<tbl_invoiceItems.getRowCount(); index++){

                    dataRowInvoiceItem = new InvoiceLine
                            (
                                Integer.parseInt(tbl_invoiceItems.getValueAt(index,0).toString()),
                                tbl_invoiceItems.getValueAt(index,1).toString(),
                                Double.parseDouble(tbl_invoiceItems.getValueAt(index,2).toString()),
                                Integer.parseInt(tbl_invoiceItems.getValueAt(index,3).toString())
                            ) ;

                        invoiceItemsArrToWrite.add(dataRowInvoiceItem);
                }
                //Prepare Invoice Items data to be saved -- End //

               boolean writeStatus = operations.writeFile(invoiceHeadersArrToWrite, invoiceItemsArrToWrite, selectedFile_InvoiceHeader,selectedFile_InvoiceItems  );
                if(writeStatus== true){
                    JOptionPane.showMessageDialog(this, "Invoices data have been successfully saved", "Save Data",JOptionPane.INFORMATION_MESSAGE);

                }
                else{
                    JOptionPane.showMessageDialog(this, "Invoices data failed to save", "Save Data",JOptionPane.ERROR_MESSAGE);
                }
                break;

            case "NewInvoice":
                try {
                    AddNewInvoice();
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }
                break;

            case "SaveItem":
                AddNewItem(invNo);
                break;

            case "DeleteInvoice":
                if(invoiceHeaderTable.getSelectedRow() != -1) {
                    // remove selected row from the model

                    int result =JOptionPane.showConfirmDialog(this, "Are you sure to delete this invoice?", "Delete Invoice", JOptionPane.OK_CANCEL_OPTION);
                    if(result == JOptionPane.OK_OPTION){
                        try {
                            int numRows = invoiceHeaderTable.getSelectedRows().length;
                            for(int i=0; i<numRows ; i++ ) {
                                //Consider that the table's model has different indexing compared to the invoice header table, getSelectedRow()-1
                                invoiceHeaderModel.removeRow(invoiceHeaderTable.getSelectedRow()-1);
                                invoiceHeaderTable.setModel(invoiceHeaderModel);
                                invoiceHeaderTable.updateUI();
                            }
                            JOptionPane.showMessageDialog(null, "Selected row deleted successfully");


                        } catch (Exception ex){
                            JOptionPane.showMessageDialog(null, ex.getMessage());
                        }

                    }
                    else if(result == JOptionPane.CANCEL_OPTION){
                        System.exit(0);
                    }

                }
                else{
                    if(invoiceHeaderTable.getRowCount() == 0){
                        JOptionPane.showMessageDialog(this, "This table is empty");
                    }
                    else{
                        JOptionPane.showMessageDialog(this, "Please select single row to be deleted");
                    }
                }

                break;

            case "DeleteItem":
                if(tbl_invoiceItems.getSelectedRow() != 1) {
                    int result =JOptionPane.showConfirmDialog(this, "Are you sure to delete this item?", "Delete Item", JOptionPane.OK_CANCEL_OPTION);
                    if(result == JOptionPane.OK_OPTION){
                        try {
                            int[] selectedRows = tbl_invoiceItems.getSelectedRows();
                            if (selectedRows.length > 0) {
                                for (int i = selectedRows.length - 1; i >= 0; i--) {
                                    invoiceItemsModel.removeRow(selectedRows[i]);
                                }
                            }
                                JOptionPane.showMessageDialog(null, "Selected row deleted successfully");

                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Exception raised while trying to remove invoice record "+ ex.getMessage());
                        }
                    } else if (result == JOptionPane.CANCEL_OPTION) {
                        System.exit(0);
                    } else {
                        if (tbl_invoiceItems.getRowCount() == 0) {
                            JOptionPane.showMessageDialog(this, "This table is empty");
                        } else {
                            JOptionPane.showMessageDialog(this, "Please select single row to be deleted");
                        }
                    }
                }
                break;

        }
    }
}
