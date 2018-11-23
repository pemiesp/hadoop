#Update repository
apt-get update && apt-get upgrade -y
#Get the java version jdk1.8.0_181
wget -c --header "Cookie: oraclelicense=accept-securebackup-cookie" https://download.oracle.com/otn-pub/java/jdk/8u191-b12/2787e4a523244c269598db4e85c51e0c/jdk-8u191-linux-x64.tar.gz
mkdir /opt/jdk
tar -zxf jdk-8u191-linux-x64.tar.gz -C /opt/jdk
#Setting the default Java
update-alternatives --install /usr/bin/java java /opt/jdk/jdk1.8.0_191/bin/java 100
update-alternatives --install /usr/bin/javac javac /opt/jdk/jdk1.8.0_191/bin/javac 100
update-alternatives --install /usr/bin/jps jps /opt/jdk/jdk1.8.0_191/bin/jps 100
#Delete the java zip profile
rm jdk-8u191-linux-x64.tar.gz
#Config environment variables
touch /etc/profile.d/bigdata_env.sh
chmod +x /etc/profile.d/bigdata_env.sh
#Set JAVA_HOME variable
echo "export JAVA_HOME=/opt/jdk/jdk1.8.0_191" | sudo tee --append /etc/profile.d/bigdata_env.sh
#Download Maven
wget https://www-us.apache.org/dist/maven/maven-3/3.6.0/binaries/apache-maven-3.6.0-bin.tar.gz
mkdir /opt/maven
tar -zxf apache-maven-3.6.0-bin.tar.gz -C /opt/maven
rm apache-maven-3.6.0-bin.tar.gz
#Setting the path for maven
echo  "export PATH=$PATH:/opt/maven/apache-maven-3.6.0/bin:/nmx/hadoop" | sudo tee --append /etc/profile.d/bigdata_env.sh
source /etc/profile.d/bigdata_env.sh
export PATH=$PATH:/opt/maven/apache-maven-3.6.0/bin:/nmx/hadoop
#Disable ipv6
echo "net.ipv6.conf.all.disable_ipv6 = 1" sudo tee --append  /etc/sysctl.conf
echo "net.ipv6.conf.default.disable_ipv6 = 1" sudo tee --append  /etc/sysctl.conf
echo "net.ipv6.conf.lo.disable_ipv6 = 1" sudo tee --append  /etc/sysctl.conf
sudo sysctl -p
#Also add execution permissions for scripts for executing hadoopConfig
chmod 774 hadoop/execute/*.sh
source /etc/profile.d/bigdata_env.sh
#Set java home
export JAVA_HOME=/opt/jdk/jdk-8.0_181