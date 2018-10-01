/*
 * Copyright (c) 2018 Mario Fonseca
 */
package com.diplomadobd.retailforecasting.map;

import java.io.IOException;
import java.text.DecimalFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * Creates the accumulative demand.
 *
 *
 * @author mario
 */
public class AccDemandMap extends Mapper<Object, Text, Text, Text> {

    //Regular expression from hdfs comma separated.
    private final String csvRegex = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";
    //Represents a SKU of a product
    private Text sku = new Text();
    //Represents a string with the next columns: date, demand.
    private Text dateDemand = new Text();

    /**
     * Implementation of the map operations. Here we will receive as parameter
     * one csv line and as output we will write a SKU(key) with their
     * corresponding date and demand value.
     *
     * @param key The key of the row. We will ignore it.
     * @param value The csv row.
     * @param context The hadoop context.
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public void map(Object key, Text value, Context context
    ) throws IOException, InterruptedException {
        Configuration conf;
        conf = context.getConfiguration();
        String sep = conf.get("mapreduce.output.textoutputformat.separator");
        String firstCols = conf.get("first.key.columns");
        String[] tokens = value.toString().split(csvRegex, -1);
        String tempVar = null;
        //Discard the file header.
        try {
            //Replaces the "-" for zero and validates it is a expected row.
            boolean isTheHeader = false;
            Long demand=null;
            try {
                tempVar = tokens[2]
                        .replace("\"", "").replace("'", "''").replace("\\", "\\\\").trim()
                        .replace("-", "0").replace(",", "");
               demand = Long.valueOf(tempVar);
            } catch (NumberFormatException ex) {
                //It is the header
                isTheHeader = true;
            }
            if (!isTheHeader) {
                //Sets the sku
                sku.set(tokens[0]);
                DecimalFormat myFormatter = new DecimalFormat("###.##");
                String demandAsString = myFormatter.format(demand);
                dateDemand.set(tokens[1]+","+demandAsString);
                context.write(sku, dateDemand);
                
            }
        } catch (NumberFormatException | IOException | InterruptedException ex) {
            //We expect a number in the index 2. If it is absent then
            //it most be the header. Do nothing.
            // We will log the errors.
            context.write(new Text("error" + sku.toString()), new Text(tempVar));
            System.out.println("error " + sku.toString() + ": " + tempVar + "\n" + ex.getMessage());
        }
    }

}
