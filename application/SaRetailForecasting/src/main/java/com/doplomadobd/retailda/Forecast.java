/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doplomadobd.retailda;

import java.io.FileInputStream;
import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
import com.diplomadobd.dao.FandeliDAO;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;

/**
 * Class that does the different kinds of forecast
 *
 * @author mario
 */
public class Forecast {

    private Connection conn;
    private String modelFolder;

    public Forecast(Connection conn, String modelFolder) {
        this.conn = conn;
        this.modelFolder = modelFolder;
    }

    public void generateTraditionalForecast(int baseYear) {
        try {
            //First do the query
            List<Object[]> recallRecords = FandeliDAO.getLatestForecastIndexes(this.conn);
            //For each compute the abt
            for (Iterator<Object[]> iterator = recallRecords.iterator(); iterator.hasNext();) {
                Object[] row = iterator.next();
                int idcum = ((Integer) row[1]);
                String sku = (String) row[0];
                String fileName = sku + "-" + idcum + "_1_1_MultilayerPerceptron.model";
                int latestIndex = (Integer) row[2];
                ABT_Generation abtGen = new ABT_Generation(idcum, conn);
                List<List<double[]>> theRecords = abtGen.buildForecastAbtRows(sku, idcum, latestIndex);
                List<double[]> cumVarRecords = theRecords.get(1);//The variation is the second element
                //Load de mlp model
                MultilayerPerceptron mlp = (MultilayerPerceptron) SerializationHelper.
                        read(new FileInputStream(modelFolder + fileName));
                //Generate predictions
                FastVector attributes = new FastVector(4);
                attributes.add(new Attribute("vcdl32"));
                attributes.add(new Attribute("vcdl21"));
                attributes.add(new Attribute("vcdl10"));
                attributes.add(new Attribute("vcdlforeast"));
                //Create the dataset
                Instances theRecallDataSet = new Instances("recall", attributes, cumVarRecords.size());
                theRecallDataSet.setClassIndex(theRecallDataSet.numAttributes() - 1);
                //Do the prediction              
                for (int i = 0; i < cumVarRecords.size(); i++) {
                    double[] cumVarRecord = cumVarRecords.get(i);
                    Instance theInstance = new DenseInstance(4);
                    theInstance.setValue((Attribute) attributes.elementAt(0), cumVarRecord[0]);
                    theInstance.setValue((Attribute) attributes.elementAt(1), cumVarRecord[1]);
                    theInstance.setValue((Attribute) attributes.elementAt(2), cumVarRecord[2]);
                    theInstance.setValue((Attribute) attributes.elementAt(3), 0);//This is the value to forecast
                    theRecallDataSet.add(theInstance);
                    //Executes the model
                    double forecast = mlp.classifyInstance(theRecallDataSet.instance(i));
                    //Stores the value in the array
                    cumVarRecord[3] = forecast;
                }
                //Now we update the data base with both forecast abt tables.
                FandeliDAO.insertTraditionalForecastAbtRecords(sku, idcum, theRecords, latestIndex + 1, conn);
            }
            System.out.println("OK");
            //End
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

}
