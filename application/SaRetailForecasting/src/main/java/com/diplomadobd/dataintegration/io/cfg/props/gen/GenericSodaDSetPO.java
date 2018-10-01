/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diplomadobd.dataintegration.io.cfg.props.gen;

import com.diplomadobd.dataintegration.io.core.PropertiesFileValidator;
import java.util.Properties;

/**
 *
 * @author mario
 */
public class GenericSodaDSetPO extends GenericDataSetPO {

    public static final String dateFilterConfiguration = "date";
    public static final String genericFilterConfiguration = "generic";

    private String sodaBaseUrl;
    private String sodaDataSetId;
    private String filterColumn;
    private String sodaFilterColumn;
    private String filterConfiguration;
    private String sodaSelectColumns;
    private String sodaDateOutFormat;

    public GenericSodaDSetPO(Properties theProperties) {
        super(theProperties);
        //now continues loading the particular variables
        String aux = null;
        String propertiesName = PropertiesFileValidator.genericSodaDataSet;
        try {
            aux = "sodaBaseUrl";
            sodaBaseUrl = ((String) (theProperties.get(aux))).toString();
            aux = "sodaDataSetId";
            sodaDataSetId = ((String) (theProperties.get(aux))).toString();
            aux = "filterColumn";
            filterColumn = ((String) (theProperties.get(aux))).toString();
            aux = "sodaFilterColumn";
            sodaFilterColumn = ((String) (theProperties.get(aux))).toString();
            aux = "filterConfiguration";
            filterConfiguration = ((String) (theProperties.get(aux))).toString();
            aux = "sodaSelectColumns";
            sodaSelectColumns = ((String) (theProperties.get(aux))).toString();
            aux = "sodaDateOutFormat";
            sodaDateOutFormat = ((String) (theProperties.get(aux))).toString();
        } catch (NullPointerException npe) {
            if (theProperties == null) {
                throw new IllegalStateException("The properties file is null");
            }
            throw new IllegalStateException("The variable " + aux + " was expected but not found in " + propertiesName + " file.");
        }

    }

    /**
     * @return the sodaBaseUrl
     */
    public String getSodaBaseUrl() {
        return sodaBaseUrl;
    }

    /**
     * @param sodaBaseUrl the sodaBaseUrl to set
     */
    public void setSodaBaseUrl(String sodaBaseUrl) {
        this.sodaBaseUrl = sodaBaseUrl;
    }

    /**
     * @return the sodaDataSetId
     */
    public String getSodaDataSetId() {
        return sodaDataSetId;
    }

    /**
     * @param sodaDataSetId the sodaDataSetId to set
     */
    public void setSodaDataSetId(String sodaDataSetId) {
        this.sodaDataSetId = sodaDataSetId;
    }

    /**
     * @return the dateFilterColumn
     */
    public String getFilterColumn() {
        return filterColumn;
    }

    /**
     * @param dateFilterColumn the dateFilterColumn to set
     */
    public void setFilterColumn(String dateFilterColumn) {
        this.filterColumn = dateFilterColumn;
    }

    /**
     * @return the sodaFilterColumn
     */
    public String getSodaFilterColumn() {
        return sodaFilterColumn;
    }

    /**
     * @param sodaFilterColumn the sodaFilterColumn to set
     */
    public void setSodaFilterColumn(String sodaFilterColumn) {
        this.sodaFilterColumn = sodaFilterColumn;
    }

    /**
     * @return the filterConfiguration
     */
    public String getFilterConfiguration() {
        return filterConfiguration;
    }

    /**
     * @param filterConfiguration the filterConfiguration to set
     */
    public void setFilterConfiguration(String filterConfiguration) {
        this.filterConfiguration = filterConfiguration;
    }

    /**
     * @return the sodaSelectColumns
     */
    public String getSodaSelectColumns() {
        return sodaSelectColumns;
    }

    /**
     * @param sodaSelectColumns the sodaSelectColumns to set
     */
    public void setSodaSelectColumns(String sodaSelectColumns) {
        this.sodaSelectColumns = sodaSelectColumns;
    }

    /**
     * @return the sodaDateOutFormat
     */
    public String getSodaDateOutFormat() {
        return sodaDateOutFormat;
    }

    /**
     * @param sodaDateOutFormat the sodaDateOutFormat to set
     */
    public void setSodaDateOutFormat(String sodaDateOutFormat) {
        this.sodaDateOutFormat = sodaDateOutFormat;
    }

}
