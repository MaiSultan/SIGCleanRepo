package model;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InvoiceHeaderModel extends DefaultTableModel {

    private String[] columnNames =
            {
                    "No.", "Date","Customer","Total"
            };

    private List<InvoiceHeader> invoiceHeaderList;
    public InvoiceHeaderModel()
    {
        invoiceHeaderList = new ArrayList<InvoiceHeader>();
    }
    public InvoiceHeaderModel(List<InvoiceHeader> invoices)
    {
        this.invoiceHeaderList = invoices;
    }

    /**
     * Returns the number of rows in the model. A
     * <code>JTable</code> uses this method to determine how many rows it
     * should display.  This method should be quick, as it
     * is called frequently during rendering.
     *
     * @return the number of rows in the model
     * @see #getColumnCount
     */
    @Override
    public int getRowCount() {
        return 0;
    }

    /**
     * Returns the number of columns in the model. A
     * <code>JTable</code> uses this method to determine how many columns it
     * should create and display by default.
     *
     * @return the number of columns in the model
     * @see #getRowCount
     */
    @Override
    public int getColumnCount() {
        return 0;
    }

    /**
     * Returns the value for the cell at <code>columnIndex</code> and
     * <code>rowIndex</code>.
     *
     * @param rowIndex    the row whose value is to be queried
     * @param columnIndex the column whose value is to be queried
     * @return the value Object at the specified cell
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        InvoiceHeader invoiceHeader = getInvoice(rowIndex);

        switch (columnIndex)
        {
            case 0: return invoiceHeader.getInvoiceNum();
            case 1: return invoiceHeader.getInvoiceDate();
            case 2: return invoiceHeader.getCustomerName();
            default: return null;
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        //all cells false
        return false;
    }

    @Override
    public void setValueAt(Object value, int row, int column)
    {
        InvoiceHeader invoiceHeader = getInvoice(row);

        switch (column)
        {
            case 1: invoiceHeader.setInvoiceDate((String)value); break;
            case 2: invoiceHeader.setCustomerName((String)value); break;
        }

        fireTableCellUpdated(row, column);
    }

    public InvoiceHeader getInvoice(int row)
    {
        return invoiceHeaderList.get( row );
    }

    public void addInvoice(InvoiceHeader invoiceHeader)
    {
        System.out.println(invoiceHeader);
        insertInvoice(getRowCount(), invoiceHeader);
        System.out.println((getRowCount()));
    }

    public void insertInvoice(int row, InvoiceHeader invoiceHeader)
    {
        invoiceHeaderList.add(row, invoiceHeader);
        fireTableRowsInserted(row, row);
    }

    public void removeInvoice(int row)
    {
        invoiceHeaderList.remove(row);
        fireTableRowsDeleted(row, row);
    }


}
