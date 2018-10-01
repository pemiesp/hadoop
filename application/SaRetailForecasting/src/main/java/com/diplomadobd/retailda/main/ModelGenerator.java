/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diplomadobd.retailda.main;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import com.diplomadobd.dao.Dao;
import com.diplomadobd.dao.FandeliDAO;

/**
 *
 * @author mario
 */
public class ModelGenerator {

    public static void main(String[] args) {

        int cumRule = 1;
        Connection conn = null;

        try {

            Dao dao = new Dao();
            conn = dao.getDbConnection();
            List<String> skus = FandeliDAO.getSkus(conn);
            for (String sku : skus) {
                String token = sku + "-" + cumRule + ".kf";

                Process process = new ProcessBuilder(
                        "/usr/lib/jvm/java-8-oracle/bin/java", "-cp", "/home/mario/weka-3-8-1/weka.jar", "weka.knowledgeflow.FlowRunner", "/home/mario/NewSharedFolder/GitViews/Fandeli/kflows/" + token).start();
                InputStream is = process.getInputStream();
                InputStream es = process.getErrorStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
                br = new BufferedReader(new InputStreamReader(es));
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
                //args[0] = "../kflows/MLP_NL012.kf";

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
