#Sets the local directory
export BATCHD_REPO=~/gitviews/batchadoop
#Creates the directory to put the raw csv
hdfs dfs -mkdir /user/$USER/50skusraw
#Creates the directory to put the raw csv
hdfs dfs -mkdir /user/$USER/skudemandtbl
#Copy from the local
hdfs dfs -put $BATCHD_REPO/datasets/50SKUs_raw.csv /user/$USER/50skusraw
#Verify the file is there
hdfs dfs -ls /user/$USER/50skusraw
#Execute the job to transform
hadoop jar $BATCHD_REPO/application/RetailForecasting/target/RetailForecasting-1.0-SNAPSHOT.jar com.diplomadobd.retailforecasting.driver.Raw2TableDriver /user/$USER/50skusraw /user/$USER/skudemandtbl

