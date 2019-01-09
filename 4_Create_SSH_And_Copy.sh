USER=hadoop
HOME=/home/hadoop

#generate ssh key, and hit enter
mkdir /home/$USER/.ssh
ssh-keygen -t rsa -P "" -f /home/$USER/.ssh/id_rsa

ssh-copy-id -i ~/.ssh/id_rsa.pub hadoop@bgd1.bigdata
ssh-copy-id -i ~/.ssh/id_rsa.pub hadoop@bgd2.bigdata