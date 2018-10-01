/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diplomadobd.dataintegration.data.cfg;

import java.util.HashMap;

/**
 * Describes an abstract Data type for a specific Raw column.
 * Each Data type should extend from this class.
 * @author mg.fonseca
 */
public abstract class AbstractDataType {
    protected String sqlVendorName;
    protected HashMap<String,Integer> sqlVendorList;
    protected Integer sqlVendorId;
    /**
     * Establishes the specific sql configuration for the corresponding transformations.
     * Should create a HashTable with the <Name,ID> of the available vendors .
     * In this constructor we should assign the vendor id to the attribute sqlVendorId
     * @param sqlVendorName 
     */
    public AbstractDataType(String sqlVendorName){
        this.sqlVendorName = sqlVendorName.toLowerCase();
        this.sqlVendorList = new HashMap();        
        this.assignSqlVendors();
        this.verifySqlVendor();        
    }
    private void verifySqlVendor() throws IllegalStateException{
        Integer vendorId = this.sqlVendorList.get(this.sqlVendorName);
        if(vendorId == null){
            throw new IllegalStateException("The vendor "+
                    this.sqlVendorName+" has not been implemented yet for this DataType: ."+
                    this.getClass().getName());
        }
        //now assigns the vendor ID
        this.sqlVendorId = vendorId;
    }
    /**
     * This method is to assign the implemented vendors and its ID
     */    
     private void assignSqlVendors() {
        //Se agregan los vendors soportados
        this.sqlVendorList.put("postgres", 1);
    }
    /**
     * Transforms the raw text to the corresponding Object
     * @param rawText
     * @return 
     */
    public abstract Object textToObject(String rawText);
    public abstract String sqlStringValue(Object theValue);

    /**
     * @return the sqlVendorName
     */
    public String getSqlVendorName() {
        return sqlVendorName;
    }

    /**
     * @param sqlVendorName the sqlVendorName to set
     */
    public void setSqlVendorName(String sqlVendorName) {
        this.sqlVendorName = sqlVendorName;
    }
}
