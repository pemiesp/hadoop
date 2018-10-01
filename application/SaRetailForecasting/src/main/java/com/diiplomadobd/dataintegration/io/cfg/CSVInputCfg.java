/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diiplomadobd.dataintegration.io.cfg;

/**
 *  Generic CSV input configuration, for comma separated datasets.
 * @author root
 */
public class CSVInputCfg extends RawInputCfgAbs {
    public CSVInputCfg(){
        this.setDelimiter(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        this.setTheDelimiterLimit(-1);
    }
    
}
