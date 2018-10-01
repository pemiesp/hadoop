/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.diplomadobd.retailforecasting.driver;

import com.diplomadobd.retailforecasting.map.Raw2TableMap;
import com.diplomadobd.retailforecasting.reduce.Raw2TableRd;
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
public class Raw2TableDriver {
    public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    //Separator most be configured before instanciating the job
    conf.set("mapreduce.output.textoutputformat.separator", ",");
    Job job = Job.getInstance(conf, "Raw To Table Transformation");
    job.setJarByClass(Raw2TableDriver.class);
    job.setMapperClass(Raw2TableMap.class);
    job.setCombinerClass(Raw2TableRd.class);
    job.setReducerClass(Raw2TableRd.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileSystem.get(conf).delete(new Path(args[1]),true);
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
