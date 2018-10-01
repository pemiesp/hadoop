/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diplomadobd.retailda.main;

import java.sql.Connection;
import com.diplomadobd.dao.Dao;
import com.doplomadobd.retailda.KnowledgeFlowGen;

/**
 *
 * @author mario
 */
public class GenerateKFs {

    public static void main(String[] args) {
        int cumRule = 3;
        Connection conn = null;
        try {
            Dao dao = new Dao();
            conn = dao.getDbConnection();
            String templateFileName = "template.kf";
            String kfFolder = "../kflows/";
            KnowledgeFlowGen kf = new KnowledgeFlowGen(templateFileName, kfFolder, conn);
            //kf.generateKF(1);
            //kf.generateKF(2);
            kf.generateKF(cumRule);
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
