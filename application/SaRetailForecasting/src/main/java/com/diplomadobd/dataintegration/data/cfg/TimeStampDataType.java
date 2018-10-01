/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diplomadobd.dataintegration.data.cfg;

/**
 *
 * @author mg.fonseca
 */
public class TimeStampDataType extends DateDataType {

    public TimeStampDataType(String sqlVendorName) {
        super(sqlVendorName);
    }

    @Override
    public String sqlStringValue(Object theValue) {
        String sqlStrDate = "null";
        if (theValue != null) {
            switch (sqlVendorId) {
                case (1)://Postrgress
                    sqlStrDate = "TIMESTAMP '" + this.stringValue(theValue) + "'";
                    break;
                default:
                    throw new IllegalStateException("No implementations for this vendor. "
                            + this.sqlVendorName + "\n" + this.getClass().getName());
            }
        }
        return sqlStrDate;
    }

}
