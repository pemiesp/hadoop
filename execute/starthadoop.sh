#Starting and stopping hadoop services

#Starting name node daemon
$HADOOP_HOME/bin/hdfs --daemon start namenode
#Starting the datanode
$HADOOP_HOME/bin/hdfs --daemon start datanode
#Start yarn resourcemanager daemon
$HADOOP_HOME/bin/yarn --daemon start resourcemanager
#Start yarn resourcemanager daemon
$HADOOP_HOME/bin/yarn --daemon start nodemanager
