#Creates hdfs folders
hdfs dfs -mkdir /user
hdfs dfs -mkdir /user/hadoop

#Create hdfs folders for Word Count

hdfs dfs -mkdir /user/hadoop/wordcount
hdfs dfs -mkdir /user/hadoop/wordcount/input
hdfs dfs -mkdir /user/hadoop/wordcount/output
cd /nmx/hadoop/datasets
hdfs dfs -put 1342-0.txt /user/hadoop/wordcount/input
cd ~
#Stops hadoop ecosystem
. /nmx/hadoop/execute/stophadoop.sh
#Loads env variables
source /etc/profile.d/bigdata_env.sh
. /nmx/hadoop/execute/starthadoop.sh
. /nmx/hadoop/execute/wordcount.sh
