#Creates hdfs folders
hdfs dfs -mkdir /user
hdfs dfs -mkdir /user/hadoop

#Create hdfs folders for Word Count

hdfs dfs -mkdir /user/hadoop/wordcount
hdfs dfs -mkdir /user/hadoop/wordcount/input
hdfs dfs -mkdir /user/hadoop/wordcount/output
cd /nmx/hadoop/datasets
hdfs dfs -put 1342-0.txt /user/hadoop/wordcount/input