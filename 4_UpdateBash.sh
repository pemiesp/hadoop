#Update bach
sudo echo "#Custom exports" >> ~/.bashrc
sudo echo "export HADOOP_HOME=/opt/hadoop" >> ~/.bashrc
sudo echo "export NMX_HADOOP_HOME=/nmx/hadoop" >> ~/.bashrc
sudo echo "export HADOOP_CONF_DIR=$HADOOP_HOME/etc/hadoop" >> ~/.bashrc
sudo echo "export PATH=$PATH:/etc/hadoop/bin:/opt/maven/apache-maven-3.6.0/bin" >> ~/.bashrc

sudo echo "#Custom Alias" >> ~/.bashrc
sudo echo "alias hstart="$HADOOP_HOME/sbin/start-dfs.sh"" >> ~/.bashrc
sudo echo "alias hstop="$HADOOP_HOME/sbin/stop-dfs.sh"" >> ~/.bashrc
sudo echo "alias ystart="$HADOOP_HOME/sbin/start-yarn.sh"" >> ~/.bashrc
sudo echo "alias ystop="$HADOOP_HOME/sbin/stop-yarn.sh"" >> ~/.bashrc
sudo echo "alias runWordcount="$NMX_HADOOP_HOME/execute/wordcount.sh"" >> ~/.bashrc
sudo echo "alias runAirplanen.OnTime="$NMX_HADOOP_HOME/execute/airplane_ontime.sh"" >> ~/.bashrc