#FLINK_DIR 本地flink客户端目录
#执行flink任务
${FLINK_DIR}/bin/flink ${FLINK_ACTION} -m ${FLINK_CLUSTER}  <#if SAVEPOINT_PATH??> -s ${SAVEPOINT_PATH} </#if> <#if ALLOWNRS==1> -n </#if> <#if PARALLELISM??> -p ${PARALLELISM} </#if> -d ${JAR_PATH} \
<#if DATA_MODE=='ds'>
  --data-mode=ds --maxParallelism=${MaxParallelism} --JobID=${JobID}  --profilesActive=${PROFILES_ACTIVE}
<#else>
  -w "${FILE_DIR}" -f ${FILE_NAME}.sql
</#if>

if [ $? != 0 ]; then
  echo "last exe fail"
  exit 1
else
  echo "last exe success"
  exit 0
fi