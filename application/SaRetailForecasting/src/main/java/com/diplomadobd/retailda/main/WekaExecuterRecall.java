/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diplomadobd.retailda.main;

import java.io.File;
import java.io.FileInputStream;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.CSVLoader;
import weka.core.converters.CSVSaver;

/**
 *
 * @author mario
 */
public class WekaExecuterRecall {

    public static void main(String[] args) {
        try {
            MultilayerPerceptron mlp = (MultilayerPerceptron) SerializationHelper.
                    read(new FileInputStream("../models/MLP_NL012_1_1_MultilayerPerceptron.model"));
            // // load unlabeled data
//            Instances unlabeled = new Instances(
//                    new BufferedReader(
//                            new FileReader("../datasets/abt/NL012_ABT_noclass.arff")));
//            // set class attribute
            CSVLoader source = new CSVLoader();
            source.setFile(new File("../datasets/abt/NL012_ABT_noclass.csv"));
            
            Instances unlabeled = source.getDataSet();
            unlabeled.setClassIndex(unlabeled.numAttributes() - 1);
// create copy
            Instances labeled = new Instances(unlabeled);
        

            // label instances
            for (int i = 0; i < unlabeled.numInstances(); i++) {
                double clsLabel = mlp.classifyInstance(unlabeled.instance(i));
                labeled.instance(i).setClassValue(clsLabel);
            }
            String resultFile = "../results/NL012_ABT_Results.csv";
            CSVSaver csvs = new CSVSaver();
            csvs.setFile(new File(resultFile));
            csvs.setInstances(labeled);
            //BufferedWriter bw = csvs.getWriter();//TextFileHelper.getBufferedWriter(resultFile);
            //bw.write(labeled.toString());
            //bw.close();
            
            csvs.writeBatch();
            
//            csvs.getWriter().close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
