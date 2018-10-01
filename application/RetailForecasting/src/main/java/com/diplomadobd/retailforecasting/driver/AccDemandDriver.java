/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diplomadobd.retailforecasting.driver;

import com.diplomadobd.retailforecasting.map.AccDemandMap;
import com.diplomadobd.retailforecasting.reduce.AccDemandRd;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 *
 * @author mario
 */
public class AccDemandDriver {
    public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    //Separator most be configured before instanciating the job
   
    conf.set("mapreduce.output.textoutputformat.separator", ",");
    conf.set("first.key.columns", "1");
    Job job = Job.getInstance(conf, "Accumulate Demand");
    job.setJarByClass(AccDemandDriver.class);
    job.setMapperClass(AccDemandMap.class);
    job.setCombinerClass(AccDemandRd.class);
    job.setReducerClass(AccDemandRd.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileSystem.get(conf).delete(new Path(args[1]),true);
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
