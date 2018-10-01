/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diplomadobd.dto;

/**
 *
 * @author mario
 */
public class SplineAccumRule {
    //The number of the week until which will be added the extraWeeks
    private int thresholdWeek ;
    //The extra weeks to consider so that there isn't too fiew data.
    private int exceptionWeeks ;
    private int cumRule;
    private int lag;
    private int totalWeeks;
    private int weeksAhead;
    private int forecastId;

    /**
     * @return the thresholdWeek
     */
    public int getThresholdWeek() {
        return thresholdWeek;
    }

    /**
     * @param thresholdWeek the thresholdWeek to set
     */
    public void setThresholdWeek(int thresholdWeek) {
        this.thresholdWeek = thresholdWeek;
    }

    /**
     * @return the exceptionWeeks
     */
    public int getExceptionWeeks() {
        return exceptionWeeks;
    }

    /**
     * @param exceptionWeeks the exceptionWeeks to set
     */
    public void setExceptionWeeks(int exceptionWeeks) {
        this.exceptionWeeks = exceptionWeeks;
    }

    /**
     * @return the cumRule
     */
    public int getCumRule() {
        return cumRule;
    }

    /**
     * @param cumRule the cumRule to set
     */
    public void setCumRule(int cumRule) {
        this.cumRule = cumRule;
    }

    /**
     * @return the lag
     */
    public int getLag() {
        return lag;
    }

    /**
     * @param lag the lag to set
     */
    public void setLag(int lag) {
        this.lag = lag;
    }

    /**
     * @return the totalWeeks
     */
    public int getTotalWeeks() {
        return totalWeeks;
    }

    /**
     * @param totalWeeks the totalWeeks to set
     */
    public void setTotalWeeks(int totalWeeks) {
        this.totalWeeks = totalWeeks;
    }

    /**
     * @return the weeksAhead
     */
    public int getWeeksAhead() {
        return weeksAhead;
    }

    /**
     * @param weeksAhead the weeksAhead to set
     */
    public void setWeeksAhead(int weeksAhead) {
        this.weeksAhead = weeksAhead;
    }

    /**
     * @return the forecastId
     */
    public int getForecastId() {
        return forecastId;
    }

    /**
     * @param forecastId the forecastId to set
     */
    public void setForecastId(int forecastId) {
        this.forecastId = forecastId;
    }

}
