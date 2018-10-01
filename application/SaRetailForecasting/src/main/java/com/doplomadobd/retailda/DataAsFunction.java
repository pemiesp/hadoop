/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doplomadobd.retailda;

import java.awt.BorderLayout;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JFrame;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;
import org.math.plot.Plot2DPanel;
import org.math.plot.plotObjects.BaseLabel;
import com.diplomadobd.dao.FandeliDAO;
import com.diplomadobd.dto.SplineAccumRule;

/**
 * this class transforms
 *
 * @author mario
 */
public class DataAsFunction {

    public DataAsFunction() {
        //Do the connection
    }

    /**
     * Return the skus
     *
     * @param conn
     * @return
     * @throws SQLException
     */
    private List<String> getSkus(Connection conn) throws SQLException {
        String querySkus = "select distinct sku\n"
                + "from aux_fandeli\n"
                + "order by sku";
        List<String> theList = new ArrayList();
        PreparedStatement ps = conn.prepareStatement(querySkus);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            theList.add(rs.getString("sku"));
        }
        return theList;
    }

    /**
     *
     * @param sku
     * @param conn
     * @param baseYear
     */
    private List<Object[]> generateSyntheticDemand(String sku, Connection conn, int baseYear) {
        //Get the time series
        List<Object[]> theList = FandeliDAO.getOriginalDataPlusX(sku, baseYear, conn);
        List<Object[]> newRows = new ArrayList();
        SplineAccumRule theRule = FandeliDAO.getRule(1, conn);
        double[] y = new double[theList.size()];
        double[] x = new double[theList.size()];
        double accValue = 0;
        //Build the data generate the acumulated interpolation
        for (int i = 0; i < theList.size(); i++) {
            Object[] originalRow = theList.get(i);
            x[i] = ((Integer) originalRow[4]).doubleValue();
            accValue += ((Integer) originalRow[3]).doubleValue();
            y[i] = accValue;
        }
        //Generate the splines
        UnivariateInterpolator interpolator = new SplineInterpolator();
        UnivariateFunction polinomio = interpolator.interpolate(x, y);
        //Now generate the original and the artificial demand
        Object[] prevRow, row, newRow;
        double prevY, actualY;
        prevY = 0;
        for (int i = 1; i < theList.size(); i++) {
            prevRow = theList.get(i - 1);
            row = theList.get(i);
            int iniIndex = (Integer) prevRow[4];
            int nextIndex = (Integer) row[4];
            //Take last week of previous month
            newRow = new Object[7];
            newRow[0] = prevRow[0];//Sku
            newRow[1] = prevRow[1];//Year
            newRow[2] = prevRow[2];//Month
            newRow[3] = prevRow[4];//xvalue
            actualY = polinomio.value(((Integer) (prevRow[4])).doubleValue());// cumulative demand sas spline
            newRow[4] = actualY - prevY;
            newRow[5] = 0;//Observation id
            newRow[6] = new Boolean(false); //this is an original observation
            newRows.add(newRow);
            prevY = actualY;
            //Now adds the elements of the next month
            for (int j = iniIndex + 1; j < nextIndex; j++) {
                //sku (String) , year,  month,  week, xvalue, 
                //demand (Double), forecastid, spline (boolean)
                newRow = new Object[7];
                newRow[0] = row[0];//id
                newRow[1] = row[1];//year
                newRow[2] = row[2];//month
                newRow[3] = j;//value on x
                actualY = polinomio.value(((Integer) j).doubleValue());
                newRow[4] = actualY - prevY;
                newRow[5] = 0;//Observation id
                newRow[6] = new Boolean(true); //this is a splin
                newRows.add(newRow);
                prevY = actualY;
            }
            if (i + 1 == theList.size()) {
                //Now adds the last element of the week for the series
                newRow = new Object[7];
                newRow[0] = row[0];//id
                newRow[1] = row[1];//year
                newRow[2] = row[2];//month
                newRow[3] = row[4];//value on x
                actualY = polinomio.value(((Integer) row[4]).doubleValue());
                newRow[4] = actualY - prevY;
                newRow[5] = 0;//Observation id
                newRow[6] = new Boolean(false); //this is an original observation
                newRows.add(newRow);
            }
        }
        return newRows;
    }

    /**
     * Generates the Accumulated demand
     *
     * @param sku
     * @param conn
     * @param baseYear
     */
    private void generateAccumDemand(String sku, Connection conn,
            int baseYear) {
        //Get the time series
        List<Object[]> theList = FandeliDAO.getOriginalDataPlusX(sku, baseYear, conn);
        SplineAccumRule theRule = FandeliDAO.getRule(1, conn);
        double[] accDemand = new double[theList.size()];
        double accumulator = 0;
        for (int i = 0; i < theList.size(); i++) {
            Object[] originalRow = theList.get(i);
            double cumDemand;
            int x = (Integer) originalRow[4];
            int year = (Integer) originalRow[1];
            int month = (Integer) originalRow[2];
            double currentDemand = ((Integer) originalRow[3]).doubleValue();
            //Now verifies if if we should reset the accumulator
            int modulus = (x + 1) % theRule.getTotalWeeks();
            if (modulus == 0) {
                //Gets the accumulated data from 
                try {
                    double startDemand = accDemand[i - theRule.getExceptionWeeks()];
                    double endDemand = accDemand[i - 1];
                    accumulator = endDemand - startDemand;
                } catch (ArrayIndexOutOfBoundsException e) {
                    //Do nothing, it is the first case
                }
            } else if (modulus == theRule.getThresholdWeek()) {
                //Gets the accumulated data from 
                try {
                    double startDemand = accDemand[i - theRule.getThresholdWeek() - 1];
                    double endDemand = accDemand[i - 1];
                    accumulator = endDemand - startDemand;
                } catch (ArrayIndexOutOfBoundsException e) {
                    //Do nothing, it is the first case
                }
            }
            accumulator += currentDemand;
            accDemand[i] = accumulator;
            //Now inserts the accumulated row
            FandeliDAO.insertCumDemand(sku, theRule.getCumRule(), year, month, x, accDemand[i], conn);
        }
    }

    /**
     * Populates the table of accumulated demand
     *
     * @param baseYear
     */
    public void accumDemand(int baseYear) {
        Connection conn = null;
        try {
            conn = new com.diplomadobd.dao.Dao().getDbConnection();
            List<String> skus = this.getSkus(conn);
            for (String sku : skus) {
                generateAccumDemand(sku, conn, baseYear);
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);

        } finally {
            try {
                conn.close();
            } catch (Exception ex) {
                //Do nothing
            }
        }
    }

    public void insertSplines(int idCumRule) {
        Connection conn = null;
        try {
            conn = new com.diplomadobd.dao.Dao().getDbConnection();
            List<String> skus = this.getSkus(conn);
            SplineAccumRule theRule = FandeliDAO.getRule(idCumRule, conn);
            for (String sku : skus) {
                List theSplines = this.generateSplines(sku, idCumRule, conn);
                insertSplines(theSplines, theRule, conn);

            }
        } catch (Exception e) {
            throw new IllegalStateException(e);

        } finally {
            try {
                conn.close();
            } catch (Exception ex) {
                //Do nothing
            }
        }
    }

    /**
     *Generates the synthetic demand based on the original data
     * @param baseYear
     * @param conn
     */
    public void generateSynthDemand(int baseYear, Connection conn) {
        int idCumRule = 0;
        try {
            List<String> skus = this.getSkus(conn);
            SplineAccumRule theRule = FandeliDAO.getRule(idCumRule, conn);
            //truncate the augmented table
            FandeliDAO.truncate("fandeli_augmented", conn);
            for (String sku : skus) {
                List theSplines = this.generateSyntheticDemand(sku, conn, baseYear);
                insertSplines(theSplines, theRule, conn);
            }
            //After inserting splines, compute the offset
            FandeliDAO.computeSplineOffset(conn);
        } catch (Exception e) {
            throw new IllegalStateException(e);

        } 
    }

/**
     *Generates the synthetic demand based on the original data
     * @param baseYear
     * @param conn
     */
    public void generateOriginalTable(List<Object[]> theRows, Connection conn) {        
        try {
            //truncate the original table
            FandeliDAO.truncate("aux_fandeli", conn);
            //After inserting splines, compute the offset
            this.insertOriginalTable(theRows,conn);
        } catch (Exception e) {
            throw new IllegalStateException(e);

        } 
    }    
    /**
     * Get a general idea of how are the series
     */
    public void plotSeries() {
        Connection conn = null;
        try {
            conn = new com.diplomadobd.dao.Dao().getDbConnection();
            List<String> skus = getSkus(conn);
            Plot2DPanel plot = new Plot2DPanel();
            BaseLabel baseLabel = new BaseLabel("Interpolación del SKUs", Color.blue, 0.5, 1.1);
            plot.addPlotable(baseLabel);
            plot.addLegend("EAST");
            for (String sku : skus) {
                drawSpline(sku, conn, plot);

            }
            JFrame frame = new JFrame("Interpolación del SKUs");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 500);
            frame.add(plot, BorderLayout.CENTER);
            frame.setVisible(true);
        } catch (Exception e) {
            throw new IllegalStateException(e);

        } finally {
            try {
                conn.close();
            } catch (Exception ex) {
                //Do nothing
            }
        }
        //query the series

    }

    public static void main(String[] args) {
        DataAsFunction dataAsaF = new DataAsFunction();
        //dataAsaF.insertSplines(1);
        dataAsaF.plotSeries();

    }

    /**
     * Generates the splines from original data
     *
     * @param sku
     * @param conn
     * @return
     * @throws SQLException
     */
    private List<Object[]> generateSplines(String sku, int idCum, Connection conn) throws SQLException {

        List<Object[]> aList = FandeliDAO.getCumData(sku, idCum, conn);

        //Build functions for spline
        int arraySize = aList.size();
        double x[] = new double[arraySize];
        double y[] = new double[arraySize];
        Object row[];
        for (int i = 0; i < arraySize; i++) {
            row = aList.get(i);
            x[i] = ((Integer) row[3]).doubleValue();
            y[i] = (Double) row[4];
        }
        Object[] prevRow = null;
        List<Object[]> newRows = new LinkedList();
        Object[] newRow = null;
        UnivariateInterpolator interpolator = new SplineInterpolator();
        UnivariateFunction polinomio = interpolator.interpolate(x, y);
        //Generate splines
        for (int i = 1; i < arraySize; i++) {
            prevRow = aList.get(i - 1);
            row = aList.get(i);
            int iniIndex = (Integer) prevRow[3];
            int nextIndex = (Integer) row[3];
            //Take last week of previous month
            newRow = new Object[7];
            newRow[0] = prevRow[0];//Sku
            newRow[1] = prevRow[1];//Year
            newRow[2] = prevRow[2];//Month
            newRow[3] = prevRow[3];//xvalue
            newRow[4] = polinomio.value(((Integer) (prevRow[3])).doubleValue());//demand sas spline
            newRow[5] = 0;//Observation id
            newRow[6] = new Boolean(false); //this is an original observation
            newRows.add(newRow);
            //Now adds the elements of the next month
            for (int j = iniIndex + 1; j < nextIndex; j++) {
                //sku (String) , year,  month,  week, xvalue, 
                //demand (Double), forecastid, spline (boolean)
                newRow = new Object[7];
                newRow[0] = row[0];//id
                newRow[1] = row[1];//year
                newRow[2] = row[2];//month
                newRow[3] = j;//value on x
                newRow[4] = polinomio.value(((Integer) j).doubleValue());
                newRow[5] = 0;//Observation id
                newRow[6] = new Boolean(true); //this is a splin
                newRows.add(newRow);
            }
            if (i + 1 == arraySize) {
                //Now adds the last element of the week for the series
                newRow = new Object[7];
                newRow[0] = row[0];//id
                newRow[1] = row[1];//year
                newRow[2] = row[2];//month
                newRow[3] = row[3];//value on x
                newRow[4] = polinomio.value(((Integer) row[3]).doubleValue());
                newRow[5] = 0;//Observation id
                newRow[6] = new Boolean(false); //this is an original observation
                newRows.add(newRow);
            }

        }
        return newRows;
    }

    /**
     * Inserts the new rows into the new structure
     *
     * @param newRows
     * @param conn
     * @param theRule
     * @throws SQLException
     */
    private void insertSplines(List<Object[]> newRows, SplineAccumRule theRule,
            Connection conn) throws SQLException {
        //sku (String) , year,  month,  week, xvalue, 
        //demand (Double), forecastid, spline (boolean)
        String sqlStatement = "INSERT INTO fandeli_augmented(sku, idcum, year, month, xvalue, demand, forecastid, spline)\n"
                + "Values (?,?,?,?,?,?,?,?)";
        for (Object[] newRow : newRows) {
            PreparedStatement ps = conn.prepareStatement(sqlStatement);
            ps.setString(1, (String) newRow[0]);
            ps.setInt(2, (Integer) theRule.getCumRule());
            ps.setInt(3, (Integer) newRow[1]);
            ps.setInt(4, (Integer) newRow[2]);
            ps.setInt(5, (Integer) newRow[3]);
            ps.setDouble(6, (Double) newRow[4]);
            ps.setInt(7, (Integer) newRow[5]);
            ps.setBoolean(8, (Boolean) newRow[6]);
            ps.execute();
        }

    }

    /**
     * Inserts the new rows into the new structure
     *
     * @param newRows
     * @param conn
     * @param theRule
     * @throws SQLException
     */
    private void insertOriginalTable(List<Object[]> newRows, Connection conn) throws SQLException {
        //sku (String) , year,  month,  demand
        String sqlStatement = "INSERT INTO aux_fandeli(sku, year, month, demand)\n"
                + "Values (?,?,?,?)";
        for (Object[] newRow : newRows) {
            PreparedStatement ps = conn.prepareStatement(sqlStatement);
            ps.setString(1, (String) newRow[0]);
            ps.setInt(2, (Integer) Integer.parseInt((String)newRow[1]));
            ps.setInt(3, (Integer) newRow[2]);
            ps.setInt(4, (Integer) Integer.parseInt((String)newRow[3]));
            ps.execute();
        }

    }
    
    
    private void drawSpline(String sku, Connection conn, Plot2DPanel plot) throws SQLException {
        String querySkuSeries = "select xvalue,demand\n"
                + "from fandeli_augmented  \n"
                + "where sku = ? "//and spline=false\n"
                + "order by xvalue";
        PreparedStatement ps = conn.prepareStatement(querySkuSeries);
        ps.setString(1, sku);
        ResultSet rs = ps.executeQuery();
        LinkedList<Double> x = new LinkedList();
        LinkedList<Double> y = new LinkedList();

        while (rs.next()) {
            x.add(((Integer) rs.getInt("xvalue")).doubleValue());
            y.add(rs.getDouble("demand"));
        }
        double[] yarr = null;
        double[] xarr = null;
        xarr = x.stream().mapToDouble(Double::doubleValue).toArray();
        yarr = y.stream().mapToDouble(Double::doubleValue).toArray();
        plot.addLinePlot(sku, xarr, yarr);
    }

}
