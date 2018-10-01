/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diplomadobd.dataintegration.data.cfg;

/**
 * General implementation for the field descriptor. The validation always
 * returns true, it means no validation is required.
 * @author mg.fonseca
 */
public class GeneralFieldDescriptor extends AbstractFieldDescriptor {

    public GeneralFieldDescriptor(String rawName, String dbName, AbstractDataType dtype, boolean allowNull) {
        super(rawName, dbName, dtype, allowNull);
    }

    @Override
    public boolean validateValue(Object theValueToValidate) {
        //No validation is required
        return true;
    }
    
}
