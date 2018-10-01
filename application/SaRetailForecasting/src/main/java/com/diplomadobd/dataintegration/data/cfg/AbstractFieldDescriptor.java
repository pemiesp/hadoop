/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diplomadobd.dataintegration.data.cfg;

/**
 * DTO that relates the raw column name with a specific DataType and Data base name;
 * @author mg.fonseca
 */
public abstract class AbstractFieldDescriptor {
    protected String rawName;
    protected String dbName;
    protected AbstractDataType dataType;
    protected boolean allowNull;
    
    public AbstractFieldDescriptor(String rawName, String dbName, 
            AbstractDataType dtype, boolean allowNull){
        this.rawName = rawName;
        this.dbName = dbName;
        this.dataType = dtype;
        this.allowNull = allowNull;
    }
    /**
     * Implement the corresponding validation
     * @param theValueToValidate
     * @return 
     */
    public abstract boolean validateValue(Object theValueToValidate);

    /**
     * @return the rawName
     */
    public String getRawName() {
        return rawName;
    }

    /**
     * @param rawName the rawName to set
     */
    public void setRawName(String rawName) {
        this.rawName = rawName;
    }

    /**
     * @return the dbName
     */
    public String getDbName() {
        return dbName;
    }

    /**
     * @param dbName the dbName to set
     */
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    /**
     * @return the dataType
     */
    public AbstractDataType getDataType() {
        return dataType;
    }

    /**
     * @param dataType the dataType to set
     */
    public void setDataType(AbstractDataType dataType) {
        this.dataType = dataType;
    }

    /**
     * @return the allowNull
     */
    public boolean isAllowNull() {
        return allowNull;
    }

    /**
     * @param allowNull the allowNull to set
     */
    public void setAllowNull(boolean allowNull) {
        this.allowNull = allowNull;
    }
}
