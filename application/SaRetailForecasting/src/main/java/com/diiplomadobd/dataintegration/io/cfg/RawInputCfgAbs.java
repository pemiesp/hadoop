package com.diiplomadobd.dataintegration.io.cfg;

/**
 * Describes the features of the input text file that contains
 * information to load.
 * @author mg.fonseca
 */
public abstract class RawInputCfgAbs {
    /**
     * Describes the regular expression for the column delimiter string 
     * for this configuration. Through this delimiter every 
     * line will break into tokens.
     */
    protected String delimiter;
    /**
     * Describes the limit for the delimiter. Check The String.split method.
     */
    protected int theDelimiterLimit;
    /**
     * Describes the name of the input file.
     */
    protected String inputFileName;
    /**
     * Describe the number of lines this file would skip from the beginning.
     * It is understood that the next line contains the headers of this dataset.
     */
    protected int linesToSkip;
    /**
     * Optional String that contain the columns names for this dataset 
     * separated by the same delimiter as "delimiter".
     * If present it can be used to check if there have been changes to the
     * raw data set header, or if the data set doesn't present header this line
     * would replace it.
     */    
    protected String headerLine;

    /**
     * @return the delimiter
     */
    public String getDelimiter() {
        return delimiter;
    }

    /**
     * @param delimiter the delimiter to set
     */
    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    /**
     * @return the theDelimiterLimit
     */
    public int getTheDelimiterLimit() {
        return theDelimiterLimit;
    }

    /**
     * @param theDelimiterLimit the theDelimiterLimit to set
     */
    public void setTheDelimiterLimit(int theDelimiterLimit) {
        this.theDelimiterLimit = theDelimiterLimit;
    }

    /**
     * @return the inputFileName
     */
    public String getInputFileName() {
        return inputFileName;
    }

    /**
     * @param inputFileName the inputFileName to set
     */
    public void setInputFileName(String inputFileName) {
        this.inputFileName = inputFileName;
    }

    /**
     * @return the linesToSkip
     */
    public int getLinesToSkip() {
        return linesToSkip;
    }

    /**
     * @param linesToSkip the linesToSkip to set
     */
    public void setLinesToSkip(int linesToSkip) {
        this.linesToSkip = linesToSkip;
    }

    /**
     * @return the headerLine
     */
    public String getHeaderLine() {
        return headerLine;
    }

    /**
     * @param headerLine the headerLine to set
     */
    public void setHeaderLine(String headerLine) {
        this.headerLine = headerLine;
    }

    
    
}
