/*
 * Copyright (c) 2018 Mario Fonseca
 */
package com.diplomadobd.retailforecasting.reduce;

import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * This Reducer will receive all the rows for a specific SKU. It will only write
 * each record as is with no extra reduce task.
 *
 * @author mario
 */
public class Raw2TableRd extends Reducer<Text, Text, Text, Text> {

    @Override
    public void reduce(Text key, Iterable<Text> values,
            Context context) throws IOException, InterruptedException {
        for (Text value : values) {
            context.write(key, value);
        }        
    }

}
