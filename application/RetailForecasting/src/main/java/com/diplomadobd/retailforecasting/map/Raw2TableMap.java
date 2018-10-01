/*
 * Copyright (c) 2018 Mario Fonseca
 */
package com.diplomadobd.retailforecasting.map;

import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * Map class to transform the raw input data into a table with the next three
 * column schema: Date, SKU, Demand.
 *
 * The input file has the next schema: CÓDIGO.- String that represents an
 * skukey. AÑO.- A numbrer with a year number in the format yyyy ENE, FEB.,
 * MAR., ABR., MAY., JUN., JUL., AGO., SEP., OCT., NOV., DIC.- This 12 columns
 * represent a month of the year
 *
 * @author mario
 */
public class Raw2TableMap extends Mapper<Object, Text, Text, Text> {

    //Regular expression to read columns from a csv file coma separated.
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
        String[] tokens = value.toString().split(csvRegex, -1);
        String tempVar = null;
        //Discard the file header.
        try {
            //Replaces the "-" for zero and validates it is a expected row.
            boolean isTheHeader = false;
            try {
                tempVar = tokens[2]
                        .replace("\"", "").replace("'", "''").replace("\\", "\\\\").trim()
                        .replace("-", "0").replace(",", "");
                Long.valueOf(tempVar);
            } catch (NumberFormatException ex) {
                //It is the header
                isTheHeader = true;
            }
            if (!isTheHeader) {
                Long demand;
                //Sets the sku
                sku.set(tokens[0]);
                String theYear = tokens[1];
                for (int i = 2; i < tokens.length; i++) {
                    //Converts the String value of demand into a Long
                    tempVar = tokens[i]
                            .replace("\"", "").replace("'", "''").replace("\\", "\\\\").trim()
                            .replace("-", "0").replace(",", "");
                    demand = Long.valueOf(tempVar);
                    //Converts the month in a format MM
                    String theMonth = ("0" + String.valueOf(i - 1));
                    theMonth = theMonth.substring(theMonth.length() - 2, theMonth.length());
                    //Sets a row in the format: yyyMM,demandValue
                    dateDemand = new Text(theYear + theMonth + "," + demand.toString());
                    //Writes the Key, Value row
                    context.write(sku, dateDemand);
                }
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
