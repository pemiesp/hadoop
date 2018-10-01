/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diplomadobd.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author mario
 */
public class Dao {

    private String host = "localhost";
    private String port = "5432";
    private String dbName = "retailforecast";
    private String dbUser = "dipbduser";
    private String dbPwd = "dipbduser";
    
    public Dao(){
        
    }
    
    public Dao(String host, String dbName, String dbUser, String dbPwd, String port){
        this.host = host;
        this.dbName = dbName;
        this.dbUser = dbUser;
        this.dbPwd = dbPwd;
        this.port = port;
    }

    public Connection getDbConnection() {
        Connection theConn = null;
        String theConnUrl = "jdbc:postgresql://" + this.host + ":" + this.port + "/" + dbName;
        Properties props = new Properties();
        props.setProperty("user", this.dbUser);
        props.setProperty("password", this.dbPwd);
        try {
            theConn = DriverManager.getConnection(theConnUrl, props);
        } catch (SQLException e) {
            throw new IllegalStateException("Error geting the DB connection. Server: " + this.host
                    + "Port: " + this.port, e);
        }
        return theConn;
    }
}
