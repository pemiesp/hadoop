/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diplomadobd.retailda.main;

import com.doplomadobd.retailda.DataAsFunction;

/**
 *
 * @author mario
 */
public class GenerateCum {
    public static void main(String[] args) {
        DataAsFunction daf = new DataAsFunction();
        daf.accumDemand(2014);
    }
    
}
