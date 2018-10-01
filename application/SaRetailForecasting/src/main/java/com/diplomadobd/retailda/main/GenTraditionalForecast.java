/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diplomadobd.retailda.main;

import java.sql.Connection;
import com.diplomadobd.dao.Dao;
import com.doplomadobd.retailda.ABT_Generation;
import com.doplomadobd.retailda.Forecast;

/**
 *
 * @author mario
 */
public class GenTraditionalForecast {
     public static void main(String[] args) {
        Connection conn = null;
        int baseYear = 2014;
        try {
            Dao dao = new Dao();
            conn = dao.getDbConnection();
            String modelFolder = "../models/";
            Forecast f = new Forecast(conn, modelFolder);
            f.generateTraditionalForecast(baseYear);
            System.out.println("OK");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (Exception ex) {
                //Do Nothing
            }
        }
    }
}
