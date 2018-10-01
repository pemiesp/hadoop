/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diplomadobd.dataintegration.data.cfg;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *  Data Type for Dates
 * @author mg.fonseca
 */
public class DateDataType extends AbstractDataType {
    protected String strInputFormat;
    protected String strOutputFormat;

    public DateDataType(String sqlVendorName) {
        super(sqlVendorName);
        //Sets the default inputFormat
        this.strInputFormat = "dd/MM/yyyy";
        //Sets the default output format
        this.strOutputFormat = "yyyy-MM-dd HH:mm:ss.SSS";
    }

    @Override
    public Object textToObject(String rawText) {
        Date theDate = null;
        SimpleDateFormat formatter = new SimpleDateFormat(this.strInputFormat);

        try {
            theDate = formatter.parse(rawText.replace("\"",""));

        } catch (Exception e) {
            //throw new IllegalStateException("Error formatting the date with pattern:\n"+
              //      this.strInputFormat, e);            
        }
        return theDate;
    }
    protected String stringValue(Object theValue){
        String date;      
        if (theValue == null) {
            date = "null";
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat(this.strOutputFormat);
            date = formatter.format(theValue);
            try {
                date = formatter.format(theValue);

            } catch (Exception e) {
                throw new IllegalStateException("Error formatting the date to string with pattern:\n"
                        + this.strOutputFormat, e);
            }
        }
        return date;
    }

    @Override
    public String sqlStringValue(Object theValue) {
        String sqlStrDate = "null";
        if (theValue != null) {
            switch (sqlVendorId) {
                case (1)://Postrgre
                    sqlStrDate = "DATE '" + this.stringValue(theValue) + "'";
                    break;
                default:
                    throw new IllegalStateException("No implementations for this vendor. "
                            + this.sqlVendorName + "\n" + this.getClass().getName());
            }
        }else{
            sqlStrDate = "NULL";
        }
        return sqlStrDate;
    }

    /**
     * @return the strInputFormat
     */
    public String getStrInputFormat() {
        return strInputFormat;
    }

    /**
     * @param strInputFormat the strInputFormat to set
     */
    public void setStrInputFormat(String strInputFormat) {
        this.strInputFormat = strInputFormat;
    }

    /**
     * @return the strOutputFormat
     */
    public String getStrOutputFormat() {
        return strOutputFormat;
    }

    /**
     * @param strOutputFormat the strOutputFormat to set
     */
    public void setStrOutputFormat(String strOutputFormat) {
        this.strOutputFormat = strOutputFormat;
    }

    
}
