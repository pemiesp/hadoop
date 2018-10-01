#Compiles and executes WordCount
export BATCHD_REPO=~/gitviews/batchadoop
cd $BATCHD_REPO/application/RetailForecasting
mvn package 
hadoop jar $BATCHD_REPO/application/RetailForecasting/target/RetailForecasting-1.0-SNAPSHOT.jar com.diplomadobd.retailforecasting.driver.Raw2TableDriver /user/$USER/50skusraw /user/$USER/skudemandtbl
cd ~