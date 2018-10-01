/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diiplomadobd.dataintegration.io.cfg;

/**
 *
 * @author root
 */
public class BatchOutputCfg {
    /**
     * Output file name  where the insert statements will written.
     */
    protected String insertOutputFileName;
    protected String dbVendor;
    protected String dbTable;
    protected String user;
    protected String pwd;

    /**
     * @return the insertOutputFileName
     */
    public String getInsertOutputFileName() {
        return insertOutputFileName;
    }

    /**
     * @param insertOutputFileName the insertOutputFileName to set
     */
    public void setInsertOutputFileName(String insertOutputFileName) {
        this.insertOutputFileName = insertOutputFileName;
    }

    /**
     * @return the dbVendor
     */
    public String getDbVendor() {
        return dbVendor;
    }

    /**
     * @param dbVendor the dbVendor to set
     */
    public void setDbVendor(String dbVendor) {
        this.dbVendor = dbVendor;
    }

    /**
     * @return the dbTable
     */
    public String getDbTable() {
        return dbTable;
    }

    /**
     * @param dbTable the dbTable to set
     */
    public void setDbTable(String dbTable) {
        this.dbTable = dbTable;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return the pwd
     */
    public String getPwd() {
        return pwd;
    }

    /**
     * @param pwd the pwd to set
     */
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    
}
