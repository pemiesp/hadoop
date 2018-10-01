/*
 * Copyright (c) 2018 Mario Fonseca
 */
package com.diplomadobd.retailforecasting.reduce;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * This Reducer will receive all the rows for a specific SKU. It will only write
 * each record as is with no extra reduce task.
 *
 * @author mario
 */
public class AccDemandRd extends Reducer<Text, Text, Text, Text> {

    @Override
    public void reduce(Text key, Iterable<Text> values,
            Context context) throws IOException, InterruptedException {
        Configuration conf;
        conf = context.getConfiguration();
        //Set the separator
        String sep = conf.get("mapreduce.output.textoutputformat.separator");
        //Set the initial state of the accumulator
        Double acc = 0.0;
        //Set the initial state of the week index
        Integer week = -2;

        //Convert the given iterator into a stream
        //to easily be able to order the contents
        Collection<String> coll = new ArrayList<>();
        values.forEach(a -> coll.add(a.toString()));

        //Sorts the values in coll by date
        for (String row : coll.stream().sorted().collect(Collectors.toList())) {
            //tokens[0] is the date
            //tokens[1] is the demand
            String[] tokens = row.split(sep);
            String date = tokens[0];
            Text dateCumDemand = new Text();
            //Convert the demand from String type to Double type
            Double demand = Double.valueOf(tokens[1]);
            //Accumulate the demand
            acc += demand;
            //Accumulate the week index
            week += 3;
            //
            DecimalFormat myFormatter = new DecimalFormat("###.##");
            StringBuilder sb = new StringBuilder();
            sb.append(date)
                    .append(sep)
                    .append(myFormatter.format(demand))
                    .append(sep)
                    .append(myFormatter.format(acc))
                    .append(sep)
                    .append(String.valueOf(week));
            context.write(key, new Text(sb.toString()));
        }
    }

}
