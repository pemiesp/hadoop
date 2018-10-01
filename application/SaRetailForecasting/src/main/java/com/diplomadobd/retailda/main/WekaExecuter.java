/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diplomadobd.retailda.main;


import weka.knowledgeflow.FlowRunner;

/**
 *
 * @author mario
 */
public class WekaExecuter {
    
    public static void main(String[] args) {
        try {
            FlowRunner fr = new FlowRunner();
            FlowRunner.SimpleLogger sl = new FlowRunner.SimpleLogger();            
            args = new String[1];
            args[0] = "../kflows/NL001-2.kf";
            //args[0] = "../kflows/MLP_NL012.kf";
            fr.run(fr, args);            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
    
}
