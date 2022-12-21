package view;

import controller.FileOperations;
import model.InvoiceHeader;
import model.InvoiceLine;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.concurrent.atomic.AtomicInteger;

public class InvoiceFrame extends JFrame implements ActionListener{
private JTable invoiceHeaderTable;

private Object[] cols_InvoiceHeader;//Array of objects for Invoice Header columns

    private Object[] cols_InvoiceItems;//Array of objects for Invoice Items columns
private ArrayList<InvoiceLine> invoiceLines = new ArrayList<InvoiceLine>();

private InvoiceHeader invoicesData;

private LayoutManager layoutManager;

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

        /////////////////////////////////////////

        JPanel panelLeft_InvoiceHeader = new JPanel();

        //Start filling invoice lines of the first invoice header
        //invoiceLines.add( new InvoiceLine("Blouse", 202.99, 2));
        //invoiceLines.add(new InvoiceLine("Jacket", 375.6, 1));

        invoicesData = new InvoiceHeader(1, LocalDate.of(2022, Month.DECEMBER, 02), "Ann Ezzat");

        invoicesData.setInvoiceLines(invoiceLines);

        //Invoice Header Columns names
        cols_InvoiceHeader = new Object[]{"No.", "Date","Customer","Total" };
        //Prepare JTable data as 2 dimensions object array
        data_InvoiceHeader= new Object[3][4];
                /*{
         {invoicesData.getInvoiceNum(),invoicesData.getInvoiceDate(), invoicesData.getCustomerName(), invoicesData.getInvoiceTotal() }};*/


        invoiceHeaderTable = new JTable(data_InvoiceHeader, cols_InvoiceHeader);




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
        JButton btnDeleteInovice = new JButton("Delete Invoice");

        //add the table to the frame
        //1. Adding Invoice Header Table component to Left panel
        panelLeft_InvoiceHeader.add(new JScrollPane( invoiceHeaderTable));

        panelLeft_InvoiceHeader.add(btnCreateInvoice);
        panelLeft_InvoiceHeader.add(btnDeleteInovice);

        add(panelLeft_InvoiceHeader);//1st Try




        JLabel invNoLbl = new JLabel("Invoice Number");
        JTextField tfInvoiceNoVal = new JTextField("1",30);
        tfInvoiceNoVal.setEditable(false);

        JLabel invDateLbl = new JLabel("Invoice Date");
        JTextField tfInvDate = new JTextField(30);

        JLabel custNameLbl = new JLabel("Customer Name");
        JTextField tfCustName = new JTextField(30);

        JLabel invTotalLbl = new JLabel("Invoice Total");
        JTextField tfInvTotalVal = new JTextField("200",30);
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
        JTable tbl_invoiceItems = new JTable(new Object[3][5], cols_InvoiceItems);

        panel_invoiceItems.add(new JScrollPane(tbl_invoiceItems));
        panel_invoiceFields.add(panel_invoiceItems);

        JButton btnSave = new JButton("Save");
        JButton btnDelete = new JButton("Delete");
        panel_invoiceFields.add(btnSave);
        panel_invoiceFields.add(btnDelete);

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

            //Call function to load invoice lines based on selected invoice no.
            FileOperations operations = new FileOperations();
            //Send Invoice Lines file for reading
            File invoiceLinesFile = new File("src/InvoiceLine.csv");
            System.out.println(invoiceLinesFile);
            ArrayList<InvoiceLine> invoiceLinesArr;
            invoiceLinesArr = operations.loadInvoiceLinesByInvoiceNo(selectedInvoiceNo[0], invoiceLinesFile);
            int invoiceTotal = 0;
            //Load the read inovice headers in invoice header table
            if(invoiceLinesArr != null && invoiceLinesArr.size()>0) {
                int invLine;
                int invNo = 0;

                for (invLine = 0; invLine < invoiceLinesArr.size(); invLine++) {
                    invNo = invoiceLinesArr.get(invLine).getInvoiceNum();
                    tbl_invoiceItems.setValueAt(invoiceLinesArr.get(invLine).getInvoiceNum(), invLine, 0);
                    tbl_invoiceItems.setValueAt(invoiceLinesArr.get(invLine).getItemName(), invLine, 1);
                    tbl_invoiceItems.setValueAt(invoiceLinesArr.get(invLine).getItemPrice(), invLine, 2);
                    tbl_invoiceItems.setValueAt(invoiceLinesArr.get(invLine).getCount(), invLine, 3);
                    tbl_invoiceItems.setValueAt(invoiceLinesArr.get(invLine).getItemTotal(), invLine, 4);
                    invoiceTotal+= (invoiceLinesArr.get(invLine)).getItemTotal();

                }
                System.out.println("The total invoice cost is : "+  invoiceTotal);

                //Set Invoice total price based of the summation of total invoice items inside Invoice Header Table
                invoiceHeaderTable.setValueAt(invoiceTotal,selectedRowIndex.get() , 3);

                //Set Invoice No and Invoice Total in non-editable text fields
                tfInvoiceNoVal.setText(String.valueOf(invNo));
                tfInvTotalVal.setText(String.valueOf(invoiceTotal));


            }
        });

    }


    /**
     * Invoked when an action occurs.
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        FileOperations operations = new FileOperations();
ArrayList<InvoiceHeader> invoiceHeadersArrListToWrite  = null;
ArrayList<InvoiceHeader> invoiceHeadersArrListToLoad = null;
        switch(e.getActionCommand())
        {
            case "Load":
                JFileChooser fc = new JFileChooser();
                int result = fc.showOpenDialog(this);
                if(result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fc.getSelectedFile();
                    try
                    {
                        invoiceHeadersArrListToLoad = operations.readFile(selectedFile);
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
                    //Load the read inovice headers in invoice header table
                    if(invoiceHeadersArrListToLoad != null && invoiceHeadersArrListToLoad.size()>0){
                       for(int invHead=0; invHead< invoiceHeadersArrListToLoad.size(); invHead++ ){
                           invoiceHeaderTable.setValueAt(invoiceHeadersArrListToLoad.get(invHead).getInvoiceNum(),invHead, 0);
                           invoiceHeaderTable.setValueAt(invoiceHeadersArrListToLoad.get(invHead).getInvoiceDate(),invHead, 1);
                           invoiceHeaderTable.setValueAt(invoiceHeadersArrListToLoad.get(invHead).getCustomerName(),invHead, 2);
                           //invoiceHeaderTable.setValueAt(invoiceHeadersArrListToLoad.get(invHead).getInvoiceTotal(),invHead, 3);
                       }
                    }
                }

                break;

            case "Save":
                operations.writeFile(invoiceHeadersArrListToWrite);
                break;



        }
    }
}
