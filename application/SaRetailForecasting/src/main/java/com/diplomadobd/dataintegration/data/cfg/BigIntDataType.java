/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diplomadobd.dataintegration.data.cfg;

import java.math.BigInteger;

/**
 *
 * @author mario
 */
public class BigIntDataType extends AbstractDataType{

    public BigIntDataType(String sqlVendorName) {
        super(sqlVendorName);
    }

    @Override
    public Object textToObject(String rawText) {
        return BigInteger.valueOf(Long.valueOf(rawText.replace("\"", "")));
    }

    @Override
    public String sqlStringValue(Object theValue) {
        return String.valueOf(theValue);
    }
    
}
