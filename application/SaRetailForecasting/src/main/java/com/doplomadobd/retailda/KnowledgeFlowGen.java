/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doplomadobd.retailda;

import com.diplomadobd.dataintegration.io.core.TextFileHelper;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.Scanner;
import com.diplomadobd.dao.FandeliDAO;

/**
 *
 * @author mario
 */
public class KnowledgeFlowGen {
    private String templateFileName;
    private String kfFolder;
    private Connection conn ;
    public KnowledgeFlowGen(String templateFileName, String kfFolder, Connection conn){
        this.templateFileName = templateFileName;
        this.kfFolder = kfFolder;
        this.conn = conn;
    }
    public void generateKF(int cumRule){
        //Read the template kf
        try{
        String template = new Scanner(new File(kfFolder+templateFileName)).useDelimiter("\\Z").next();        
        List<String> theSkus = FandeliDAO.getSkus(conn);
        for (String theSku : theSkus) {
            String curContent = new String(template);
            String token = theSku+"-"+cumRule;
            String kfFileName = this.kfFolder+token+".kf";
            curContent = curContent.replace("template", token);
            BufferedWriter bw = TextFileHelper.getBufferedWriter(kfFileName);
            bw.write(curContent);
            bw.close();
        }
        }catch (IOException ex){
            throw new IllegalStateException("Error reading the template");
        }
    }
}
