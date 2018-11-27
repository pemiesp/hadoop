#Creates hdfs folders
hdfs dfs -mkdir /user
hdfs dfs -mkdir /user/bgd1admin

#Create hdfs folders for Word Count

hdfs dfs -mkdir /user/bgd1admin/wordcount
hdfs dfs -mkdir /user/bgd1admin/wordcount/input
hdfs dfs -mkdir /user/bgd1admin/wordcount/output
cd /nmx/hadoop/datasets
hdfs dfs -put 1342-0.txt /user/bgd1admin/wordcount/input

#Stops hadoop ecosystem
. /nmx/hadoop/execute/stophadoop.sh
#Loads env variables
source /etc/profile.d/bigdata_env.sh
. /nmx/hadoop/execute/starthadoop.sh