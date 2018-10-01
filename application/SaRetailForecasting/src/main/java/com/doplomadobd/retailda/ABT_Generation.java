/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doplomadobd.retailda;

import com.diplomadobd.dataintegration.io.core.TextFileHelper;
import java.io.BufferedWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import com.diplomadobd.dao.FandeliDAO;
import com.diplomadobd.dto.SplineAccumRule;

/**
 * Generates the ABT data for both the cumulative and the
 *
 * @author mario
 */
public class ABT_Generation {

    //The number of the week until which will be added the extraWeeks
    private final int firstWeeks = 4;
    //The extra weeks to consider so that there isn't too fiew data.
    private final int extraWeeks = 4;

    private int cumRule;
    private int lag;
    private int totalWeeks;
    private int weeksAhead;
    private Connection conn;

    /**
     * Indicates the Id of the rule group to build the ABT
     *
     * @param cumRule The id of the cumulative rule
     * @param conn
     */
    public ABT_Generation(int cumRule, Connection conn) {
        this.cumRule = cumRule;
        this.conn = conn;
        this.setCumRule();
    }

    /**
     * Gets the cumulative rule from the Database and sets the local variables
     *
     * @param conn
     */
    private void setCumRule() {
        String theQuery = "SELECT a.weeks, a.lag, b.forecastid, b.wahead\n"
                + "FROM fandeli_cumrule a INNER JOIN fandeli_forecast b ON (a.forecastid = b.forecastid)\n"
                + "WHERE idcum = ?";
        boolean flag = false;
        try {
            PreparedStatement ps = this.conn.prepareStatement(theQuery);
            ps.setInt(1, this.cumRule);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                this.lag = rs.getInt("lag");
                this.totalWeeks = rs.getInt("weeks");
                this.weeksAhead = rs.getInt("wahead");
                flag = true;
            }
            if (!flag) {
                throw new IllegalStateException("The idCum: " + this.cumRule + " does not exist.");
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Calculates the least cumulative value from the time series corresponding
     * to the xvalue. It also uses the constants firstWeeks and extraWeeks
     *
     * @param lag
     * @param totalWeeks
     * @param xValue
     * @return
     */
    private double[] getLeastCumDemandAndCumSplines(int lag, int totalWeeks, int xValue, String sku) {
        double[] leastCumDemandAndSplines;
        //First cumpute the index from which the accumulation will start
        int leastIndex = xValue - lag;
        int modulus = leastIndex % totalWeeks;
        int accumulationIndex = leastIndex - modulus;
        if (modulus < firstWeeks) {
            accumulationIndex -= firstWeeks;
        }
        //Gets the accumulated demand
        leastCumDemandAndSplines = FandeliDAO.getLeastCumDemandAndCumSplines(sku, accumulationIndex, leastIndex,
                xValue, (xValue + this.weeksAhead), conn);
        return leastCumDemandAndSplines;
    }

    /**
     * Builds the ABT tables for the corresponding SKU
     *
     * @param sku
     */
    private void buildAbtTables(String sku) {
        //Get the time series
        Object[] theRecords = FandeliDAO.getTimeSeriesForABT(sku, this.conn);
        int startIndex = this.lag;
        int endIndex = theRecords.length - this.weeksAhead;
        double[] cumRecord;
        double[] varCumRecord;
        try {
            //Cycle that builds the cumulative and the cumulative variation abt
            for (int i = startIndex; i < endIndex; i++) {

                //Arrays to store the abt data
                cumRecord = new double[this.lag + 2];
                varCumRecord = new double[cumRecord.length - 1];
                int xValue = (int) ((Object[]) theRecords[i])[0];
                //Gets the least cumulative demand
                double currentCumDem;
                double cumSplines;
                double cumArray[]
                        = this.getLeastCumDemandAndCumSplines(this.lag, this.totalWeeks, xValue, sku);
                currentCumDem = cumArray[0];
                cumSplines = cumArray[1];
                //If it is zero the should skip this row
                if (currentCumDem == 0.0) {
                    throw new ArithmeticException("This row should be avoided because will produce a division by zero.");
                }
                //Set the demand lag 
                for (int j = 0; j < this.lag; j++) {
                    cumRecord[j] = currentCumDem;
                    currentCumDem += (double) ((Object[]) theRecords[i - (lag - 1) + j])[1];
                }
                //Set the demand for x
                cumRecord[this.lag] = currentCumDem;
                //Set the demand for the target
                for (int j = i + 1; j < i + 1 + this.weeksAhead; j++) {
                    currentCumDem += (double) ((Object[]) theRecords[j])[1];
                }
                cumRecord[this.lag + 1] = currentCumDem;
                //Now build the variation
                for (int j = 0; j < varCumRecord.length; j++) {
                    varCumRecord[j] = cumRecord[j + 1] / cumRecord[j];
                }
                //Now inserts the rows into the abt tables
                FandeliDAO.insertAbtRows(cumRecord, varCumRecord, xValue,
                        lag, sku, cumRule, cumSplines, conn);
            }
        } catch (ArithmeticException e) {
            //Skips inserting the row with a zero cumulative  demand
        }
    }
    /**
     * Helps to get the current accumulated demand
     * @param x
     * @param cumValues
     * @param values
     * @param currentIndex
     * @return 
     */
    private double getCumValue(HashMap<Integer,Double> cumValues,
            List<Object[]> values, int currentIndex){
        
        double cumValue = -1.0;
        Double prevCumValue = -1.0;
        double actualValue = -1;
        try{
        actualValue = (Double)values.get(currentIndex)[4];
        }catch(Exception ex){
            throw new IllegalStateException(ex);
        }
        int x = (Integer)values.get(currentIndex)[3];
        //Check if the map is empty
        if(cumValues.isEmpty()){
            cumValue = actualValue;
        }else{
            prevCumValue = cumValues.get(x-1);
            if(prevCumValue!= null){
                cumValue = prevCumValue + actualValue;
            }else{
                cumValue = getCumValue(cumValues,values, currentIndex-1)+actualValue;
                
            }
        }
        cumValues.put(x,cumValue);
        return cumValue;
    }

    /**
     * Builds the ABT table, when the augmented table has already the synthetic
     * data for the corresponding SKU
     *
     * @param sku
     */
    private void buildAbtTable(String sku, int idCumRule) {
        //
        SplineAccumRule rule = FandeliDAO.getRule(idCumRule, conn);
        //Get the time series
        List<Object[]> theRecords = FandeliDAO.getSyntheticData(sku, 0, 0, conn);
        int startIndex = this.lag;
        int endIndex = theRecords.size() - this.weeksAhead;
        double[] cumRecord;
        double[] cumRecordVariation;
        //Map that stores the cumulative values
        HashMap<Integer, Double> cumStore = new HashMap();
        //Cycle that builds the cumulative variation abt
        for (int i = startIndex; i < endIndex; i++) {
            Object[] row = theRecords.get(i);
            int xValue = (int) row[3];            
            //Verify if should reset the accumulator
            //Now verifies if if we should reset the accumulator
            int modulus = (xValue - this.lag + 1) % rule.getTotalWeeks();
            if (modulus == 0) {
                //Resets the accumulatos
                cumStore.clear();
            }
            try {
                //Array to store the abt data. Includes the past, present and future elements
                double cumDemand;
                cumRecord = new double[this.lag + 2];     
                int currenValueIndex = i - this.lag;
                int cumDemandIndex = -1;
                for (int j = currenValueIndex; j <= i; j++) {  
                    cumDemandIndex = j - currenValueIndex;
                    cumDemand = getCumValue(cumStore,theRecords,j);
                    cumRecord[cumDemandIndex]= cumDemand;                    
                }
                //Now accumulates the weeks ahead
                cumDemandIndex++;
                cumRecord[cumDemandIndex] = getCumValue(cumStore,theRecords,i+this.weeksAhead);
                //Now should compute the variation...
                cumRecordVariation = new double[cumRecord.length-1];
                for (int j = 0; j < cumRecordVariation.length; j++) {
                    //verify cero division
                    double denominator = cumRecord[j];
                    if(denominator == 0){
                        throw new ArithmeticException("prevent division by cero");
                    }
                    cumRecordVariation[j] = cumRecord[j+1]/denominator;                    
                }
                //Now inserts the rows into the abt tables
                FandeliDAO.insertAbtRows(cumRecord, cumRecordVariation, xValue,
                         sku, cumRule, conn);
            } catch (ArithmeticException e) {
                //Skips inserting the row with a zero cumulative  demand
            }
        }
    }
    /**
     * Builds the ABT table, when the augmented table has already the synthetic
     * data for the corresponding SKU
     *
     * @param sku
     * @param idCumRule
     * @param latestX
     * @return Two array of Objects[]: The first is the accumulated abt and the second is the variation abt
     */
    public List<List<double[]>> buildForecastAbtRows(String sku, int idCumRule, int latestX) {
        //
        List<double[]> cumRecords = new LinkedList();
        List<double[]> cumVarRecords = new LinkedList();
        List<List<double[]>> results = new LinkedList();
        results.add(cumRecords);
        results.add(cumVarRecords);
        
        SplineAccumRule rule = FandeliDAO.getRule(idCumRule, conn);
        //Get the time series
        List<Object[]> theRecords = FandeliDAO.getSyntheticData(sku, 0, 0, conn);        
        int startIndex =  latestX + 1 -(Integer)theRecords.get(0)[3] ;
        int endIndex = theRecords.size();
        double[] cumRecord;
        double[] cumRecordVariation;
        //Map that stores the cumulative values
        HashMap<Integer, Double> cumStore = new HashMap();
        //Cycle that builds the cumulative variation abt
        for (int i = startIndex; i < endIndex; i++) {
            Object[] row = theRecords.get(i);
            int xValue = (int) row[3];            
            //Verify if should reset the accumulator
            //Now verifies if if we should reset the accumulator
            int modulus = (xValue - this.lag + 1) % rule.getTotalWeeks();
            int currenValueIndex = i - this.lag;
            if (modulus == 0) {
                //Resets the accumulatos
                cumStore.clear();
            } if(cumStore.isEmpty()){
                //Compute the first element of the series
                getCumValue(cumStore,theRecords,currenValueIndex-modulus);
            }
            try {
                //Array to store the abt data. Includes the past, present and future elements
                double cumDemand;
                cumRecord = new double[this.lag + 1];     
                int cumDemandIndex;
                for (int j = currenValueIndex; j <= i; j++) {  
                    cumDemandIndex = j - currenValueIndex;
                    cumDemand = getCumValue(cumStore,theRecords,j);
                    cumRecord[cumDemandIndex]= cumDemand;                    
                }
                
                //Now should compute the variation...
                cumRecordVariation = new double[cumRecord.length];
                for (int j = 0; j < cumRecordVariation.length-1; j++) {
                    //verify cero division
                    double denominator = cumRecord[j];
                    if(denominator == 0){
                        throw new ArithmeticException("prevent division by cero");
                    }
                    cumRecordVariation[j] = cumRecord[j+1]/denominator;                    
                }
                //Now return add the rows to the lists
                cumRecords.add(cumRecord);
                cumVarRecords.add(cumRecordVariation);
                
            } catch (ArithmeticException e) {
                //Skips inserting the row with a zero cumulative  demand
            }
        }
        return results;
    }

    /**
     * Build both the cumulative ABT and the variation cumulative ABT
     */
    public void buildAbtTables(int idCum) {
        List<String> theSkus;
        //Firstl query all the series
        theSkus = FandeliDAO.getSkus(this.conn);
        //For each sku, build their correspoindig ABT tables
        for (String theSku : theSkus) {
            this.buildAbtTable(theSku,idCum);        }
    }
    /**
     * Given a cumRule, print the training ABT for all the SKUs
     * @param path
     * @param cumRule
     * @param conn 
     */
    public void printABTTrainingSet(String path, int cumRule) {
        //Select the skus
        List<String> theSkus  = FandeliDAO.getSkus(this.conn);
        //Select the abt data for each sku
        for (String theSku : theSkus) {
            String trainingFileName = path+theSku +"-"+cumRule+".csv";
            BufferedWriter bwTSet = TextFileHelper.getBufferedWriter(trainingFileName);
            FandeliDAO.printTSet(theSku, cumRule, bwTSet, this.conn);
            
        }
    }

}
