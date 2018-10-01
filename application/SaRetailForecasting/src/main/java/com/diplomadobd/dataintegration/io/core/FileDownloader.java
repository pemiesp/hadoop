/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diplomadobd.dataintegration.io.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

/**
 * Class that download files from a specific location
 *
 * @author mario
 */
public class FileDownloader {

    private String inputListToDownload;
    private String outputFolder;

    public FileDownloader(String outputFolder, String inputCsvName) {
        this.outputFolder = outputFolder;
        this.inputListToDownload =  inputCsvName;
        //Verifies the output directory exists
        File outFolder = new File(this.outputFolder);
        if (!outFolder.exists()) {
            outFolder.mkdir();
        }
    }

    /**
     *
     */
    public void retrieveFiles() {
        BufferedReader br = null;
        String msg = null;
        try {
            msg = "reading the input file: " + this.inputListToDownload;
            br = TextFileHelper.getBufferedReader(this.inputListToDownload);
            //Read all lines. First the header
            if (br.ready()) {
                br.readLine();
            }
            while (br.ready()) {
                String theLine = br.readLine();
                String[] line = theLine.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                msg = "reading the line: " + theLine;
                //SSIMBOL	SNAME	SCATEGORY	SMARKET	STYPE	HISTORICURL
                
                //Build the file name
                String targetFileName = (line[0] + "_20080101-20160905.csv").replace("\"", "");
                File targetFile = new File(this.outputFolder + "/" + targetFileName);
                if (!targetFile.exists()) {
                    //Gathers the file
                    try{
                    this.retrieveTheFile(line[5].replace("\"", ""), targetFile);
                    }catch(Exception e){
                        System.out.println("No historic data for: "+line[0]);
                    }
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException("Error " + msg, e);
        }
    }

    /**
     *
     * @param theUrl
     * @param outFileName
     */
    private void retrieveTheFile(String theUrl, File outFileName) {
        InputStream in = null;
        FileOutputStream fos = null;
        String msg = "writing the file.";
        try {
            URL url = new URL(theUrl);
            in = url.openStream();
            fos = new FileOutputStream(outFileName);
            int length = -1;
            byte[] buffer = new byte[1024];// buffer for portion of data from connection
            while ((length = in.read(buffer)) > -1) {
                fos.write(buffer, 0, length);
            }

        } catch (Exception ex) {
            throw new IllegalStateException("Error " + msg+"\n"+ex.getMessage(), ex);
        } finally {
            try {
                fos.close();
                in.close();
            } catch (Exception e) {

            }
        }
    }

    public static void main(String[] args) {
        //Loads the Properties
        //Sets the dataset schema
        FileDownloader p = new FileDownloader("../data/datasets/histStock", "../data/datasets/StocksGenData.csv");
        p.retrieveFiles();
    }

}
