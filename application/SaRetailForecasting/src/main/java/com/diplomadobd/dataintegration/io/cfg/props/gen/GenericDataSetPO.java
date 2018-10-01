/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diplomadobd.dataintegration.io.cfg.props.gen;

import com.diplomadobd.dataintegration.data.cfg.AbstractDataType;
import com.diplomadobd.dataintegration.data.cfg.DateDataType;
import com.diplomadobd.dataintegration.io.core.PropertiesFileValidator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author mario
 */
public class GenericDataSetPO implements PropertiesObject {

    private String dsTableName;
    private String inputCsvFile;
    private String sqlInserFile;
    private HashMap<String, AbstractDataType> dataTypes;
    private List<Object[][]> columnMapping;
    private int rowsToSkip;
    private String vendor;
    private String timeStampColumnName = null;
    private String hashCodeColumnName = null;
    private String hashCodeColumns = null;

    public GenericDataSetPO(Properties theProperties) {
        //Load Absolute path properties
        dataTypes = new HashMap();
        columnMapping = new LinkedList<>();
        String propertiesName = PropertiesFileValidator.genericDataSet;
        if (this instanceof GenericSodaDSetPO) {
            propertiesName = PropertiesFileValidator.genericSodaDataSet;
        }
        String aux = "";
        Object[][] vector = new Object[1][4];
        try {
            aux = "dsTableName";
            dsTableName = ((String) (theProperties.get(aux))).trim();
            aux = "inputCsvFile";
            inputCsvFile = ((String) (theProperties.get(aux))).trim();
            aux = "sqlInserFile";
            sqlInserFile = ((String) (theProperties.get(aux))).trim();
            aux = "vendor";
            vendor = ((String) (theProperties.get(aux))).trim();
            aux = "rowsToSkip";
            rowsToSkip = Integer.parseInt(((String) (theProperties.get(aux))).trim());
            //This can be null
            aux = "timeStampColumnName";
            timeStampColumnName = (String) theProperties.get(aux);
            aux = "hashCodeColumnName";
            hashCodeColumnName = (String) theProperties.get(aux);
            aux = "hashCodeColumns";
            hashCodeColumns = (String) theProperties.get(aux);
            // End this can be null           
            
            aux = "csvCol1";
            vector[0][0] = ((String) (theProperties.get(aux))).trim();
            aux = "dbCol1";
            vector[0][1] = ((String) (theProperties.get(aux))).trim();
            aux = "csvDataType1";
            vector[0][2] = this.getDataType(((String) (theProperties.get(aux))).trim(),
                    theProperties, vendor);
            aux = "csvAllowNull1";
            vector[0][3] = Boolean.valueOf(((String) (theProperties.get(aux))).trim());
            columnMapping.add(vector);
        } catch (NullPointerException npe) {
            if (theProperties == null) {
                throw new IllegalStateException("The properties file is null");
            }
            throw new IllegalStateException("The variable " + aux + " was expected but not found in " + propertiesName + " file.");
        }
        int i = 2;
        try {
            while (true) {
                vector = new Object[1][4];
                vector[0][0] = ((String) (theProperties.get("csvCol" + i))).trim();
                vector[0][1] = ((String) (theProperties.get("dbCol" + i))).trim();
                vector[0][2] = this.getDataType(((String) (theProperties.get("csvDataType" + i))).trim(),
                        theProperties, vendor);
                vector[0][3] = Boolean.valueOf(((String) (theProperties.get("csvAllowNull" + i)).toString()).trim());
                this.columnMapping.add(vector);
                i++;
            }
        } catch (NullPointerException npe) {
            //Means we have finished loading the columns of the dataset
        }
    }

    /**
     * Returns the corresponding datatype
     *
     * @param value
     * @param theProps
     * @param vendorDb
     * @return
     */
    private AbstractDataType getDataType(String value, Properties theProps, String vendorDb) {
        AbstractDataType adt = this.getDataTypes().get(value);
        String aux = value;
        String className;
        try {
            if (adt == null) {
                className = theProps.getProperty(aux).toString();
                adt = (AbstractDataType) Class.forName(className).getConstructors()[0].newInstance(new Object[]{vendorDb});
                if (adt instanceof DateDataType) {
                    int length = value.length();
                    aux = "dataTypeInputFormat" + value.substring(length - 1, length);
                    ((DateDataType) adt).setStrInputFormat(theProps.getProperty(aux).toString());
                }
                this.getDataTypes().put(value, adt);
            }

        } catch (NullPointerException npe) {
            throw new IllegalStateException("The variable" + aux + " was expected but not found in " + PropertiesFileValidator.genericDataSet + " file.");
        } catch (Exception ex) {
            throw new IllegalStateException("Error loading data types", ex);
        }
        return adt;
    }

    /**
     * @return the dsTableName
     */
    public String getDsTableName() {
        return dsTableName;
    }

    /**
     * @param dsTableName the dsTableName to set
     */
    public void setDsTableName(String dsTableName) {
        this.dsTableName = dsTableName;
    }

    /**
     * @return the inputCsvFile
     */
    public String getInputCsvFile() {
        return inputCsvFile;
    }

    /**
     * @param inputCsvFile the inputCsvFile to set
     */
    public void setInputCsvFile(String inputCsvFile) {
        this.inputCsvFile = inputCsvFile;
    }

    /**
     * @return the sqlInserFile
     */
    public String getSqlInserFile() {
        return sqlInserFile;
    }

    /**
     * @param sqlInserFile the sqlInserFile to set
     */
    public void setSqlInserFile(String sqlInserFile) {
        this.sqlInserFile = sqlInserFile;
    }

    /**
     * @return the dataTypes
     */
    public HashMap<String, AbstractDataType> getDataTypes() {
        return dataTypes;
    }

    /**
     * @param dataTypes the dataTypes to set
     */
    public void setDataTypes(HashMap<String, AbstractDataType> dataTypes) {
        this.dataTypes = dataTypes;
    }

    /**
     * @return the columnMapping
     */
    public List<Object[][]> getColumnMapping() {
        return columnMapping;
    }

    /**
     * @param columnMapping the columnMapping to set
     */
    public void setColumnMapping(List<Object[][]> columnMapping) {
        this.columnMapping = columnMapping;
    }

    /**
     * @return the rowsToSkip
     */
    public int getRowsToSkip() {
        return rowsToSkip;
    }

    /**
     * @param rowsToSkip the rowsToSkip to set
     */
    public void setRowsToSkip(int rowsToSkip) {
        this.rowsToSkip = rowsToSkip;
    }

    /**
     * @return the vendor
     */
    public String getVendor() {
        return vendor;
    }

    /**
     * @param vendor the vendor to set
     */
    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    /**
     * @return the timeStampColumnName
     */
    public String getTimeStampColumnName() {
        return timeStampColumnName;
    }

    /**
     * @param timeStampColumnName the timeStampColumnName to set
     */
    public void setTimeStampColumnName(String timeStampColumnName) {
        this.timeStampColumnName = timeStampColumnName;
    }

    /**
     * @return the hashCodeColumnName
     */
    public String getHashCodeColumnName() {
        return hashCodeColumnName;
    }

    /**
     * @param hashCodeColumnName the hashCodeColumnName to set
     */
    public void setHashCodeColumnName(String hashCodeColumnName) {
        this.hashCodeColumnName = hashCodeColumnName;
    }

    /**
     * @return the hashCodeColumns
     */
    public String getHashCodeColumns() {
        return hashCodeColumns;
    }

    /**
     * @param hashCodeColumns the hashCodeColumns to set
     */
    public void setHashCodeColumns(String hashCodeColumns) {
        this.hashCodeColumns = hashCodeColumns;
    }
}
