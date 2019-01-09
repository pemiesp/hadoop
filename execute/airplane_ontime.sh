#Compiles and executes Airplane
export BATCHD_REPO=/nmx/hadoop
export USER=hadoop
cd $BATCHD_REPO/application/Airplane
mvn package 
hadoop jar $BATCHD_REPO/application/Airplane/target/Airplane-1.0-SNAPSHOT.jar mx.com.nissinmfg.airplane.OnTime /user/$USER/airplane/input /user/$USER/airplane/output
cd ~
