package controller;

import com.opencsv.CSVWriter;
import model.InvoiceHeader;
import model.InvoiceLine;

import javax.swing.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.lang.Exception;

public class FileOperations implements Serializable {

   public ArrayList<InvoiceHeader> readFile(File  file) throws FileNotFoundException {

       BufferedReader bufReader = null;
       ArrayList<InvoiceHeader> invoiceHeadersArrayList = null;
       String filePath = file.getPath();

       try{
           FileReader fr = new FileReader(filePath);
           bufReader = new BufferedReader(fr);
           invoiceHeadersArrayList = new ArrayList<>();
           String invHeaderLine = ""; ;


           while (( invHeaderLine = bufReader.readLine()) != null) {
               String[] invHeaderItems = invHeaderLine.split(",");
               System.out.println("Invoice Header Record: " + invHeaderLine);

               DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy");
               invoiceHeadersArrayList.add(
                       new InvoiceHeader(Integer.parseInt(invHeaderItems[0]),
                               //LocalDate.parse(invHeaderItems[1], df),
                               invHeaderItems[1],
                               invHeaderItems[2]));
           }

           //Read more: https://www.java67.com/2016/07/how-to-read-text-file-into-arraylist-in-java.html#ixzz7npXJnmjp

       }
       catch(FileNotFoundException e){
e.printStackTrace();
       } catch (IOException e) {
          e.printStackTrace();
       } finally{
           try{
               bufReader.close();
           }catch (IOException e) {
               throw new RuntimeException(e);
           }

       }
        System.out.println(invoiceHeadersArrayList);
       return invoiceHeadersArrayList;
    }

    public boolean writeFile(ArrayList<InvoiceHeader> arrInvoiceHeaders,
                          ArrayList<InvoiceLine> arrInvoiceLines,
                          File invoiceHeaderFile,
                          File invoiceItemsFile){

        String filePath_InvoiceHeader = invoiceHeaderFile.getPath();
        File file_InvoiceHeader = new File(filePath_InvoiceHeader);
        String filePath_InvoiceItem = invoiceItemsFile.getPath();
        File file_InvoiceItem = new File(filePath_InvoiceItem);
        boolean writeStatus = false;
        try {
            // create FileWriter object with file as parameter
            FileWriter fileWriter_InvoiceHeader = new FileWriter(file_InvoiceHeader);
            FileWriter fileWriter_InvoiceItem = new FileWriter(file_InvoiceItem, true);
            //BufferedWriter bw_InvoiceItem = new BufferedWriter(fileWriter_InvoiceItem);

        // create CSVWriter object filewriter object as parameter
            CSVWriter writer_InvoiceHeader = new CSVWriter(fileWriter_InvoiceHeader);
            CSVWriter writer_InvoiceItem = new CSVWriter(fileWriter_InvoiceItem);



            // adding Invoice basic data data to InvoiceHeader.csv
            System.out.println("The new invoice header to be written are: " + arrInvoiceHeaders);
            String[] header = null;
            for(int invRow=0; invRow<arrInvoiceHeaders.size(); invRow++){
                header = new String[]{
                        String.valueOf(arrInvoiceHeaders.get(invRow).getInvoiceNum()),
                        arrInvoiceHeaders.get(invRow).getInvoiceDate(),
                        arrInvoiceHeaders.get(invRow).getCustomerName()
                } ;
                writer_InvoiceHeader.writeNext(header);
            }
            System.out.println("Invoice headers' data written successfully are: " + arrInvoiceHeaders);
            // closing writer connection
            writer_InvoiceHeader.close();

            // adding invoice item to InvoiceLine.csv file
            System.out.println("The new invoice header to be written are: " + arrInvoiceLines);
            String[] Item = null;
            for(int itemRow=0; itemRow<arrInvoiceLines.size(); itemRow++){
                Item = new String[]{
                        String.valueOf(arrInvoiceLines.get(itemRow).getInvoiceNum()),
                        arrInvoiceLines.get(itemRow).getItemName(),
                        String.valueOf(arrInvoiceLines.get(itemRow).getItemPrice()),
                        String.valueOf(arrInvoiceLines.get(itemRow).getCount())
                } ;
                writer_InvoiceItem.writeNext(Item);
                //writer_InvoiceItem.writeAll(Item, true);
            }
            System.out.println("Invoice lines' data written successfully are: " + arrInvoiceLines);
            // closing writer connection
            writer_InvoiceItem.close();
            writeStatus = true;
        }

        catch(FileNotFoundException e){
            JOptionPane.showMessageDialog(null, "The selected file is not found or inaccessible","Save File Error",JOptionPane.ERROR_MESSAGE);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            JOptionPane.showMessageDialog(null, "The path to the selected file is not found","Save File Error",JOptionPane.ERROR_MESSAGE);
        }

        catch (IllegalArgumentException e){
            JOptionPane.showMessageDialog(null, "Wrong file format, expected format is csv","Save File Error",JOptionPane.ERROR_MESSAGE);
        }
        return writeStatus;
    }

  public ArrayList<InvoiceLine> loadInvoiceLinesByInvoiceNo(String invoiceNo, File invoiceLinesFile){
      BufferedReader bufReader = null;
      ArrayList<InvoiceLine> invoiceLinesArrayList = null;

      String filePath = invoiceLinesFile.getPath();

      try {
          FileReader fr = new FileReader(filePath);
          bufReader = new BufferedReader(fr);
          invoiceLinesArrayList = new ArrayList<>();
          String invLineRow = "";

          while ((invLineRow = bufReader.readLine()) != null) {
              String[] invLines = invLineRow.split(",");
              System.out.println("Invoice Line Record: " + invLineRow);

              invoiceLinesArrayList.add(
                     new InvoiceLine(Integer.parseInt(invLines[0]),
                                     invLines[1],
                             Double.parseDouble(invLines[2]),
                             Integer.parseInt(invLines[3])
                             )
              );
              System.out.println("All invoices lines are: " + invoiceLinesArrayList);
              Predicate<InvoiceLine> byInvoiceNo = InvoiceLine -> InvoiceLine.getInvoiceNum() == Integer.parseInt(invoiceNo);


              List<InvoiceLine> result = invoiceLinesArrayList.stream().filter(byInvoiceNo)
                      .collect(Collectors.toList());

              System.out.println("Items list for Invoice Number "+ Integer.parseInt(invoiceNo) + ", are : " + result);
              invoiceLinesArrayList = (ArrayList<InvoiceLine>) result;
          }
      }

       catch(Exception ex){

       }
       return invoiceLinesArrayList;
  }
}
