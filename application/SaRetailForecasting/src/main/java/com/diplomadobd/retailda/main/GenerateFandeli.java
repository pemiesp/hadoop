/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diplomadobd.retailda.main;

import java.sql.Connection;
import java.util.Map;
import com.diplomadobd.dao.Dao;
import com.diplomadobd.dao.FandeliDAO;
import com.diplomadobd.dto.SplineAccumRule;
import com.doplomadobd.retailda.DataAsFunction;

/**
 *
 * @author mario
 */
public class GenerateFandeli {

    private Connection conn;

    public GenerateFandeli() {
        //Get the Connection
        this.conn = new Dao().getDbConnection();

    }

    /**
     * Generate all steps from the original data
     *
     * @param rules When Rules is null execute all the the rules. Else use the
     * rules in the array
     */
    public void generateFromOriginalData(int baseYear, Integer[] rules) {
        int start, end;
        //Generate Splines and the augmented table
        DataAsFunction dataAsaF = new DataAsFunction();
        dataAsaF.generateSynthDemand(baseYear, conn);
        //Get all the rules above zero
        Map<Integer, SplineAccumRule> theRules = FandeliDAO.getRulesAboveZero(conn);
        if (rules == null) {
            rules = theRules.keySet().toArray(rules);
        }
        
        
        //Create other objects
        GenerateABT gABT = new GenerateABT();
        for (Integer ruleId : rules) {
            SplineAccumRule rule = theRules.get(ruleId);
            if (rule != null) {                
                //Generar las tablas ABT, por cada Cum rule
                gABT.generateAbt(ruleId, conn);
                //Calcular los modelos
                //Hacer la predicción del Recall
                //Hacer la predicción de los siguientes valores consecutivos               

            } else {
                throw new IllegalStateException("The idcum " + ruleId + " does not exist.");
            }
        }
    }
    /**
     * Main method to generate all from scratch
     * @param args 
     */
    public static void main(String[] args) {
        GenerateFandeli gF = new GenerateFandeli();
        //null means generate all rules
        Integer[] rules = new Integer[1];
        rules[0] = 2;
        gF.generateFromOriginalData(2014, rules);
    }

}
