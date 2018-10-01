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
public class MailNotifierPO implements PropertiesObject {

    private String mailHost;
    private String socketPort;
    private String socketFactoryClass;
    private String smtpAuth;
    private String mailPort;
    private String senderAccount;
    private String senderPwd;
    private String recipients;
    
    public MailNotifierPO(Properties mailProps){
        String propertiesName = PropertiesFileValidator.dataBasePropertiesFileName;
        String aux = "";
        try {
            aux = "mailHost";
            mailHost = ((String) (mailProps.get(aux))).toString();
            aux = "socketPort";
            socketPort = ((String) (mailProps.get(aux))).toString();
            aux = "socketFactoryClass";
            socketFactoryClass = ((String) (mailProps.get(aux))).toString();
            aux = "smtpAuth";
            smtpAuth = ((String) (mailProps.get(aux))).toString();
            aux = "mailPort";
            mailPort = ((String) (mailProps.get(aux))).toString();
            aux = "senderAccount";
            senderAccount = ((String) (mailProps.get(aux))).toString();
            aux = "senderPwd";
            senderPwd = ((String) (mailProps.get(aux))).toString();
            aux = "recipients";
            recipients = ((String) (mailProps.get(aux))).toString();
            
            
        } catch (NullPointerException npe) {
            if (mailProps == null) {
                throw new IllegalStateException("The properties "+propertiesName+" file is null");
            }
            throw new IllegalStateException("The variable " + aux + " was expected but not found in " + propertiesName + " file.");
        }
    }

    /**
     * @return the mailHost
     */
    public String getMailHost() {
        return mailHost;
    }

    /**
     * @param mailHost the mailHost to set
     */
    public void setMailHost(String mailHost) {
        this.mailHost = mailHost;
    }

    /**
     * @return the socketPort
     */
    public String getSocketPort() {
        return socketPort;
    }

    /**
     * @param socketPort the socketPort to set
     */
    public void setSocketPort(String socketPort) {
        this.socketPort = socketPort;
    }

    /**
     * @return the socketFactoryClass
     */
    public String getSocketFactoryClass() {
        return socketFactoryClass;
    }

    /**
     * @param socketFactoryClass the socketFactoryClass to set
     */
    public void setSocketFactoryClass(String socketFactoryClass) {
        this.socketFactoryClass = socketFactoryClass;
    }

    /**
     * @return the smtpAuth
     */
    public String getSmtpAuth() {
        return smtpAuth;
    }

    /**
     * @param smtpAuth the smtpAuth to set
     */
    public void setSmtpAuth(String smtpAuth) {
        this.smtpAuth = smtpAuth;
    }

    /**
     * @return the senderAccount
     */
    public String getSenderAccount() {
        return senderAccount;
    }

    /**
     * @param senderAccount the senderAccount to set
     */
    public void setSenderAccount(String senderAccount) {
        this.senderAccount = senderAccount;
    }

    /**
     * @return the senderPwd
     */
    public String getSenderPwd() {
        return senderPwd;
    }

    /**
     * @param senderPwd the senderPwd to set
     */
    public void setSenderPwd(String senderPwd) {
        this.senderPwd = senderPwd;
    }

    /**
     * @return the recipients
     */
    public String getRecipients() {
        return recipients;
    }

    /**
     * @param recipients the recipients to set
     */
    public void setRecipients(String recipients) {
        this.recipients = recipients;
    }

    /**
     * @return the mailPort
     */
    public String getMailPort() {
        return mailPort;
    }

    /**
     * @param mailPort the mailPort to set
     */
    public void setMailPort(String mailPort) {
        this.mailPort = mailPort;
    }
    
}
