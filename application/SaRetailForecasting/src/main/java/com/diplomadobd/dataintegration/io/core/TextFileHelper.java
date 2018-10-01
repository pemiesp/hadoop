package com.diplomadobd.dataintegration.io.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;

/**
 * Helper Class that simplifies the file connection either to read or to write
 * text files.
 *
 * @author mg.fonseca
 */
public class TextFileHelper {

    /**
     * Gets a BufferedReader to read text lines from the specified file name.
     *
     * @param fileName The name of the text file
     * @return The buffer to read lines.
     * @throws IllegalStateException If an Input error occurs.
     */
    public static BufferedReader getBufferedReader(String fileName) throws IllegalStateException {
        BufferedReader br = null;
        try {
            File theFile = new File(fileName);
            if (!theFile.exists()) {
                throw new IllegalStateException("The file " + theFile.getPath() + " does not exist.\nCan't deliver buffer to read.");
            }
            InputStream fis = new FileInputStream(fileName);
            InputStreamReader isr = new InputStreamReader(fis/*, can configure encoding here*/);
            br = new BufferedReader(isr);

        } catch (IOException e) {
            throw new IllegalStateException("Cant't deliver buffer to read. " + fileName, e);
        }
        return br;

    }

    /**
     * Delivers an output buffer to write lines to a text file
     *
     * @param fileName the file name
     * @return The output buffer
     * @throws IllegalStateException If an output error occurs.
     */
    public static BufferedWriter getBufferedWriter(String fileName) throws IllegalStateException {
        BufferedWriter bw = null;
        try {
            File theFile = new File(fileName);
            File parentDir = theFile.getParentFile();
            if(!parentDir.exists()){
                parentDir.mkdirs();
            }
            if (!theFile.exists()) {
                theFile.createNewFile();
            }
            OutputStream fos = new FileOutputStream(fileName);
            OutputStreamWriter osw = new OutputStreamWriter(fos/*, can configure encoding here*/);
            bw = new BufferedWriter(osw);
        } catch (IOException e) {
            throw new IllegalStateException("Error delibering output buffer: " + fileName, e);
        }
        return bw;
    }

    /**
     * Resets already existing files to avoid inconsistencies
     *
     * @param fileName The file to reset
     */
    public static void deleteFile(String fileName) {
        File aFile = new File(fileName);
        if (aFile.exists()) {
            aFile.delete();
        }
    }
    /**
     * Loads a properties file into memory
     * @param propertiesFile
     * @return 
     */
    public static Properties getProperties(String propertiesFile) {
        Properties p = new Properties();
        try {
            Reader is = getBufferedReader(propertiesFile);
            p.load(is);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return p;
    }
    /**
     * Prints a Property object into a file
     * @param propertiesFile 
     */
        public static void writeProperties(Properties theProperties, String propertiesFile) {
        try {
            Writer w = getBufferedWriter(propertiesFile);
            theProperties.store(w, null);
            w.close();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
