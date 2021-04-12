source /etc/profile
pid=`ps -ef|grep hlink-web-manager.jar|grep -v grep|awk '{print $2}' `
echo $pid
echo ${pid}
if [ -z ${pid} ]; then
   echo 'hlink-web-manager is not running'
else
   kill -9 $pid
   echo $pid'已被干掉'
   sleep 3
fi