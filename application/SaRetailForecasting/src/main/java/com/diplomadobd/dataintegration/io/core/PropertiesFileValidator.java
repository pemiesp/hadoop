/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diplomadobd.dataintegration.io.core;

import com.diplomadobd.dataintegration.io.cfg.props.gen.PropertiesObject;
import java.io.File;

/**
 *
 * @author mario
 */
public class PropertiesFileValidator {

    public static final String dataBasePropertiesFileName = "DataBase.properties";
    public static final String absolutePathPropertiesFileName = "AbsolutePath.properties";
    public static final String genericDataSet = "DSGeneric.properties";
    public static final String genericSodaDataSet = "SodaDSGeneric.properties";
    public static final String contractorsScraperIncr = "ContractorsScraperIncrementalJob.properties";
    public static final String genericSSIUJob = "SSIUGenericJob.properties";
    public static final String mailPropertiesFileName = "MailNotifier.properties";
    public static final String ocrPdfDocument = "PdfOcrDocument.xml";
    public static final String abbyyConf = "Abbyy.properties";
    public static final String xmlToCsv = "XmlToCsv.properties";

    /**
     *
     * @param targetFileName
     * @param validFileName
     * @return A specific PropertiesObject
     */
    public static PropertiesObject validatePropertiesFileName(String targetFileName, String validFileName) {
        String aux;
        PropertiesObject thePropertiesObject = null;
        Object theProps;
        String className = "not defined jet";
        switch (validFileName) {
            case dataBasePropertiesFileName:
                aux = dataBasePropertiesFileName;
                className = "com.syngrotima.dataintegration.io.cfg.props.gen.DataBasePO";
                break;
            case absolutePathPropertiesFileName:
                aux = absolutePathPropertiesFileName;
                className = "com.syngrotima.dataintegration.io.cfg.props.gen.AbsolutePathPO";
                break;
            case contractorsScraperIncr:
                aux = contractorsScraperIncr;
                className = "com.syngrotima.dataintegration.io.cfg.props.custom.ContractorsSSIncPO";
                break;
            case genericDataSet:
                aux = genericDataSet;
                className = "com.syngrotima.dataintegration.io.cfg.props.gen.GenericDataSetPO";
                break;
            case genericSodaDataSet:
                aux = genericDataSet;
                className = "com.syngrotima.dataintegration.io.cfg.props.gen.GenericSodaDSetPO";
                break;
            case mailPropertiesFileName:
                aux = mailPropertiesFileName;
                className = "com.syngrotima.dataintegration.io.cfg.props.gen.MailNotifierPO";
                break;
            case genericSSIUJob:
                aux = genericSSIUJob;
                className = "com.syngrotima.dataintegration.io.cfg.props.custom.SSIU_GenericPO";
                break;
            case ocrPdfDocument:
                aux = ocrPdfDocument;
                className = "com.syngrotima.dataintegration.io.cfg.props.gen.OcrPdfDocumentPO";
                break;
            case abbyyConf:
                aux = abbyyConf;
                className = "com.syngrotima.dataintegration.io.cfg.props.custom.AbbyyPO";
                break;
            case xmlToCsv:
                aux = xmlToCsv;
                className = "com.syngrotima.dataintegration.io.cfg.props.custom.XmlToCsvPO";
                break;

            default:
                throw new IllegalStateException("There is not implemented validation for this properties file: " + validFileName);
        }
        File targetFile = new File(targetFileName);
        //Checks if it's name is equal, except for the custom application properties file
        String theName = targetFile.getName();
        //Checks if the file exists
        if (!targetFile.exists()) {
            throw new IllegalStateException("The properties file: " + theName + " does not exist. Please verify the path.");
        }
        if (aux.contains("xml")) {
            theProps = XMLHelper.getXmlDocument(targetFileName);
        } else {
            theProps = TextFileHelper.getProperties(targetFileName);
        }
        try {
            thePropertiesObject = (PropertiesObject) Class.forName(className).getConstructors()[0].newInstance(new Object[]{theProps});
        } catch (Exception ex) {
            Throwable ise = ex.getCause();
            if (ise instanceof IllegalStateException) {
                throw (IllegalStateException) new IllegalStateException("Error loading " + targetFileName + "\n", ise);
            }
            throw new IllegalStateException("Error getting the " + validFileName + ": " + ex.getMessage(), ex);
        }
        return thePropertiesObject;
    }

}
