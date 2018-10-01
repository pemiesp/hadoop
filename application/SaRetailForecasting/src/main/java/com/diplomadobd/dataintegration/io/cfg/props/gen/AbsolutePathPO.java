/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diplomadobd.dataintegration.io.cfg.props.gen;

import com.diplomadobd.dataintegration.io.core.PropertiesFileValidator;
import java.util.Properties;

/**
 * Class that contains the general properties for absolute path information
 * about this project
 *
 * @author mario
 */
public class AbsolutePathPO implements PropertiesObject {

    private String scraperWDAPath;
    private String screenssParamAPath;
    private String dataSetAPath;
    private String sqlOutAPath;
    private String screenssAPath;
    private String logAPath;
    private String javaEnvironmentAPath;
    private String xmlPath;
    private String imagePath;
    private String pdfPath;

    public AbsolutePathPO(Properties absPathProps) {
        //Load Absolute path properties
        String propertiesName = PropertiesFileValidator.absolutePathPropertiesFileName;
        String aux = "";
        try {            
            aux = "scraperWDAPath";
            scraperWDAPath = ((String) (absPathProps.get(aux))).replaceFirst("^~", System.getProperty("user.home"));
            aux = "screenssParamAPath";
            screenssParamAPath = ((String) (absPathProps.get(aux))).replaceFirst("^~", System.getProperty("user.home"));
            aux = "dataSetAPath";
            dataSetAPath = ((String) (absPathProps.get(aux))).replaceFirst("^~", System.getProperty("user.home"));
            aux = "sqlOutAPath";
            sqlOutAPath = ((String) (absPathProps.get(aux))).replaceFirst("^~", System.getProperty("user.home"));
            aux = "screenssAPath";
            screenssAPath = ((String) (absPathProps.get(aux))).replaceFirst("^~", System.getProperty("user.home"));
            aux = "logAPath";
            logAPath = ((String) (absPathProps.get(aux))).replaceFirst("^~", System.getProperty("user.home"));
            aux = "imagePath";
            imagePath = ((String) (absPathProps.get(aux))).replaceFirst("^~", System.getProperty("user.home"));
            aux = "pdfPath";
            pdfPath = ((String) (absPathProps.get(aux))).replaceFirst("^~", System.getProperty("user.home"));
            aux = "xmlPath";
            xmlPath = ((String) (absPathProps.get(aux))).replaceFirst("^~", System.getProperty("user.home"));
            aux = "javaEnvironmentAPath";            
            javaEnvironmentAPath = ((String) (absPathProps.get(aux))).replaceFirst("^~", System.getProperty("user.home"));
            
        } catch (NullPointerException npe) {
            if (absPathProps == null) {
                throw new IllegalStateException("The properties file is null");
            }
            throw new IllegalStateException("The variable " + aux + " was expected but not found in " + propertiesName + " file.");
        }
    }

    /**
     * @return the scraperWDAPath
     */
    public String getScraperWDAPath() {
        return scraperWDAPath;
    }

    /**
     * @param scraperWDAPath the scraperWDAPath to set
     */
    public void setScraperWDAPath(String scraperWDAPath) {
        this.scraperWDAPath = scraperWDAPath;
    }

    /**
     * @return the screenssParamAPath
     */
    public String getScreenssParamAPath() {
        return screenssParamAPath;
    }

    /**
     * @param screenssParamAPath the screenssParamAPath to set
     */
    public void setScreenssParamAPath(String screenssParamAPath) {
        this.screenssParamAPath = screenssParamAPath;
    }

    /**
     * @return the dataSetAPath
     */
    public String getDataSetAPath() {
        return dataSetAPath;
    }

    /**
     * @param dataSetAPath the dataSetAPath to set
     */
    public void setDataSetAPath(String dataSetAPath) {
        this.dataSetAPath = dataSetAPath;
    }

    /**
     * @return the sqlOutAPath
     */
    public String getSqlOutAPath() {
        return sqlOutAPath;
    }

    /**
     * @param sqlOutAPath the sqlOutAPath to set
     */
    public void setSqlOutAPath(String sqlOutAPath) {
        this.sqlOutAPath = sqlOutAPath;
    }

    /**
     * @return the screenssAPath
     */
    public String getScreenssAPath() {
        return screenssAPath;
    }

    /**
     * @param screenssAPath the screenssAPath to set
     */
    public void setScreenssAPath(String screenssAPath) {
        this.screenssAPath = screenssAPath;
    }

    /**
     * @return the logAPath
     */
    public String getLogAPath() {
        return logAPath;
    }

    /**
     * @param logAPath the logAPath to set
     */
    public void setLogAPath(String logAPath) {
        this.logAPath = logAPath;
    }

    /**
     * @return the xmlPath
     */
    public String getXmlPath() {
        return xmlPath;
    }

    /**
     * @param xmlPath the xmlPath to set
     */
    public void setXmlPath(String xmlPath) {
        this.xmlPath = xmlPath;
    }

    /**
     * @return the imagePath
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * @param imagePath the imagePath to set
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * @return the pdfPath
     */
    public String getPdfPath() {
        return pdfPath;
    }

    /**
     * @param pdfPath the pdfPath to set
     */
    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }

    /**
     * @return the javaEnvironmentAPath
     */
    public String getJavaEnvironmentAPath() {
        return javaEnvironmentAPath;
    }

    /**
     * @param javaEnvironmentAPath the javaEnvironmentAPath to set
     */
    public void setJavaEnvironmentAPath(String javaEnvironmentAPath) {
        this.javaEnvironmentAPath = javaEnvironmentAPath;
    }
}
