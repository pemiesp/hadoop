/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diplomadobd.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.diplomadobd.dto.SplineAccumRule;

/**
 *
 * @author mario
 */
public class FandeliDAO {

    /**
     * Retrieves all the SKUS
     *
     * @param conn
     * @return
     */
    public static List<String> getSkus(Connection conn) {
        List<String> theList = null;
        try {
            String querySkus = "select distinct sku\n"
                    + "from aux_fandeli\n"
                    + "order by sku";
            theList = new ArrayList();
            PreparedStatement ps = conn.prepareStatement(querySkus);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                theList.add(rs.getString("sku"));
            }
        } catch (Exception e) {
            throw new IllegalStateException("Error getting the SKUs.", e);
        }
        return theList;
    }

    public static Object[] getTimeSeriesForABT(String sku, Connection conn) {
        Object[] theTimeSeries = null;
        String message = "Error getting the count.";
        try {

            String countSeries = "SELECT count('1') total\n"
                    + "FROM fandeli_augmented a\n"
                    + "WHERE a.sku = ?\n";
            PreparedStatement ps = conn.prepareStatement(countSeries);
            ps.setString(1, sku);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int totalRecords = rs.getInt("total");
            theTimeSeries = new Object[totalRecords];
            message = "Error getting the query for the time series.";
            String querySeries = "SELECT a.xvalue, a.demand\n"
                    + "FROM fandeli_augmented a\n"
                    + "WHERE a.sku = ? and idcum=0 and forecastid = 0\n"
                    + "ORDER BY a.xvalue";
            ps = conn.prepareStatement(querySeries);
            ps.setString(1, sku);
            rs = ps.executeQuery();
            for (int i = 0; i < theTimeSeries.length; i++) {
                rs.next();
                Object[] record = new Object[2];
                record[0] = rs.getInt("xvalue");
                record[1] = rs.getDouble("demand");
                theTimeSeries[i] = record;
            }
        } catch (Exception e) {
            throw new IllegalStateException("Error getting the "
                    + "time series for the sku: " + sku + ".\n" + message, e);
        }
        return theTimeSeries;
    }

    /**
     * Brings the cumulative demand considering the given bounds
     *
     * @param sku
     * @param startX
     * @param endX
     * @param currentX
     * @param targetX
     * @param conn
     * @return
     */
    public static double[] getLeastCumDemandAndCumSplines(String sku, int startX, int endX,
            int currentX, int targetX, Connection conn) {

        double theLiestCumDemamd[] = new double[2];
        try {
            String querySkus = "SELECT a.cumulativedemand,b.cumulativesplines\n"
                    + "FROM\n"
                    + "(SELECT 1 as x, sum(demand) cumulativedemand\n"
                    + "FROM fandeli_augmented\n"
                    + "WHERE sku = ? and\n"
                    + "	forecastid = 0 and\n"
                    + "	xvalue >= ? and\n"
                    + "	xvalue <= ?\n"
                    + "GROUP BY sku\n"
                    + ") a INNER JOIN \n"
                    + "(\n"
                    + "SELECT 1 as x, sum(demand) cumulativeSplines\n"
                    + "FROM fandeli_augmented\n"
                    + "WHERE sku = ? and\n"
                    + "	forecastid = 0 and\n"
                    + "	spline = true and\n"
                    + "	xvalue > ? and\n"
                    + "	xvalue < ?\n"
                    + "GROUP BY sku\n"
                    + ") b on (a.x = b.x)";
            PreparedStatement ps = conn.prepareStatement(querySkus);
            ps.setString(1, sku);
            ps.setInt(2, startX);
            ps.setInt(3, endX);
            ps.setString(4, sku);
            ps.setInt(5, currentX);
            ps.setInt(6, targetX);
            ResultSet rs = ps.executeQuery();
            boolean flag = false;
            while (rs.next()) {
                theLiestCumDemamd[0] = rs.getDouble("cumulativedemand");
                theLiestCumDemamd[1] = rs.getDouble("cumulativesplines");
                flag = true;
            }
            if (!flag) {
                throw new IllegalStateException("Empty query");
            }
        } catch (Exception e) {
            throw new IllegalStateException("Error getting the SKUs.", e);
        }
        return theLiestCumDemamd;
    }

    /**
     * Insert the abt rows
     *
     * @param cumAbt The double values for the accumulated ABT
     * @param varcumAbt The double values for the var accumulated ABT
     * @param xvalue
     * @param lag
     * @param sku
     * @param idcum
     * @param cumSplines
     * @param conn
     */
    public static void insertAbtRows(double[] cumAbt, double[] varcumAbt,
            int xvalue, int lag, String sku, int idcum, double cumSplines,
            Connection conn) {
        String cumTable = "fandeli_abtcum";
        String varCumTable = "fandeli_abtcumvar";
        String cumColSufix = "cdl";
        String varCumColSufix = "vcdl";
        //Create the string for the value names and the names
        StringBuilder sbCum = new StringBuilder();
        StringBuilder sbVarCum = new StringBuilder();
        StringBuilder sbCumValues = new StringBuilder();
        StringBuilder sbVarCumValues = new StringBuilder();
        for (int i = 0; i < lag + 1; i++) {
            sbCum.append(cumColSufix);
            sbCum.append(lag - i);
            sbCum.append(",");
            sbCumValues.append(cumAbt[i]);
            sbCumValues.append(",");
            //--
            if (i < lag) {
                sbVarCum.append(varCumColSufix);
                sbVarCum.append(lag - i);
                sbVarCum.append(lag - i - 1);
                sbVarCum.append(",");
                sbVarCumValues.append(varcumAbt[i]);
                sbVarCumValues.append(",");
            }
        }
        //Build both insert statements
        String cumAbtStm = "INSERT INTO " + cumTable + " "
                + "(sku, xvalue, idcum, cumsplines," + sbCum + cumColSufix + "target) VALUES("
                + "'" + sku + "'" + "," + xvalue + "," + idcum + "," + cumSplines + "," + sbCumValues + cumAbt[cumAbt.length - 1] + ")";
        String varCumAbtStm = "INSERT INTO " + varCumTable + " "
                + "(sku, xvalue, idcum, " + sbVarCum + varCumColSufix + "target) VALUES("
                + "'" + sku + "'" + "," + xvalue + "," + idcum + "," + sbVarCumValues + varcumAbt[varcumAbt.length - 1] + ")";
        //Executes both queries
        try {
            PreparedStatement ps = conn.prepareStatement(cumAbtStm);
            ps.execute();
            ps = conn.prepareStatement(varCumAbtStm);
            ps.execute();
        } catch (Exception e) {
            throw new IllegalStateException("Error inserting the row in the ABT.", e);
        }

    }

    /**
     * Insert the abt rows
     *
     * @param varcumAbt The double values for the var accumulated ABT
     * @param xvalue
     * @param lag
     * @param sku
     * @param idcum
     * @param conn
     */
    public static void insertAbtRows(double[] varcumAbt,
            int xvalue, int lag, String sku, int idcum,
            Connection conn) {
        String varCumTable = "fandeli_abtcumvar";
        String varCumColSufix = "vcdl";
        //Create the string for the value names and the names
        StringBuilder sbVarCum = new StringBuilder();
        StringBuilder sbVarCumValues = new StringBuilder();
        for (int i = 0; i < lag; i++) {
            sbVarCum.append(varCumColSufix);
            sbVarCum.append(lag - i);
            sbVarCum.append(lag - i - 1);
            sbVarCum.append(",");
            sbVarCumValues.append(varcumAbt[i]);
            sbVarCumValues.append(",");
        }
        //Build both insert statements
        String varCumAbtStm = "INSERT INTO " + varCumTable + " "
                + "(sku, xvalue, idcum, " + sbVarCum + varCumColSufix + "target) VALUES("
                + "'" + sku + "'" + "," + xvalue + "," + idcum + "," + sbVarCumValues + varcumAbt[varcumAbt.length - 1] + ")";
        //Executes both queries
        try {
            PreparedStatement ps = conn.prepareStatement(varCumAbtStm);
            ps.execute();
        } catch (Exception e) {
            //If there is an infinity value, skips the insertion but does not
            //cancel the excecution
            if (e.getMessage().indexOf("infinity") == -1 && e.getMessage().indexOf("nan") == -1) {
                throw new IllegalStateException("Error inserting the row in the ABT.", e);
            }
        }

    }

    /**
     * Inserts both cum and var abt
     *
     * @param cumAbt
     * @param varcumAbt
     * @param xvalue
     * @param lag
     * @param sku
     * @param idcum
     * @param conn
     */
    public static void insertAbtRows(double[] cumAbt, double[] varcumAbt,
            int xvalue, String sku, int idcum,
            Connection conn) {
        String cumAbtStm = "INSERT INTO fandeli_abtcum (sku, xvalue, "
                + "idcum, cdl3, cdl2, cdl1, cdl0, cdltarget) values(?, ?, ?, ?, ?, ?, ?, ?)";
        String varCumAbtStm = " INSERT INTO fandeli_abtcumvar (sku, xvalue, idcum,"
                + "vcdl32, vcdl21, vcdl10, vcdltarget) values(?, ?, ?, ?, ?, ?, ?)";
        //Executes both queries
        try {
            PreparedStatement ps = conn.prepareStatement(cumAbtStm);
            ps.setString(1, sku);
            ps.setInt(2, xvalue);
            ps.setInt(3, idcum);
            ps.setDouble(4, cumAbt[0]);
            ps.setDouble(5, cumAbt[1]);
            ps.setDouble(6, cumAbt[2]);
            ps.setDouble(7, cumAbt[3]);
            ps.setDouble(8, cumAbt[4]);
            ps.execute();

            ps = conn.prepareStatement(varCumAbtStm);
            ps.setString(1, sku);
            ps.setInt(2, xvalue);
            ps.setInt(3, idcum);
            ps.setDouble(4, varcumAbt[0]);
            ps.setDouble(5, varcumAbt[1]);
            ps.setDouble(6, varcumAbt[2]);
            ps.setDouble(7, varcumAbt[3]);
            ps.execute();
        } catch (Exception e) {
            //If there is an infinity value, skips the insertion but does not
            //cancel the excecution
            if (e.getMessage().indexOf("infinity") == -1 && e.getMessage().indexOf("nan") == -1) {
                throw new IllegalStateException("Error inserting the row in the ABT.", e);
            }
        }

    }

    /**
     * Gets a List with the original data plus the x index, based on the initial
     * year given as parameter
     *
     * @param sku
     * @param yearBase
     * @param conn
     * @return
     */
    public static List<Object[]> getOriginalDataPlusX(String sku, int yearBase,
            Connection conn) {
        List<Object[]> aList;
        try {
            String queryskuData = "select sku, year, month, demand \n"
                    + "from aux_fandeli\n"
                    + "where sku = ?"
                    + "order by  sku , year , month ";
            PreparedStatement ps = conn.prepareStatement(queryskuData);
            ps.setString(1, sku);
            Object[] row;
            ResultSet rs = ps.executeQuery();
            aList = new LinkedList();
            //Retrieve data from database
            while (rs.next()) {
                row = new Object[5];
                row[0] = rs.getString(1);
                row[1] = rs.getInt(2);
                row[2] = rs.getInt(3);
                row[3] = rs.getInt(4);
                int x = ((Integer) row[2] * 4 - 1) + (((Integer) row[1] - yearBase) * 48);
                row[4] = x;
                aList.add(row);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error getting the Original Data");
        }
        return aList;
    }

    /**
     * Returns the simulated data with splines
     *
     * @param sku
     * @param idcum
     * @param forecastId
     * @param conn
     * @return
     */
    public static List<Object[]> getSyntheticData(String sku,
            int idcum, int forecastId, Connection conn) {
        List<Object[]> aList;
        try {
            String queryskuData = "select sku, year, month, xvalue, demand, spline \n"
                    + "from fandeli_augmented\n"
                    + "where sku = ? and idcum = ? and forecastid = ?"
                    + "order by  sku , year , month, xvalue ";
            PreparedStatement ps = conn.prepareStatement(queryskuData);
            ps.setString(1, sku);
            ps.setInt(2, idcum);
            ps.setInt(3, forecastId);
            Object[] row;
            ResultSet rs = ps.executeQuery();
            aList = new LinkedList();
            //Retrieve data from database
            while (rs.next()) {
                row = new Object[6];
                row[0] = rs.getString(1);
                row[1] = rs.getInt(2);
                row[2] = rs.getInt(3);
                row[3] = rs.getInt(4);
                row[4] = rs.getDouble(5);
                row[5] = rs.getBoolean(6);

                aList.add(row);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error getting the Original Data");
        }
        return aList;
    }

    public static List<Object[]> getCumData(String sku, int idcum, Connection conn) {
        List<Object[]> aList;
        try {
            String queryskuData = "select sku, year, month, xvalue, cumdemand \n"
                    + "from aux_fandelicum\n"
                    + "where sku = ? and idcum = ?"
                    + "order by  xvalue ";
            PreparedStatement ps = conn.prepareStatement(queryskuData);
            ps.setString(1, sku);
            ps.setInt(2, idcum);
            Object[] row;
            ResultSet rs = ps.executeQuery();
            aList = new LinkedList();
            //Retrieve data from database
            while (rs.next()) {
                row = new Object[5];
                row[0] = rs.getString("sku");
                row[1] = rs.getInt("year");
                row[2] = rs.getInt("month");
                row[3] = rs.getInt("xvalue");
                row[4] = rs.getDouble("cumdemand");
                aList.add(row);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error getting the Accumulated Data");
        }
        return aList;
    }

    public static SplineAccumRule getRule(int idCumRule, Connection conn) {
        SplineAccumRule rule = new SplineAccumRule();
        String theQuery = "SELECT a.weeks, a.lag, b.forecastid, b.wahead, b.weekthreshold, b.exceptionweeks\n"
                + "FROM fandeli_cumrule a INNER JOIN fandeli_forecast b ON (a.forecastid = b.forecastid)\n"
                + "WHERE idcum = ?";
        boolean flag = false;
        try {
            PreparedStatement ps = conn.prepareStatement(theQuery);
            ps.setInt(1, idCumRule);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                rule.setCumRule(idCumRule);
                rule.setLag(rs.getInt("lag"));
                rule.setTotalWeeks(rs.getInt("weeks"));
                rule.setWeeksAhead(rs.getInt("wahead"));
                rule.setExceptionWeeks(rs.getInt("exceptionweeks"));
                rule.setThresholdWeek(rs.getInt("weekthreshold"));
                rule.setForecastId(rs.getInt("forecastid"));
                flag = true;
            }
            if (!flag) {
                throw new IllegalStateException("The idCum: " + idCumRule + " does not exist.");
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
        return rule;
    }

    public static void insertCumDemand(String sku, int idcum, int year, int month, int xvalue, double demand, Connection conn) {
        String theStm = "INSERT INTO aux_fandelicum (sku, idcum, year, month, xvalue, cumdemand) VALUES (?,?,?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(theStm);
            ps.setString(1, sku);
            ps.setInt(2, idcum);
            ps.setInt(3, year);
            ps.setInt(4, month);
            ps.setInt(5, xvalue);
            ps.setDouble(6, demand);
            ps.execute();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void printTSet(String theSku, int cumRule,
            BufferedWriter bwTSet, Connection conn) {
        String header = "\"vcdl32\",\"vcdl21\",\"vcdl10\",\"vcdltarget\"\n";
        String sqlStmt = "SELECT vcdl32, vcdl21, vcdl10, vcdltarget \n"
                + "FROM fandeli_abtcumvar\n"
                + "WHERE sku =? and idcum =?\n"
                + "ORDER BY xvalue";
        try {
            PreparedStatement ps = conn.prepareStatement(sqlStmt);
            ps.setString(1, theSku);
            ps.setInt(2, cumRule);
            ResultSet rs = ps.executeQuery();
            bwTSet.write(header);
            while (rs.next()) {
                StringBuilder sb = new StringBuilder();
                sb.append(rs.getDouble(1));
                sb.append(",");
                sb.append(rs.getDouble(2));
                sb.append(",");
                sb.append(rs.getDouble(3));
                sb.append(",");
                sb.append(rs.getDouble(4));
                sb.append("\n");
                //Now print the line
                bwTSet.write(sb.toString());
            }
            bwTSet.close();
        } catch (SQLException ex) {
            throw new IllegalStateException("Error getting the data for the training set.");
        } catch (IOException ex) {
            throw new IllegalStateException("Error printing the abt.");
        }
    }

    public static void loadResults(String sku, int cumRule, Connection conn, BufferedReader br) {
        int counter1 = 0;
        int counter2 = 0;
        try {
            String stmt1 = "SELECT xvalue FROM fandeli_abtcumvar where sku=? and idcum=?\n"
                    + "ORDER BY xvalue";
            String stmt2 = "UPDATE fandeli_abtcumvar set vcdlforecast = ? WHERE sku=? AND idcum=? and xvalue=?";
            //read the header of the file
            br.readLine();
            PreparedStatement ps1, ps2;
            //First get the list of elements from the DBase
            ps1 = conn.prepareStatement(stmt1);
            ps1.setString(1, sku);
            ps1.setInt(2, cumRule);
            ResultSet rs = ps1.executeQuery();
            while (rs.next()) {
                counter1++;
                String[] theTokens = br.readLine().split(",");
                double forecastValue = Double.parseDouble(theTokens[3]);
                counter2++;
                int theXvalue = rs.getInt(1);
                ps2 = conn.prepareStatement(stmt2);
                ps2.setDouble(1, forecastValue);
                ps2.setString(2, sku);
                ps2.setInt(3, cumRule);
                ps2.setInt(4, theXvalue);
                ps2.execute();
            }
            if (counter1 != counter2) {
                throw new IllegalStateException("Forecast and Recall have not the same length.");
            }

        } catch (SQLException ex) {
            throw new IllegalStateException("Error loading the results");
        } catch (IOException ex) {
            if (counter1 == counter2) {
                throw new IllegalStateException("Error loading handling the results file");
            } else {
                throw new IllegalStateException("Forecast and Recall have not the same length.");
            }
        } finally {
            try {
                br.close();
            } catch (Exception ex1) {
            }
        }
    }

    /**
     * Brings the latest index set for forecasting
     *
     * @return
     */
    public static List<Object[]> getLatestForecastIndexes(Connection conn) {
        //The query
        String theQuery = "select sku,idcum,max(xvalue) from fandeli_abtcum  group by sku, idcum order by idcum";
        List<Object[]> theRecords = new ArrayList();
        try {
            PreparedStatement ps = conn.prepareStatement(theQuery);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Object[] aRecord = new Object[3];
                aRecord[0] = rs.getString(1);
                aRecord[1] = rs.getInt(2);
                aRecord[2] = rs.getInt(3);
                theRecords.add(aRecord);
            }

        } catch (Exception ex) {
            throw new IllegalStateException("Error getting the latest forecast indexes");
        }
        return theRecords;
    }

    public static void insertTraditionalForecastAbtRecords(String sku, int idcum, List<List<double[]>> theRecords, int xValue, Connection conn) {
        String insertCum = "INSERT INTO fandeli_abtcumfc (sku,xvalue,idcum,cdl3,cdl2,cdl1,cdl0)\n"
                + "values (?,?,?,?,?,?,?)";
        String insertCumVar = "INSERT INTO fandeli_abtcumvarfc (sku,xvalue,idcum,vcdl32,vcdl21,vcdl10,vcdlforecast)\n"
                + "values (?,?,?,?,?,?,?)";
        try {
            List<double[]> cumAbt = theRecords.get(0);
            List<double[]> cumVarAbt = theRecords.get(1);
            for (int i = 0; i < cumVarAbt.size(); i++) {
                double[] cumVarRow = cumVarAbt.get(i);
                double[] cumRow = cumAbt.get(i);
                int currentXvalue = xValue + i;
                PreparedStatement ps1 = conn.prepareStatement(insertCum);
                ps1.setString(1, sku);
                ps1.setInt(2, currentXvalue);
                ps1.setInt(3, idcum);
                ps1.setDouble(4, cumRow[0]);
                ps1.setDouble(5, cumRow[1]);
                ps1.setDouble(6, cumRow[2]);
                ps1.setDouble(7, cumRow[3]);
                ps1.execute();
                PreparedStatement ps2 = conn.prepareStatement(insertCumVar);
                ps2.setString(1, sku);
                ps2.setInt(2, currentXvalue);
                ps2.setInt(3, idcum);
                ps2.setDouble(4, cumVarRow[0]);
                ps2.setDouble(5, cumVarRow[1]);
                ps2.setDouble(6, cumVarRow[2]);
                ps2.setDouble(7, cumVarRow[3]);
                ps2.execute();

            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Get the rules above id equal to zero
     *
     * @param conn
     * @return
     */
    public static Map<Integer, SplineAccumRule> getRulesAboveZero(Connection conn) {
        Map<Integer, SplineAccumRule> theRules = new HashMap();
        String queryStm = "select a.idcum,a.weeks,a.lag,a.forecastid,b.wahead,b.weekthreshold, b.exceptionweeks\n"
                + "from fandeli_cumrule a inner join \n"
                + "     fandeli_forecast b on (a.forecastid = b.forecastid) where idcum >0\n"
                + "order by idcum";
        try {
            PreparedStatement ps = conn.prepareStatement(queryStm);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SplineAccumRule cumRule = new SplineAccumRule();
                cumRule.setCumRule(rs.getInt("idcum"));
                cumRule.setTotalWeeks(rs.getInt("weeks"));
                cumRule.setLag(rs.getInt("lag"));
                cumRule.setForecastId(rs.getInt("forecastid"));
                cumRule.setWeeksAhead(rs.getInt("wahead"));
                cumRule.setThresholdWeek(rs.getInt("weekthreshold"));
                cumRule.setExceptionWeeks(rs.getInt("exceptionweeks"));
                theRules.put(cumRule.getCumRule(), cumRule);
            }
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return theRules;
    }

    /**
     * Truncates a table
     *
     * @param tableName
     * @param conn
     */
    public static void truncate(String tableName, Connection conn) {
        try {
            String stmt = "truncate table " + tableName + ";";
            PreparedStatement ps = conn.prepareStatement(stmt);
            ps.execute();
        } catch (Exception e) {
            throw new IllegalStateException("Error doing the truncate to " + tableName);
        }
    }
    /**
     * Deletes a table by cum rule
     *
     * @param tableName
     * @param conn
     */
    public static void deleteByCumRule(String tableName, Integer cumRule, Connection conn) {
        try {
            String stmt = "delete from " + tableName + " where idcum ="+cumRule+";";
            PreparedStatement ps = conn.prepareStatement(stmt);
            ps.execute();
        } catch (Exception e) {
            throw new IllegalStateException("Error doing the truncate to " + tableName);
        }
    }

    /**
     * Computes the offset when there exists negative values
     *
     * @param conn
     */
    public static void computeSplineOffset(Connection conn) {
        try {
            //Clean the offset table
            truncate("fandeli_offset", conn);
            //Insert the offset table
            String stmt = "INSERT INTO fandeli_offset (sku,theoffset)\n"
                    + "select sku, CASE   \n"
                    + "          WHEN MIN(demand) < 0 THEN MIN(demand)*-1   \n"
                    + "          ELSE 0 END as theoffset\n"
                    + "from fandeli_augmented\n"
                    + "where idcum = 0\n"
                    + "group by sku\n"
                    + "order by sku;";
            PreparedStatement ps = conn.prepareStatement(stmt);
            ps.execute();
            //Executes the addition
            stmt = "with t as (\n"
                    + "	select a.sku as sku2, a.xvalue as xvalue2, a.demand + b.theoffset as newdemand\n"
                    + "	from fandeli_augmented as a inner join\n"
                    + "	     fandeli_offset as b on (a.sku = b.sku)\n"
                    + "	where a.idcum=0 \n"
                    + ")\n"
                    + "update fandeli_augmented\n"
                    + "set demand = t.newdemand \n"
                    + "from t\n"
                    + "where sku = t.sku2 and xvalue = t.xvalue2;";
            ps = conn.prepareStatement(stmt);
            ps.execute();
        } catch (Exception e) {
            throw new IllegalStateException("Error computing the offset for the Splines");
        }
    }
}
