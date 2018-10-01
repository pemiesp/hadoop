/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diplomadobd.retailda.main;

import java.sql.Connection;
import com.diplomadobd.dao.Dao;
import com.diplomadobd.dao.FandeliDAO;
import com.doplomadobd.retailda.ABT_Generation;

/**
 * Generates the ABT for any particular cumulative rule
 *
 * @author mario
 */
public class GenerateABT {

    public static void main(String[] args) {
        int cumRule = 1;
        Connection conn = null;
        try {
            Dao dao = new Dao();
            conn = dao.getDbConnection();
            GenerateABT ga = new GenerateABT();
            ga.generateAbt(cumRule, conn);
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

    public void generateAbt(int cumRule, Connection conn) {
        try {
            FandeliDAO.deleteByCumRule("fandeli_abtcum",cumRule, conn);
            FandeliDAO.deleteByCumRule("fandeli_abtcumvar", cumRule, conn);
            ABT_Generation abtGenerator = new ABT_Generation(cumRule, conn);
            abtGenerator.buildAbtTables(cumRule);
            String path = "../datasets/abt/";
            abtGenerator.printABTTrainingSet(path, cumRule);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
