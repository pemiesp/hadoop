/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diplomadobd.retailda.main;

import java.io.File;
import java.io.FileInputStream;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.CSVLoader;
import weka.core.converters.CSVSaver;

/**
 *
 * @author mario
 */
public class GenerateRecall {

    public static void main(String[] args) {
        int idcum = -1;
        String resultslFolderName = "../results/";
        String recallFolderName = "../datasets/abt/";
        String modelFolderName = "../models/";
        try {
            File modelFolder = new File(modelFolderName);
            File[] models = modelFolder.listFiles();
            for (File model : models) {
                String fileName = model.getName().split("_")[0];
                int theIdcum = Integer.parseInt(fileName.split("-")[1]);
                boolean flag = true;
                if (idcum != -1 && idcum != theIdcum) {
                    flag = false;
                }
                //Filters by idcum when idcum != -1
                if (flag) {

                    File recallFile = new File(recallFolderName + fileName + ".csv");
                    File resultFile = new File(resultslFolderName + fileName + "_Results.csv");
                    //Load the model
                    MultilayerPerceptron mlp = (MultilayerPerceptron) SerializationHelper.
                            read(new FileInputStream(model));
                    //Load the data to recall
                    CSVLoader source = new CSVLoader();
                    source.setFile(recallFile);
                    Instances unlabeled = source.getDataSet();
                    unlabeled.setClassIndex(unlabeled.numAttributes() - 1);
                    // create copy for the results
                    Instances labeled = new Instances(unlabeled);
                    // label instances
                    for (int i = 0; i < unlabeled.numInstances(); i++) {
                        double clsLabel = mlp.classifyInstance(unlabeled.instance(i));
                        labeled.instance(i).setClassValue(clsLabel);
                    }
                    CSVSaver csvs = new CSVSaver();
                    csvs.setFile(resultFile);
                    csvs.setInstances(labeled);
                    csvs.writeBatch();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
