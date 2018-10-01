#Stopping name node daemon
$HADOOP_HOME/bin/hdfs --daemon stop namenode
#Stopping the datanode
$HADOOP_HOME/bin/hdfs --daemon stop datanode
#Stopping yarn resourcemanager daemon
$HADOOP_HOME/bin/yarn --daemon stop resourcemanager
#Stopping yarn resourcemanager daemon
$HADOOP_HOME/bin/yarn --daemon stop nodemanager
