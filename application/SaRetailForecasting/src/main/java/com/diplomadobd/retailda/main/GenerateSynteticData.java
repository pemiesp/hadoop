/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diplomadobd.retailda.main;

import com.diplomadobd.dao.Dao;
import com.doplomadobd.retailda.DataAsFunction;

/**
 * Obsoleto, borrar
 * @author mario
 */
public class GenerateSynteticData {
    public static void main(String[] args) {
        DataAsFunction dataAsaF = new DataAsFunction();
        dataAsaF.generateSynthDemand(2014,new Dao().getDbConnection());
        dataAsaF.plotSeries();
    }
   
}
