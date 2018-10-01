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
public class HeaderDataType extends AbstractDataType {

    public HeaderDataType(String sqlVendorName) {
        super(sqlVendorName);
    }

    @Override
    public Object textToObject(String rawText) {
        String aCleanString;
        aCleanString = rawText.replace("\"", "").trim();
        return aCleanString;
    }

    @Override
    public String sqlStringValue(Object theValue) {
        String theStringValue = (String) theValue;
        String theReturnValue = null;
        switch (sqlVendorId) {
            case (1):
                theReturnValue = "'" + theStringValue + "'";
                break;
        }
        return theReturnValue;
    }

}
