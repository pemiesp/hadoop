USER=hadoop
HOME=/home/hadoop

#Removes the Path variable
head -n -1 /etc/profile.d/bigdata_env.sh >tmp.txt
sudo mv tmp.txt /etc/profile.d/bigdata_env.sh

#create a  group hadoop
sudo addgroup hadoop

#Add hdadmin to the hadoop usergroup.
sudo usermod -a -G hadoop $USER

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

#generate ssh key, and hit enter
mkdir /home/$USER/.ssh
ssh-keygen -t rsa -P "" -f /home/$USER/.ssh/id_rsa

#Enable SSH access to the machine with the key created in the previous step. For this, we have to add the key to the authorized keys list of the machine.
cat $HOME/.ssh/id_rsa.pub >> $HOME/.ssh/authorized_keys

#Copy the hadoop file configurations
cp /nmx/hadoop/etchadoop/* /opt/hadoop/etc/hadoop/

#Format the hdfs file systems
hdfs namenode -format

#Start the hadoop ecosystem
. /nmx/hadoop/execute/starthadoop.sh
jps
