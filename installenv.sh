#Update repository
sudo apt-get update && sudo apt-get upgrade -y
#Get the java version jdk1.8.0_181
wget -c --header "Cookie: oraclelicense=accept-securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/8u181-b13/96a7b8442fe848ef90c96a2fad6ed6d1/jdk-8u181-linux-x64.tar.gz
sudo mkdir /opt/jdk
sudo tar -zxf jdk-8u181-linux-x64.tar.gz -C /opt/jdk
#Setting the default Java
sudo update-alternatives --install /usr/bin/java java /opt/jdk/jdk1.8.0_181/bin/java 100
sudo update-alternatives --install /usr/bin/javac javac /opt/jdk/jdk1.8.0_181/bin/javac 100
sudo update-alternatives --install /usr/bin/jps jps /opt/jdk/jdk1.8.0_181/bin/jps 100
#Delete the java zip profile
rm jdk-8u181-linux-x64.tar.gz
#Config environment variables
sudo touch /etc/profile.d/bigdata_env.sh
sudo chmod +x /etc/profile.d/bigdata_env.sh
#Set JAVA_HOME variable
echo  "export JAVA_HOME=/opt/jdk/jdk1.8.0_181" | sudo tee --append /etc/profile.d/bigdata_env.sh
#Download Maven
wget http://www-eu.apache.org/dist/maven/maven-3/3.5.4/binaries/apache-maven-3.5.4-bin.tar.gz
sudo mkdir /opt/maven
sudo tar -zxf apache-maven-3.5.4-bin.tar.gz -C /opt/maven
rm apache-maven-3.5.4-bin.tar.gz
#Setting the path for maven
echo  "export PATH=$PATH:/opt/maven/apache-maven-3.5.4/bin:/home/$USER/hadoop" | sudo tee --append /etc/profile.d/bigdata_env.sh
source /etc/profile.d/bigdata_env.sh
export PATH=$PATH:/opt/maven/apache-maven-3.5.4/bin:/home/$USER/hadoop
#Disable ipv6
echo "net.ipv6.conf.all.disable_ipv6 = 1" sudo tee --append  /etc/sysctl.conf
echo "net.ipv6.conf.default.disable_ipv6 = 1" sudo tee --append  /etc/sysctl.conf
echo "net.ipv6.conf.lo.disable_ipv6 = 1" sudo tee --append  /etc/sysctl.conf
sudo sysctl -p
#Also add execution permissions for scripts for executing hadoopConfig
sudo chmod 774 gitviews/batchadoop/singlenodex/*.sh
source /etc/profile.d/bigdata_env.sh