package controller;

import model.InvoiceHeader;
import model.InvoiceLine;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
                               LocalDate.parse(invHeaderItems[1], df),
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

    public void writeFile(ArrayList<InvoiceHeader> arrayListInvoiceHeader){

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
