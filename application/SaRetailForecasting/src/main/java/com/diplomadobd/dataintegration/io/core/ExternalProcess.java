/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diplomadobd.dataintegration.io.core;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Class made to execute java classes main from external process
 *
 * @author mario
 */
public class ExternalProcess {

    private ExternalProcess() {
    }

    public static void exec(Class klass, String[] mainArgs) {
        List<String> theCommandAsList = new LinkedList();
        try {
            String javaHome = System.getProperty("java.home");
            String javaBin = javaHome
                    + File.separator + "bin"
                    + File.separator + "java";
            String classpath = System.getProperty("java.class.path");
            String className = klass.getCanonicalName();

            theCommandAsList.add(javaBin);
            theCommandAsList.add("-cp");
            theCommandAsList.add(classpath);
            theCommandAsList.add(className);
            for (int i = 0; i < mainArgs.length; i++) {
                String mainArg = mainArgs[i];
                theCommandAsList.add(mainArg);
            }
            //ProcessBuilder builder = new ProcessBuilder(
            //        javaBin, "-cp", classpath, className);
            ProcessBuilder builder = new ProcessBuilder(theCommandAsList);

            Process process = builder.start();
        } catch (Exception ex) {
            throw new IllegalStateException("Error executing the class " + klass.getName() + " from an external process.", ex);
        }
    }
}
