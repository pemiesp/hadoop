/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diplomadobd.dataintegration.io.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.TreeMap;

/**
 *
 * @author mario
 */
public class CsvUnifier {
    private String sourcePath;
    private String outputCsvPath;
    /**
     * Constructor
     * @param sourcePath
     * @param outputCsvPath 
     */
    private CsvUnifier(String sourcePath, String outputCsvPath) { 
        this.sourcePath = sourcePath;
        this.outputCsvPath = outputCsvPath;
    }
    /**
     * Return the next valid date from the given as parameter
     * @param initialDate
     * @return 
     */
    private Date getNextLaborDate(Date initialDate){
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(initialDate);
        gc.add(Calendar.DAY_OF_MONTH, 1);
        if(gc.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
            //Adds until get day of the week
            gc.add(Calendar.DAY_OF_MONTH, 2);
        }
        Date newDate = gc.getTime();
        return newDate;
    }
    /**
     * Loads the data set and imputes missing values
     * @param theCurrentCsv
     * @return 
     */
    private TreeMap<Date,String> imputeMissingValues(String theCurrentCsv) {  
        String stockSimbol = new File(theCurrentCsv).getName().split("_")[0];
        BufferedReader br = null;
        TreeMap<Date,String> theOriginalDataset = new TreeMap();
        TreeMap<Date,String> theImputedDataset = new TreeMap();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        try{
            br = TextFileHelper.getBufferedReader(theCurrentCsv);
            //First load the data into the map
            //Reads the header
            br.readLine();
            while(br.ready()){
                String line = br.readLine();
                int comaIndex = line.indexOf(",");
                String dateAsString = line.substring(0,comaIndex);
                Date theDate = sdf.parse(dateAsString);
                String theRest = line.substring(comaIndex+1,line.length());
                theOriginalDataset.put(theDate, theRest);
            }
            //Now we have loaded the data, let us impute the missing values
            //Prepares the first value
            Iterator<Date> datesIterator = theOriginalDataset.keySet().iterator();
            Date currDate = null;
            try{
                currDate= datesIterator.next();
            }catch(Exception ex){
                throw new IllegalStateException(ex);
            }
            String currValue = theOriginalDataset.get(currDate);
            String newValue = stockSimbol+","+sdf.format(currDate)+","+currValue;
            theImputedDataset.put(currDate,newValue);
            while(datesIterator.hasNext()){
                Date nextDate = datesIterator.next();
                //First we get the next expected date
                currDate = this.getNextLaborDate(currDate);
                while(currDate.compareTo(nextDate)<0){
                    newValue = stockSimbol+","+sdf.format(currDate)+","+currValue;
                    theImputedDataset.put(currDate,newValue);  
                    currDate = this.getNextLaborDate(currDate);
                }
                currValue = theOriginalDataset.get(currDate);
                newValue = stockSimbol+","+sdf.format(currDate)+","+currValue;
                theImputedDataset.put(currDate,newValue);                
            }
        }catch(IllegalStateException e){
            throw e;            
        }catch(Exception e){
            throw new IllegalStateException(e);
        }finally{
            try{
                br.close();
            }catch(Exception ex){//Do nothing                
            }                
        }
        return theImputedDataset;
    }
    /**
     * Prints the data set as csv
     * @param bw
     * @param theDataset 
     */
    private void printTheDataset(BufferedWriter bw, TreeMap<Date,String> theDataset) {
        for (String next : theDataset.values()) {
            try{
                bw.write(next);
                bw.newLine();
            }catch(Exception e){
                throw new IllegalStateException(e);
            }
        }
    }
    /**
     * Executes the unification of all csv files into one, and also imputes values
     * for missing dates, between the higgest and shortest date.
     */
    public void executeUnification(){
        //Prepare OutputFile
        BufferedWriter bw = TextFileHelper.getBufferedWriter(this.outputCsvPath);
        //Gets the list of files to unify
        File theSourceDir = new File(this.sourcePath);
        File [] theCsvsToUnify = theSourceDir.listFiles();
        try{
            //Set header
            String header = "Ssimbol,Date,Open,High,Low,Close,Volume,AdjClose";
            bw.write(header);
            bw.newLine();
            //Unifies all files from directory
            for (int i = 0; i < theCsvsToUnify.length; i++) {
                String theCurrentCsv = theCsvsToUnify[i].getAbsolutePath();
                TreeMap<Date,String> theImputedDataset = imputeMissingValues(theCurrentCsv);
                printTheDataset(bw, theImputedDataset);                
            }
            
        }catch (IllegalStateException e){
            throw e;
        }catch (Exception e){
            throw new IllegalStateException(e);
        }finally{
            try{
                bw.close();
            }catch (Exception e){//Do nothing                
            }
        }
        
    }

    public static void main(String[] args) {
        String sourcePath = "../data/datasets/histStock";
        String outputCsvPath = "../data/datasets/AllHistStock.csv";
        CsvUnifier csvU= new CsvUnifier(sourcePath, outputCsvPath);
        csvU.executeUnification();
              
    }


    
}

