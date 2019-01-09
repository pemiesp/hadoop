#Creates hdfs folders
hdfs dfs -mkdir /user
hdfs dfs -mkdir /user/hadoop

#Create hdfs folders for Word Count

hdfs dfs -mkdir /user/hadoop/airplane
hdfs dfs -mkdir /user/hadoop/airplane/input
hdfs dfs -mkdir /user/hadoop/airplane/output
cd /nmx/hadoop/datasets
hdfs dfs -put 2006.csv /user/hadoop/airplane/input
hdfs dfs -put 2007.csv /user/hadoop/airplane/input
hdfs dfs -put 2008.csv /user/hadoop/airplane/input