USER=bgd1admin
HOME=/home/bgd1admin
#Removes the Path variable
head -n -1 /etc/profile.d/bigdata_env.sh >tmp.txt
sudo mv tmp.txt /etc/profile.d/bigdata_env.sh
#create a  group hadoop
sudo addgroup hadoop
#Add hdadmin to the hadoop usergroup.
sudo usermod -a -G hadoop $USER
#Download Hadoop
wget http://www-us.apache.org/dist/hadoop/core/current/hadoop-3.1.1.tar.gz
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
#generate ssh key, and hit enter
ssh-keygen -t rsa -P "" -f /home/$USER/.ssh/id_rsa
#Enable SSH access to the machine with the key created in the previous step. For this, we have to add the key to the authorized
#keys list of the machine.
cat $HOME/.ssh/id_rsa.pub >> $HOME/.ssh/authorized_keys
#Copy the hadoop file configurations
cp hadoop/etchadoop/* /opt/hadoop/etc/hadoop/
#Format the hdfs file systems
hdfs namenode -format
#Start the hadoop ecosystem
starthadoop.sh
#Creates hdfs folders
hdfs dfs -mkdir /user
hdfs dfs -mkdir /user/bgd1admin

#Create hdfs folders for Word Count

hdfs dfs -mkdir wordcount
hdfs dfs -mkdir wordcount/input
hdfs dfs -mkdir wordcount/output
hdfs dfs -put $BATCHD_REPO/datasets/1342-0.txt wordcount/input

#Create hdfs folders for Retail Forecasting
hdfs dfs -mkdir 50skusraw
#Creates the directory to put the raw csv
hdfs dfs -mkdir skudemandtbl
#Copy from the local
hdfs dfs -put $BATCHD_REPO/datasets/50SKUs_raw.csv /user/$USER/50skusraw

#Stops hadoop ecosystem
stophadoop.sh
#Loads env variables
#source /etc/profile.d/bigdata_env.sh
