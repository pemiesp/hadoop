USER=hadoop
HOME=/home/hadoop

#Removes the Path variable
head -n -1 /etc/profile.d/bigdata_env.sh >tmp.txt
sudo mv tmp.txt /etc/profile.d/bigdata_env.sh

#create a  group hadoop
sudo addgroup hadoop

#Add hdadmin to the hadoop usergroup.
sudo usermod -a -G hadoop $USER
#Add hadoop as a sudoer
#sudo usermod -aG sudo hadoop

#Download Hadoop
wget https://www-us.apache.org/dist/hadoop/common/hadoop-3.1.1/hadoop-3.1.1.tar.gz
tar -xzf hadoop-3.1.1.tar.gz

#Move to the next directory and move the contents into the hadoop directory
sudo mv  hadoop-3.1.1 /opt/hadoop

#Delete the zip file
rm hadoop-3.1.1.tar.gz

#change the permissions to the hduser user.
sudo chown -R $USER:$USER /opt/hadoop

#Set env variables
export BATCHD_REPO=/home/$USER/hadoop
echo "export BATCHD_REPO=/home/$USER/hadoop" | sudo tee --append /etc/profile.d/bigdata_env.sh
export HADOOP_HOME=/opt/hadoop
echo "export HADOOP_HOME=/opt/hadoop" | sudo tee --append /etc/profile.d/bigdata_env.sh
echo "export PATH=$PATH:/opt/hadoop/bin" >> /etc/profile.d/bigdata_env.sh
export PATH=$PATH:/opt/hadoop/bin

#Copy the hadoop file configurations
cp /nmx/hadoop/etchadoop/* /opt/hadoop/etc/hadoop/

#Format the hdfs file systems
hdfs namenode -format

#Start the hadoop ecosystem
. /nmx/hadoop/execute/starthadoop.sh
jps
