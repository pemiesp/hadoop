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
public class DataBasePO implements PropertiesObject{

    private String dbPwd;
    private String dbPort;
    private String dbUser;
    private String dbName;
    private String dbHost;
    private String sodaUser;
    private String sodaPwd;
    private String sodaToken;

    public DataBasePO(Properties dbProperties) {
        //Load Absolute path properties
        String propertiesName = PropertiesFileValidator.dataBasePropertiesFileName;
        String aux = "";
        try {
            aux = "dbPwd";
            dbPwd = ((String) (dbProperties.get(aux))).toString();
            aux = "dbUser";
            dbUser = ((String) (dbProperties.get(aux))).toString();
            aux = "dbName";
            dbName = ((String) (dbProperties.get(aux))).toString();
            aux = "dbHost";
            dbHost = ((String) (dbProperties.get(aux))).toString();
            aux = "dbPort";
            dbPort = ((String) (dbProperties.get(aux))).toString();
            aux = "sodaUser";
            sodaUser = ((String) (dbProperties.get(aux))).toString();
            aux = "sodaPwd";
            sodaPwd = ((String) (dbProperties.get(aux))).toString();
            aux = "sodaToken";
            sodaToken = ((String) (dbProperties.get(aux))).toString();
            
            
        } catch (NullPointerException npe) {
            if (dbProperties == null) {
                throw new IllegalStateException("The properties file is null");
            }
            throw new IllegalStateException("The variable " + aux + " was expected but not found in " + propertiesName + " file.");
        }
    }

    /**
     * @return the dbPwd
     */
    public String getDbPwd() {
        return dbPwd;
    }

    /**
     * @param dbPwd the dbPwd to set
     */
    public void setDbPwd(String dbPwd) {
        this.dbPwd = dbPwd;
    }

    /**
     * @return the dbUser
     */
    public String getDbUser() {
        return dbUser;
    }

    /**
     * @param dbUser the dbUser to set
     */
    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
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
     * @return the dbHost
     */
    public String getDbHost() {
        return dbHost;
    }

    /**
     * @param dbHost the dbHost to set
     */
    public void setDbHost(String dbHost) {
        this.dbHost = dbHost;
    }

    /**
     * @return the sodaUser
     */
    public String getSodaUser() {
        return sodaUser;
    }

    /**
     * @param sodaUser the sodaUser to set
     */
    public void setSodaUser(String sodaUser) {
        this.sodaUser = sodaUser;
    }

    /**
     * @return the sodaPwd
     */
    public String getSodaPwd() {
        return sodaPwd;
    }

    /**
     * @param sodaPwd the sodaPwd to set
     */
    public void setSodaPwd(String sodaPwd) {
        this.sodaPwd = sodaPwd;
    }

    /**
     * @return the sodaToken
     */
    public String getSodaToken() {
        return sodaToken;
    }

    /**
     * @param sodaToken the sodaToken to set
     */
    public void setSodaToken(String sodaToken) {
        this.sodaToken = sodaToken;
    }

    /**
     * @return the dbPort
     */
    public String getDbPort() {
        return dbPort;
    }

    /**
     * @param dbPort the dbPort to set
     */
    public void setDbPort(String dbPort) {
        this.dbPort = dbPort;
    }
}
