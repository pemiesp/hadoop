/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diplomadobd.retailda.main;

import com.diplomadobd.dataintegration.io.core.TextFileHelper;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import com.diplomadobd.dao.Dao;
import com.diplomadobd.dao.FandeliDAO;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.CSVLoader;
import weka.core.converters.CSVSaver;

/**
 *
 * @author mario
 */
public class LoadRecall {

    public static void main(String[] args) {
        String resultslFolderName = "../results/";
        Connection conn = null;
        int idcum = -1;
        try {
            Dao dao = new Dao();
            conn = dao.getDbConnection();
            File resultsFolder = new File(resultslFolderName);
            File[] results = resultsFolder.listFiles();
            for (File result : results) {
                String fileName = result.getName().split("_")[0];
                String[] fileTokens = fileName.split("-");
                String sku = fileTokens[0];
                int cumRule = Integer.parseInt(fileTokens[1]);
                boolean flag = true;
                if(idcum!=-1 && idcum!=cumRule){
                    flag=false;
                }
                if (flag) {
                    BufferedReader br = TextFileHelper.getBufferedReader(result.getCanonicalPath());
                    FandeliDAO.loadResults(sku, cumRule, conn, br);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                conn.close();
            } catch (Exception ex1) {

            }
        }

    }
}
