/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doplomadobd.retailda;

import com.diplomadobd.dao.Dao;
import com.diplomadobd.dataintegration.io.core.TextFileHelper;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author mario
 */
public class DataFormater {

    public static void main(String[] args) {
        Object o = new DataFormater();
    }
    private final String csvDelimiter;
    private int idColumn;


    public DataFormater() {
        csvDelimiter = ",";
        String fileName = "50SKUs_raw.csv";//"ampliado50-2016.csv";//"datosfandeli01.csv";
        String outFolder = "../datasets/";
        BufferedReader br = TextFileHelper.getBufferedReader("../../datasets/" + fileName);
        BufferedWriter bw = TextFileHelper.getBufferedWriter(outFolder + "Vertical_" + fileName);
        try {
            //Read the input file

            //skip the first lines to avoid headers: Código/Año/Enero/Febrero/.../Diciembre
            int skipLines = 2;
            for (int i = 0; i < skipLines; i++) {
                br.readLine();
            }
            //Section for the data
            idColumn = 0;
            //Define the size of the record 
            //sku, year, month, demand
            bw.write("sku, year, month, demand");
            bw.newLine();
            String line;
            List<Object[]> totalTable = new  LinkedList<>();
            //Reads the next line            
            while (br.ready()) {
                line = br.readLine();
                String[] theRawRow = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                List<Object[]> theRecords = this.getRow(theRawRow);
                totalTable.addAll(theRecords);
                printTheRecords(theRecords, bw);
            }
            DataAsFunction da = new DataAsFunction();
            da.generateOriginalTable(totalTable, new Dao().getDbConnection());

        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            try {
                br.close();
            } catch (Exception ex) {
                //Do nothing
            }
            try{
                bw.close();
            }catch(Exception ex){
                //Do nothing
            }
        }
    }

    private List<Object[]> getRow(String[] theRawRow) {
        List<Object[]> records = new ArrayList();
        try {
            String theId = String.valueOf(theRawRow[idColumn]);
            String theYear = String.valueOf(theRawRow[1]);
            //Traverse all the other columns
            for (int i = 2; i < theRawRow.length; i++) {
                int isMissingValue = 0;
                Object[] theRow = new Object[4];
                //Set the fixed Values
                theRow[0] = theId;
                theRow[1] = theYear;
                theRow[2] = i-1;//month
                String demanda = theRawRow[i].replace(",", "").replace("\"", "").trim();
                try{
                    int intValue = Integer.parseInt(demanda);
                    if(intValue<0){
                        demanda = String.valueOf(intValue);
                    }
                }catch(Exception e){
                    demanda = "0";
                }
                theRow[3] = demanda;
                //id, year, month, demand
                records.add(theRow);
                
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return records;
    }

    private void printTheRecords(List<Object[]> theRecords, BufferedWriter bw) {
        try {
            for (Object[] theRecord : theRecords) {
                StringBuilder sb = new StringBuilder();
                for (Object aValue : theRecord) {
                    sb.append(aValue);
                    sb.append(",");
                }
                String aRow = sb.toString();
                bw.write(aRow.substring(0, aRow.length() - 1));
                bw.newLine();
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

    }

}
