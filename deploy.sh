#!/usr/bin/env bash

set password "admin"

#scp plugins/* admin@10.57.17.160:/data01/hlink/flink-1.9.2/lib/
#scp shutdown.sh admin@10.57.17.160:/data01/hlink/app

source ~/.bash_profile
mvn clean package -Dmaven.test.skip=true

ssh admin@10.57.17.160 "
cd /data01/hlink/app
sh shutdown.sh
exit
"

scp hlink-web-manager/target/hlink-web-manager.jar hlink-web-manager/target/classes/application.yml hlink-web-manager/target/classes/application-test.yml hlink-clients/target/hlink-clients.jar admin@10.57.17.160:/data01/hlink/app

ssh admin@10.57.17.160 "
source /etc/profile
cd /data01/hlink/app
echo 'hlink-web-manager server start ...'

nohup java -Xms1024m -Xmx1024m -XX:PermSize=256m -XX:MaxPermSize=512m -XX:MaxNewSize=512m  -Dfile.encoding=UTF8 -Duser.timezone=GMT+08 -jar hlink-web-manager.jar > logs/catalina.out 2>&1 &
echo  > logs/catalina.out
tail -f logs/catalina.out
"
