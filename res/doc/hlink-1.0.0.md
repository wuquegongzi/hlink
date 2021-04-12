# Hlink实时特征加工服务


**简介**:Hlink实时特征加工服务


**HOST**:localhost:8086


**联系人**:leon


**Version**:1.0.0


**接口路径**:/v2/api-docs?group=1.0.0


[TOC]






# 作业管理


## 新建作业


**接口地址**:`/jobs/addDO`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
	"allowNrs": 0,
	"checkpointCleanupMode": 0,
	"checkpointInterval": 0,
	"checkpointMaxCuncurrency": 0,
	"checkpointMode": 0,
	"checkpointStatebackendPath": "",
	"checkpointStatebackendType": 0,
	"checkpointTimeout": 0,
	"clusterId": 0,
	"clusterName": "",
	"clusterType": 0,
	"clusterTypeName": "",
	"computingUnit": 0,
	"curPage": 0,
	"delayInterval": 0,
	"enableMinibatchOptimization": 0,
	"entryClass": "",
	"failureInterval": 0,
	"failureRate": 0,
	"jobDesc": "",
	"jobDsVOS": [
		{
			"dsId": 0,
			"dsType": "",
			"id": 0,
			"jobId": 0,
			"runSql": ""
		}
	],
	"jobName": "",
	"jobType": 0,
	"jobTypeName": "",
	"minPauseBetweenCheckpoints": 0,
	"pageSize": 0,
	"parallelism": 0,
	"parms": {},
	"program": "",
	"restartAttempts": 0,
	"restartStrategy": 0,
	"savepointPath": "",
	"stateSavepointsDir": "",
	"tableExecMinibatchAllowlatency": 0,
	"tableExecMinibatchSize": 0,
	"tableOptimizerAggPhaseStrategy": 0,
	"tableOptimizerDistinctAggSplitEnabled": 0,
	"timeType": 0,
	"useJar": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|jobVO|jobVO|body|true|作业  视图实体|作业  视图实体|
|&emsp;&emsp;allowNrs|Allow Non Restore State,允许非还原状态||false|integer(int32)||
|&emsp;&emsp;checkpointCleanupMode|checkpoint清除策略，对应字典 checkpoint_cleanup_mode||false|integer(int32)||
|&emsp;&emsp;checkpointInterval|Checkpoint Interval (ms),设置该值则开启检查点，为0则不开启||false|integer(int64)||
|&emsp;&emsp;checkpointMaxCuncurrency|最大并发生成Checkpoint数||false|integer(int32)||
|&emsp;&emsp;checkpointMode|checkpoint模式，对应字典 checkpoint_mode||false|integer(int32)||
|&emsp;&emsp;checkpointStatebackendPath|statebackend存储地址||false|string||
|&emsp;&emsp;checkpointStatebackendType|statebackend方式，见字典statebackend_type ||false|integer(int32)||
|&emsp;&emsp;checkpointTimeout|生成checkpoint的超时时间||false|integer(int64)||
|&emsp;&emsp;clusterId|映射的集群ID||false|integer(int64)||
|&emsp;&emsp;clusterName|集群名称||false|string||
|&emsp;&emsp;clusterType|集群类型，目前默认 0-sdandlone||false|integer(int32)||
|&emsp;&emsp;clusterTypeName|集群类型 名称，辅助 转义字段||false|string||
|&emsp;&emsp;computingUnit|计算单元||false|integer(int32)||
|&emsp;&emsp;createBy|创建人||false|string||
|&emsp;&emsp;createTime|创建时间||false|string(date-time)||
|&emsp;&emsp;curPage|当前页||false|integer(int32)||
|&emsp;&emsp;delFlag|逻辑删除标识 0 废弃 1 正常 ,默认为1，保留字段||false|integer(int32)||
|&emsp;&emsp;delayInterval|重启间隔时间(s)||false|integer(int32)||
|&emsp;&emsp;enableMinibatchOptimization|开启微型批次聚合 0-不开启 1-开启||false|integer(int32)||
|&emsp;&emsp;entryClass|JAR任务运行时使用的入口类||false|string||
|&emsp;&emsp;failureInterval|计算失败率的时间间隔(m)||false|integer(int32)||
|&emsp;&emsp;failureRate|一个时间段内的最大失败次数||false|integer(int32)||
|&emsp;&emsp;flinkJobId|集群内运行的作业ID||false|string||
|&emsp;&emsp;id|主键id||false|integer(int64)||
|&emsp;&emsp;jobCode|作业编号||false|string||
|&emsp;&emsp;jobDesc|作业描述||false|string||
|&emsp;&emsp;jobDsVOS|SQL作业专用 数据源映射||false|array|作业与数据源映射|
|&emsp;&emsp;&emsp;&emsp;dsId|选择的数据源类型ID||false|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;dsType|数据源类型  source、sink、side||false|string||
|&emsp;&emsp;&emsp;&emsp;id|主键ID||false|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;jobId|对应的作业ID，新增作业的时候默认为0即可||false|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;runSql|特征加工语句，只有数据源类型为sink目标表的时候，才需要填写该值||false|string||
|&emsp;&emsp;jobName|作业名称||false|string||
|&emsp;&emsp;jobStatus|作业状态0未运行，1运行中，2取消，3失败, 9提交中 ||false|integer(int32)||
|&emsp;&emsp;jobStatusName|作业状态名称 转义辅助字段||false|string||
|&emsp;&emsp;jobType|作业类型  0-SQL 1-JAR||false|integer(int32)||
|&emsp;&emsp;jobTypeName|作业类型名称 辅助转义字段||false|string||
|&emsp;&emsp;minPauseBetweenCheckpoints|确保检查点之间有1s的时间间隔【checkpoint最小间隔】||false|integer(int64)||
|&emsp;&emsp;modifyBy|修改人||false|string||
|&emsp;&emsp;modifyTime|修改时间||false|string(date-time)||
|&emsp;&emsp;pageSize|页面大小||false|integer(int32)||
|&emsp;&emsp;parallelism|并行度||false|integer(int32)||
|&emsp;&emsp;parms|自定义参数||false|object||
|&emsp;&emsp;program|JAR任务运行时使用的参数||false|string||
|&emsp;&emsp;restartAttempts|尝试重启的次数||false|integer(int32)||
|&emsp;&emsp;restartStrategy|重启策略，对应字典 restart_strategy||false|integer(int32)||
|&emsp;&emsp;savepointPath|存储点路径||false|string||
|&emsp;&emsp;stateSavepointsDir|自定义保存点生成根路径||false|string||
|&emsp;&emsp;tableExecMinibatchAllowlatency|缓冲输入记录最大等待时间(s). 注：若开启微批次聚合，其值必须大于零||false|integer(int64)||
|&emsp;&emsp;tableExecMinibatchSize|缓冲最大输入记录数.注：若开启微批次聚合，则其值必须为正||false|integer(int64)||
|&emsp;&emsp;tableOptimizerAggPhaseStrategy|启用局部本地全局聚合. 0-禁用 1-开启||false|integer(int32)||
|&emsp;&emsp;tableOptimizerDistinctAggSplitEnabled|启用不同的agg分割. 0-禁用 1-开启||false|integer(int32)||
|&emsp;&emsp;timeType|Flink 时间类型，对应字典 flink_time_type||false|integer(int32)||
|&emsp;&emsp;useJar|JAR任务运行时使用的JAR文件||false|string||
|&emsp;&emsp;version|版本号 保留字段||false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|公共 响应«作业  持久层实体»|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|data|数据|作业  持久层实体|作业  持久层实体|
|&emsp;&emsp;allowNrs|Allow Non Restore State,允许非还原状态|integer(int32)||
|&emsp;&emsp;checkpointCleanupMode|checkpoint清除策略，对应字典 checkpoint_cleanup_mode|boolean||
|&emsp;&emsp;checkpointInterval|Checkpoint Interval (ms),设置该值则开启检查点，为0则不开启|integer(int64)||
|&emsp;&emsp;checkpointMaxCuncurrency|最大并发生成Checkpoint数|integer(int32)||
|&emsp;&emsp;checkpointMode|checkpoint模式，对应字典 checkpoint_mode|integer(int32)||
|&emsp;&emsp;checkpointStatebackendPath|statebackend存储地址|string||
|&emsp;&emsp;checkpointStatebackendType|statebackend方式，见字典statebackend_type |integer(int32)||
|&emsp;&emsp;checkpointTimeout|生成checkpoint的超时时间|integer(int64)||
|&emsp;&emsp;clusterId|集群ID|integer(int64)||
|&emsp;&emsp;computingUnit|计算单元个数，eg:1个计算单元资源为1核3GB2个并发度，当前可用资源为146核168.00GB|integer(int32)||
|&emsp;&emsp;createBy|创建人|string||
|&emsp;&emsp;createTime|创建时间|string(date-time)||
|&emsp;&emsp;delFlag|0 废弃 1 正常 |integer(int32)||
|&emsp;&emsp;delayInterval|重启间隔时间(s)|integer(int32)||
|&emsp;&emsp;enableMinibatchOptimization|开启微型批次聚合 0-不开启 1-开启|integer(int32)||
|&emsp;&emsp;entryClass|JAR任务运行时使用的入口类|string||
|&emsp;&emsp;failureInterval|计算失败率的时间间隔(m)|integer(int32)||
|&emsp;&emsp;failureRate|一个时间段内的最大失败次数|integer(int32)||
|&emsp;&emsp;flinkJobId|关联的集群内运行的作业ID|string||
|&emsp;&emsp;id|主键|integer(int64)||
|&emsp;&emsp;jobCode|作业编号|string||
|&emsp;&emsp;jobDesc|作业描述|string||
|&emsp;&emsp;jobName|作业名称|string||
|&emsp;&emsp;jobStatus|作业状态0未运行，1运行中，2取消，3失败, 9提交中 |integer(int32)||
|&emsp;&emsp;jobType|作业类型  0-SQL 1-JAR|integer(int32)||
|&emsp;&emsp;minPauseBetweenCheckpoints|确保检查点之间有1s的时间间隔【checkpoint最小间隔】|integer(int64)||
|&emsp;&emsp;modifyBy|修改人|string||
|&emsp;&emsp;modifyTime|修改时间|string(date-time)||
|&emsp;&emsp;parallelism|并行度|integer(int32)||
|&emsp;&emsp;program|JAR任务运行时使用的参数|string||
|&emsp;&emsp;restartAttempts|尝试重启的次数|integer(int32)||
|&emsp;&emsp;restartStrategy|重启策略，对应字典 restart_strategy|integer(int32)||
|&emsp;&emsp;savepointPath|当前保存点位置|string||
|&emsp;&emsp;stateSavepointsDir|自定义保存点生成根路径|string||
|&emsp;&emsp;tableExecMinibatchAllowlatency|缓冲输入记录最大等待时间(s). 注：若开启微批次聚合，其值必须大于零|integer(int64)||
|&emsp;&emsp;tableExecMinibatchSize|缓冲最大输入记录数.注：若开启微批次聚合，则其值必须为正|integer(int64)||
|&emsp;&emsp;tableOptimizerAggPhaseStrategy|启用局部本地全局聚合. 0-禁用 1-开启|integer(int32)||
|&emsp;&emsp;tableOptimizerDistinctAggSplitEnabled|启用不同的agg分割. 0-禁用 1-开启|integer(int32)||
|&emsp;&emsp;timeType|Flink 时间类型，对应字典 flink_time_type|integer(int32)||
|&emsp;&emsp;useJar|JAR任务运行时使用的JAR文件|string||
|&emsp;&emsp;version|版本号 保留字段|string||
|msg|状态信息|string||
|success|请求是否成功|boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"data": {
		"allowNrs": 0,
		"checkpointCleanupMode": true,
		"checkpointInterval": 0,
		"checkpointMaxCuncurrency": 0,
		"checkpointMode": 0,
		"checkpointStatebackendPath": "",
		"checkpointStatebackendType": 0,
		"checkpointTimeout": 0,
		"clusterId": 0,
		"computingUnit": 0,
		"createBy": "",
		"createTime": "",
		"delFlag": 0,
		"delayInterval": 0,
		"enableMinibatchOptimization": 0,
		"entryClass": "",
		"failureInterval": 0,
		"failureRate": 0,
		"flinkJobId": "",
		"id": 0,
		"jobCode": "",
		"jobDesc": "",
		"jobName": "",
		"jobStatus": 0,
		"jobType": 0,
		"minPauseBetweenCheckpoints": 0,
		"modifyBy": "",
		"modifyTime": "",
		"parallelism": 0,
		"program": "",
		"restartAttempts": 0,
		"restartStrategy": 0,
		"savepointPath": "",
		"stateSavepointsDir": "",
		"tableExecMinibatchAllowlatency": 0,
		"tableExecMinibatchSize": 0,
		"tableOptimizerAggPhaseStrategy": 0,
		"tableOptimizerDistinctAggSplitEnabled": 0,
		"timeType": 0,
		"useJar": "",
		"version": ""
	},
	"msg": "",
	"success": true
}
```


## 取消作业


**接口地址**:`/jobs/cancel/{jobId}`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|jobId|jobId|path|true|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|公共 响应|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|data|数据|object||
|msg|状态信息|string||
|success|请求是否成功|boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"data": {},
	"msg": "",
	"success": true
}
```


## 批量停止作业


**接口地址**:`/jobs/cancelBatch`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|jobIds|jobIds|query|true|array|integer|


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|公共 响应|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|data|数据|object||
|msg|状态信息|string||
|success|请求是否成功|boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"data": {},
	"msg": "",
	"success": true
}
```


## 作业列表查询


**接口地址**:`/jobs/list`


**请求方式**:`GET`


**请求数据类型**:`*`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|allowNrs|Allow Non Restore State,允许非还原状态|query|false|integer(int32)||
|checkpointCleanupMode|checkpoint清除策略，对应字典 checkpoint_cleanup_mode|query|false|integer(int32)||
|checkpointInterval|Checkpoint Interval (ms),设置该值则开启检查点，为0则不开启|query|false|integer(int64)||
|checkpointMaxCuncurrency|最大并发生成Checkpoint数|query|false|integer(int32)||
|checkpointMode|checkpoint模式，对应字典 checkpoint_mode|query|false|integer(int32)||
|checkpointStatebackendPath|statebackend存储地址|query|false|string||
|checkpointStatebackendType|statebackend方式，见字典statebackend_type |query|false|integer(int32)||
|checkpointTimeout|生成checkpoint的超时时间|query|false|integer(int64)||
|clusterId|映射的集群ID|query|false|integer(int64)||
|clusterName|集群名称|query|false|string||
|clusterType|集群类型，目前默认 0-sdandlone|query|false|integer(int32)||
|clusterTypeName|集群类型 名称，辅助 转义字段|query|false|string||
|computingUnit|计算单元|query|false|integer(int32)||
|createBy|创建人|query|false|string||
|createTime|创建时间|query|false|string(date-time)||
|curPage|当前页|query|false|integer(int32)||
|delFlag|逻辑删除标识 0 废弃 1 正常 ,默认为1，保留字段|query|false|integer(int32)||
|delayInterval|重启间隔时间(s)|query|false|integer(int32)||
|enableMinibatchOptimization|开启微型批次聚合 0-不开启 1-开启|query|false|integer(int32)||
|entryClass|JAR任务运行时使用的入口类|query|false|string||
|failureInterval|计算失败率的时间间隔(m)|query|false|integer(int32)||
|failureRate|一个时间段内的最大失败次数|query|false|integer(int32)||
|flinkJobId|集群内运行的作业ID|query|false|string||
|id|主键id|query|false|integer(int64)||
|jobCode|作业编号|query|false|string||
|jobDesc|作业描述|query|false|string||
|jobDsVOS[0].dsId|选择的数据源类型ID|query|false|integer(int64)||
|jobDsVOS[0].dsType|数据源类型  source、sink、side|query|false|string||
|jobDsVOS[0].id|主键ID|query|false|integer(int64)||
|jobDsVOS[0].jobId|对应的作业ID，新增作业的时候默认为0即可|query|false|integer(int64)||
|jobDsVOS[0].runSql|特征加工语句，只有数据源类型为sink目标表的时候，才需要填写该值|query|false|string||
|jobName|作业名称|query|false|string||
|jobStatus|作业状态0未运行，1运行中，2取消，3失败, 9提交中 |query|false|integer(int32)||
|jobStatusName|作业状态名称 转义辅助字段|query|false|string||
|jobType|作业类型  0-SQL 1-JAR|query|false|integer(int32)||
|jobTypeName|作业类型名称 辅助转义字段|query|false|string||
|minPauseBetweenCheckpoints|确保检查点之间有1s的时间间隔【checkpoint最小间隔】|query|false|integer(int64)||
|modifyBy|修改人|query|false|string||
|modifyTime|修改时间|query|false|string(date-time)||
|pageSize|页面大小|query|false|integer(int32)||
|parallelism|并行度|query|false|integer(int32)||
|parms|自定义参数|query|false|object||
|program|JAR任务运行时使用的参数|query|false|string||
|restartAttempts|尝试重启的次数|query|false|integer(int32)||
|restartStrategy|重启策略，对应字典 restart_strategy|query|false|integer(int32)||
|savepointPath|存储点路径|query|false|string||
|stateSavepointsDir|自定义保存点生成根路径|query|false|string||
|tableExecMinibatchAllowlatency|缓冲输入记录最大等待时间(s). 注：若开启微批次聚合，其值必须大于零|query|false|integer(int64)||
|tableExecMinibatchSize|缓冲最大输入记录数.注：若开启微批次聚合，则其值必须为正|query|false|integer(int64)||
|tableOptimizerAggPhaseStrategy|启用局部本地全局聚合. 0-禁用 1-开启|query|false|integer(int32)||
|tableOptimizerDistinctAggSplitEnabled|启用不同的agg分割. 0-禁用 1-开启|query|false|integer(int32)||
|timeType|Flink 时间类型，对应字典 flink_time_type|query|false|integer(int32)||
|useJar|JAR任务运行时使用的JAR文件|query|false|string||
|version|版本号 保留字段|query|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|公共 响应«IPage«作业  视图实体»»|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|data|数据|IPage«作业  视图实体»|IPage«作业  视图实体»|
|&emsp;&emsp;current||integer(int64)||
|&emsp;&emsp;pages||integer(int64)||
|&emsp;&emsp;records||array|作业  视图实体|
|&emsp;&emsp;&emsp;&emsp;allowNrs|Allow Non Restore State,允许非还原状态||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;checkpointCleanupMode|checkpoint清除策略，对应字典 checkpoint_cleanup_mode||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;checkpointInterval|Checkpoint Interval (ms),设置该值则开启检查点，为0则不开启||false|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;checkpointMaxCuncurrency|最大并发生成Checkpoint数||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;checkpointMode|checkpoint模式，对应字典 checkpoint_mode||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;checkpointStatebackendPath|statebackend存储地址||false|string||
|&emsp;&emsp;&emsp;&emsp;checkpointStatebackendType|statebackend方式，见字典statebackend_type ||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;checkpointTimeout|生成checkpoint的超时时间||false|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;clusterId|映射的集群ID||false|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;clusterName|集群名称||false|string||
|&emsp;&emsp;&emsp;&emsp;clusterType|集群类型，目前默认 0-sdandlone||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;clusterTypeName|集群类型 名称，辅助 转义字段||false|string||
|&emsp;&emsp;&emsp;&emsp;computingUnit|计算单元||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;createBy|创建人||false|string||
|&emsp;&emsp;&emsp;&emsp;createTime|创建时间||false|string(date-time)||
|&emsp;&emsp;&emsp;&emsp;curPage|当前页||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;delFlag|逻辑删除标识 0 废弃 1 正常 ,默认为1，保留字段||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;delayInterval|重启间隔时间(s)||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;enableMinibatchOptimization|开启微型批次聚合 0-不开启 1-开启||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;entryClass|JAR任务运行时使用的入口类||false|string||
|&emsp;&emsp;&emsp;&emsp;failureInterval|计算失败率的时间间隔(m)||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;failureRate|一个时间段内的最大失败次数||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;flinkJobId|集群内运行的作业ID||false|string||
|&emsp;&emsp;&emsp;&emsp;id|主键id||false|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;jobCode|作业编号||false|string||
|&emsp;&emsp;&emsp;&emsp;jobDesc|作业描述||false|string||
|&emsp;&emsp;&emsp;&emsp;jobDsVOS|SQL作业专用 数据源映射||false|array|作业与数据源映射|
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;dsId|选择的数据源类型ID||false|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;dsType|数据源类型  source、sink、side||false|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;id|主键ID||false|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;jobId|对应的作业ID，新增作业的时候默认为0即可||false|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;runSql|特征加工语句，只有数据源类型为sink目标表的时候，才需要填写该值||false|string||
|&emsp;&emsp;&emsp;&emsp;jobName|作业名称||false|string||
|&emsp;&emsp;&emsp;&emsp;jobStatus|作业状态0未运行，1运行中，2取消，3失败, 9提交中 ||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;jobStatusName|作业状态名称 转义辅助字段||false|string||
|&emsp;&emsp;&emsp;&emsp;jobType|作业类型  0-SQL 1-JAR||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;jobTypeName|作业类型名称 辅助转义字段||false|string||
|&emsp;&emsp;&emsp;&emsp;minPauseBetweenCheckpoints|确保检查点之间有1s的时间间隔【checkpoint最小间隔】||false|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;modifyBy|修改人||false|string||
|&emsp;&emsp;&emsp;&emsp;modifyTime|修改时间||false|string(date-time)||
|&emsp;&emsp;&emsp;&emsp;pageSize|页面大小||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;parallelism|并行度||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;parms|自定义参数||false|object||
|&emsp;&emsp;&emsp;&emsp;program|JAR任务运行时使用的参数||false|string||
|&emsp;&emsp;&emsp;&emsp;restartAttempts|尝试重启的次数||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;restartStrategy|重启策略，对应字典 restart_strategy||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;savepointPath|存储点路径||false|string||
|&emsp;&emsp;&emsp;&emsp;stateSavepointsDir|自定义保存点生成根路径||false|string||
|&emsp;&emsp;&emsp;&emsp;tableExecMinibatchAllowlatency|缓冲输入记录最大等待时间(s). 注：若开启微批次聚合，其值必须大于零||false|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;tableExecMinibatchSize|缓冲最大输入记录数.注：若开启微批次聚合，则其值必须为正||false|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;tableOptimizerAggPhaseStrategy|启用局部本地全局聚合. 0-禁用 1-开启||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;tableOptimizerDistinctAggSplitEnabled|启用不同的agg分割. 0-禁用 1-开启||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;timeType|Flink 时间类型，对应字典 flink_time_type||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;useJar|JAR任务运行时使用的JAR文件||false|string||
|&emsp;&emsp;&emsp;&emsp;version|版本号 保留字段||false|string||
|&emsp;&emsp;searchCount||boolean||
|&emsp;&emsp;size||integer(int64)||
|&emsp;&emsp;total||integer(int64)||
|msg|状态信息|string||
|success|请求是否成功|boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"data": {
		"current": 0,
		"pages": 0,
		"records": [
			{
				"allowNrs": 0,
				"checkpointCleanupMode": 0,
				"checkpointInterval": 0,
				"checkpointMaxCuncurrency": 0,
				"checkpointMode": 0,
				"checkpointStatebackendPath": "",
				"checkpointStatebackendType": 0,
				"checkpointTimeout": 0,
				"clusterId": 0,
				"clusterName": "",
				"clusterType": 0,
				"clusterTypeName": "",
				"computingUnit": 0,
				"createBy": "",
				"createTime": "",
				"curPage": 0,
				"delFlag": 0,
				"delayInterval": 0,
				"enableMinibatchOptimization": 0,
				"entryClass": "",
				"failureInterval": 0,
				"failureRate": 0,
				"flinkJobId": "",
				"id": 0,
				"jobCode": "",
				"jobDesc": "",
				"jobDsVOS": [
					{
						"dsId": 0,
						"dsType": "",
						"id": 0,
						"jobId": 0,
						"runSql": ""
					}
				],
				"jobName": "",
				"jobStatus": 0,
				"jobStatusName": "",
				"jobType": 0,
				"jobTypeName": "",
				"minPauseBetweenCheckpoints": 0,
				"modifyBy": "",
				"modifyTime": "",
				"pageSize": 0,
				"parallelism": 0,
				"parms": {},
				"program": "",
				"restartAttempts": 0,
				"restartStrategy": 0,
				"savepointPath": "",
				"stateSavepointsDir": "",
				"tableExecMinibatchAllowlatency": 0,
				"tableExecMinibatchSize": 0,
				"tableOptimizerAggPhaseStrategy": 0,
				"tableOptimizerDistinctAggSplitEnabled": 0,
				"timeType": 0,
				"useJar": "",
				"version": ""
			}
		],
		"searchCount": true,
		"size": 0,
		"total": 0
	},
	"msg": "",
	"success": true
}
```


## 获取作业视图


**接口地址**:`/jobs/overview/{jobId}`


**请求方式**:`GET`


**请求数据类型**:`*`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|jobId|jobId|path|true|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|公共 响应|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|data|数据|object||
|msg|状态信息|string||
|success|请求是否成功|boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"data": {},
	"msg": "",
	"success": true
}
```


## 删除作业


**接口地址**:`/jobs/removeDO`


**请求方式**:`DELETE`


**请求数据类型**:`*`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|作业id|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|公共 响应|
|204|No Content||
|401|Unauthorized||
|403|Forbidden||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|data|数据|object||
|msg|状态信息|string||
|success|请求是否成功|boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"data": {},
	"msg": "",
	"success": true
}
```


## 重启作业


**接口地址**:`/jobs/restart/{jobId}`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|jobId|jobId|path|true|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|公共 响应|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|data|数据|object||
|msg|状态信息|string||
|success|请求是否成功|boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"data": {},
	"msg": "",
	"success": true
}
```


## 批量重启作业


**接口地址**:`/jobs/restartBatch`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|jobIds|jobIds|query|true|array|integer|


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|公共 响应|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|data|数据|object||
|msg|状态信息|string||
|success|请求是否成功|boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"data": {},
	"msg": "",
	"success": true
}
```


## 启动作业


**接口地址**:`/jobs/run/{jobId}`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|jobId|jobId|path|true|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|公共 响应|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|data|数据|object||
|msg|状态信息|string||
|success|请求是否成功|boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"data": {},
	"msg": "",
	"success": true
}
```


## 批量启动作业


**接口地址**:`/jobs/runBatch`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|jobIds|jobIds|query|true|array|integer|


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|公共 响应|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|data|数据|object||
|msg|状态信息|string||
|success|请求是否成功|boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"data": {},
	"msg": "",
	"success": true
}
```


## 触发作业保存点


**接口地址**:`/jobs/savepoint/{jobId}`


**请求方式**:`GET`


**请求数据类型**:`*`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|jobId|jobId|path|true|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|公共 响应|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|data|数据|object||
|msg|状态信息|string||
|success|请求是否成功|boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"data": {},
	"msg": "",
	"success": true
}
```


## 作业信息更新


**接口地址**:`/jobs/updateDO`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
	"allowNrs": 0,
	"checkpointCleanupMode": 0,
	"checkpointInterval": 0,
	"checkpointMaxCuncurrency": 0,
	"checkpointMode": 0,
	"checkpointStatebackendPath": "",
	"checkpointStatebackendType": 0,
	"checkpointTimeout": 0,
	"clusterId": 0,
	"clusterName": "",
	"clusterType": 0,
	"clusterTypeName": "",
	"computingUnit": 0,
	"curPage": 0,
	"delayInterval": 0,
	"enableMinibatchOptimization": 0,
	"entryClass": "",
	"failureInterval": 0,
	"failureRate": 0,
	"id": 0,
	"jobDesc": "",
	"jobDsVOS": [
		{
			"dsId": 0,
			"dsType": "",
			"id": 0,
			"jobId": 0,
			"runSql": ""
		}
	],
	"jobName": "",
	"jobType": 0,
	"jobTypeName": "",
	"minPauseBetweenCheckpoints": 0,
	"pageSize": 0,
	"parallelism": 0,
	"parms": {},
	"program": "",
	"restartAttempts": 0,
	"restartStrategy": 0,
	"savepointPath": "",
	"stateSavepointsDir": "",
	"tableExecMinibatchAllowlatency": 0,
	"tableExecMinibatchSize": 0,
	"tableOptimizerAggPhaseStrategy": 0,
	"tableOptimizerDistinctAggSplitEnabled": 0,
	"timeType": 0,
	"useJar": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|jobVO|jobVO|body|true|作业  视图实体|作业  视图实体|
|&emsp;&emsp;allowNrs|Allow Non Restore State,允许非还原状态||false|integer(int32)||
|&emsp;&emsp;checkpointCleanupMode|checkpoint清除策略，对应字典 checkpoint_cleanup_mode||false|integer(int32)||
|&emsp;&emsp;checkpointInterval|Checkpoint Interval (ms),设置该值则开启检查点，为0则不开启||false|integer(int64)||
|&emsp;&emsp;checkpointMaxCuncurrency|最大并发生成Checkpoint数||false|integer(int32)||
|&emsp;&emsp;checkpointMode|checkpoint模式，对应字典 checkpoint_mode||false|integer(int32)||
|&emsp;&emsp;checkpointStatebackendPath|statebackend存储地址||false|string||
|&emsp;&emsp;checkpointStatebackendType|statebackend方式，见字典statebackend_type ||false|integer(int32)||
|&emsp;&emsp;checkpointTimeout|生成checkpoint的超时时间||false|integer(int64)||
|&emsp;&emsp;clusterId|映射的集群ID||false|integer(int64)||
|&emsp;&emsp;clusterName|集群名称||false|string||
|&emsp;&emsp;clusterType|集群类型，目前默认 0-sdandlone||false|integer(int32)||
|&emsp;&emsp;clusterTypeName|集群类型 名称，辅助 转义字段||false|string||
|&emsp;&emsp;computingUnit|计算单元||false|integer(int32)||
|&emsp;&emsp;createBy|创建人||false|string||
|&emsp;&emsp;createTime|创建时间||false|string(date-time)||
|&emsp;&emsp;curPage|当前页||false|integer(int32)||
|&emsp;&emsp;delFlag|逻辑删除标识 0 废弃 1 正常 ,默认为1，保留字段||false|integer(int32)||
|&emsp;&emsp;delayInterval|重启间隔时间(s)||false|integer(int32)||
|&emsp;&emsp;enableMinibatchOptimization|开启微型批次聚合 0-不开启 1-开启||false|integer(int32)||
|&emsp;&emsp;entryClass|JAR任务运行时使用的入口类||false|string||
|&emsp;&emsp;failureInterval|计算失败率的时间间隔(m)||false|integer(int32)||
|&emsp;&emsp;failureRate|一个时间段内的最大失败次数||false|integer(int32)||
|&emsp;&emsp;flinkJobId|集群内运行的作业ID||false|string||
|&emsp;&emsp;id|主键id||false|integer(int64)||
|&emsp;&emsp;jobCode|作业编号||false|string||
|&emsp;&emsp;jobDesc|作业描述||false|string||
|&emsp;&emsp;jobDsVOS|SQL作业专用 数据源映射||false|array|作业与数据源映射|
|&emsp;&emsp;&emsp;&emsp;dsId|选择的数据源类型ID||false|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;dsType|数据源类型  source、sink、side||false|string||
|&emsp;&emsp;&emsp;&emsp;id|主键ID||false|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;jobId|对应的作业ID，新增作业的时候默认为0即可||false|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;runSql|特征加工语句，只有数据源类型为sink目标表的时候，才需要填写该值||false|string||
|&emsp;&emsp;jobName|作业名称||false|string||
|&emsp;&emsp;jobStatus|作业状态0未运行，1运行中，2取消，3失败, 9提交中 ||false|integer(int32)||
|&emsp;&emsp;jobStatusName|作业状态名称 转义辅助字段||false|string||
|&emsp;&emsp;jobType|作业类型  0-SQL 1-JAR||false|integer(int32)||
|&emsp;&emsp;jobTypeName|作业类型名称 辅助转义字段||false|string||
|&emsp;&emsp;minPauseBetweenCheckpoints|确保检查点之间有1s的时间间隔【checkpoint最小间隔】||false|integer(int64)||
|&emsp;&emsp;modifyBy|修改人||false|string||
|&emsp;&emsp;modifyTime|修改时间||false|string(date-time)||
|&emsp;&emsp;pageSize|页面大小||false|integer(int32)||
|&emsp;&emsp;parallelism|并行度||false|integer(int32)||
|&emsp;&emsp;parms|自定义参数||false|object||
|&emsp;&emsp;program|JAR任务运行时使用的参数||false|string||
|&emsp;&emsp;restartAttempts|尝试重启的次数||false|integer(int32)||
|&emsp;&emsp;restartStrategy|重启策略，对应字典 restart_strategy||false|integer(int32)||
|&emsp;&emsp;savepointPath|存储点路径||false|string||
|&emsp;&emsp;stateSavepointsDir|自定义保存点生成根路径||false|string||
|&emsp;&emsp;tableExecMinibatchAllowlatency|缓冲输入记录最大等待时间(s). 注：若开启微批次聚合，其值必须大于零||false|integer(int64)||
|&emsp;&emsp;tableExecMinibatchSize|缓冲最大输入记录数.注：若开启微批次聚合，则其值必须为正||false|integer(int64)||
|&emsp;&emsp;tableOptimizerAggPhaseStrategy|启用局部本地全局聚合. 0-禁用 1-开启||false|integer(int32)||
|&emsp;&emsp;tableOptimizerDistinctAggSplitEnabled|启用不同的agg分割. 0-禁用 1-开启||false|integer(int32)||
|&emsp;&emsp;timeType|Flink 时间类型，对应字典 flink_time_type||false|integer(int32)||
|&emsp;&emsp;useJar|JAR任务运行时使用的JAR文件||false|string||
|&emsp;&emsp;version|版本号 保留字段||false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|公共 响应|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|data|数据|object||
|msg|状态信息|string||
|success|请求是否成功|boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"data": {},
	"msg": "",
	"success": true
}
```


## 作业详情查询


**接口地址**:`/jobs/{jobId}`


**请求方式**:`GET`


**请求数据类型**:`*`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|jobId|jobId|path|true|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|公共 响应«作业  视图实体»|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|data|数据|作业  视图实体|作业  视图实体|
|&emsp;&emsp;allowNrs|Allow Non Restore State,允许非还原状态|integer(int32)||
|&emsp;&emsp;checkpointCleanupMode|checkpoint清除策略，对应字典 checkpoint_cleanup_mode|integer(int32)||
|&emsp;&emsp;checkpointInterval|Checkpoint Interval (ms),设置该值则开启检查点，为0则不开启|integer(int64)||
|&emsp;&emsp;checkpointMaxCuncurrency|最大并发生成Checkpoint数|integer(int32)||
|&emsp;&emsp;checkpointMode|checkpoint模式，对应字典 checkpoint_mode|integer(int32)||
|&emsp;&emsp;checkpointStatebackendPath|statebackend存储地址|string||
|&emsp;&emsp;checkpointStatebackendType|statebackend方式，见字典statebackend_type |integer(int32)||
|&emsp;&emsp;checkpointTimeout|生成checkpoint的超时时间|integer(int64)||
|&emsp;&emsp;clusterId|映射的集群ID|integer(int64)||
|&emsp;&emsp;clusterName|集群名称|string||
|&emsp;&emsp;clusterType|集群类型，目前默认 0-sdandlone|integer(int32)||
|&emsp;&emsp;clusterTypeName|集群类型 名称，辅助 转义字段|string||
|&emsp;&emsp;computingUnit|计算单元|integer(int32)||
|&emsp;&emsp;createBy|创建人|string||
|&emsp;&emsp;createTime|创建时间|string(date-time)||
|&emsp;&emsp;curPage|当前页|integer(int32)||
|&emsp;&emsp;delFlag|逻辑删除标识 0 废弃 1 正常 ,默认为1，保留字段|integer(int32)||
|&emsp;&emsp;delayInterval|重启间隔时间(s)|integer(int32)||
|&emsp;&emsp;enableMinibatchOptimization|开启微型批次聚合 0-不开启 1-开启|integer(int32)||
|&emsp;&emsp;entryClass|JAR任务运行时使用的入口类|string||
|&emsp;&emsp;failureInterval|计算失败率的时间间隔(m)|integer(int32)||
|&emsp;&emsp;failureRate|一个时间段内的最大失败次数|integer(int32)||
|&emsp;&emsp;flinkJobId|集群内运行的作业ID|string||
|&emsp;&emsp;id|主键id|integer(int64)||
|&emsp;&emsp;jobCode|作业编号|string||
|&emsp;&emsp;jobDesc|作业描述|string||
|&emsp;&emsp;jobDsVOS|SQL作业专用 数据源映射|array|作业与数据源映射|
|&emsp;&emsp;&emsp;&emsp;dsId|选择的数据源类型ID||false|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;dsType|数据源类型  source、sink、side||false|string||
|&emsp;&emsp;&emsp;&emsp;id|主键ID||false|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;jobId|对应的作业ID，新增作业的时候默认为0即可||false|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;runSql|特征加工语句，只有数据源类型为sink目标表的时候，才需要填写该值||false|string||
|&emsp;&emsp;jobName|作业名称|string||
|&emsp;&emsp;jobStatus|作业状态0未运行，1运行中，2取消，3失败, 9提交中 |integer(int32)||
|&emsp;&emsp;jobStatusName|作业状态名称 转义辅助字段|string||
|&emsp;&emsp;jobType|作业类型  0-SQL 1-JAR|integer(int32)||
|&emsp;&emsp;jobTypeName|作业类型名称 辅助转义字段|string||
|&emsp;&emsp;minPauseBetweenCheckpoints|确保检查点之间有1s的时间间隔【checkpoint最小间隔】|integer(int64)||
|&emsp;&emsp;modifyBy|修改人|string||
|&emsp;&emsp;modifyTime|修改时间|string(date-time)||
|&emsp;&emsp;pageSize|页面大小|integer(int32)||
|&emsp;&emsp;parallelism|并行度|integer(int32)||
|&emsp;&emsp;parms|自定义参数|object||
|&emsp;&emsp;program|JAR任务运行时使用的参数|string||
|&emsp;&emsp;restartAttempts|尝试重启的次数|integer(int32)||
|&emsp;&emsp;restartStrategy|重启策略，对应字典 restart_strategy|integer(int32)||
|&emsp;&emsp;savepointPath|存储点路径|string||
|&emsp;&emsp;stateSavepointsDir|自定义保存点生成根路径|string||
|&emsp;&emsp;tableExecMinibatchAllowlatency|缓冲输入记录最大等待时间(s). 注：若开启微批次聚合，其值必须大于零|integer(int64)||
|&emsp;&emsp;tableExecMinibatchSize|缓冲最大输入记录数.注：若开启微批次聚合，则其值必须为正|integer(int64)||
|&emsp;&emsp;tableOptimizerAggPhaseStrategy|启用局部本地全局聚合. 0-禁用 1-开启|integer(int32)||
|&emsp;&emsp;tableOptimizerDistinctAggSplitEnabled|启用不同的agg分割. 0-禁用 1-开启|integer(int32)||
|&emsp;&emsp;timeType|Flink 时间类型，对应字典 flink_time_type|integer(int32)||
|&emsp;&emsp;useJar|JAR任务运行时使用的JAR文件|string||
|&emsp;&emsp;version|版本号 保留字段|string||
|msg|状态信息|string||
|success|请求是否成功|boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"data": {
		"allowNrs": 0,
		"checkpointCleanupMode": 0,
		"checkpointInterval": 0,
		"checkpointMaxCuncurrency": 0,
		"checkpointMode": 0,
		"checkpointStatebackendPath": "",
		"checkpointStatebackendType": 0,
		"checkpointTimeout": 0,
		"clusterId": 0,
		"clusterName": "",
		"clusterType": 0,
		"clusterTypeName": "",
		"computingUnit": 0,
		"createBy": "",
		"createTime": "",
		"curPage": 0,
		"delFlag": 0,
		"delayInterval": 0,
		"enableMinibatchOptimization": 0,
		"entryClass": "",
		"failureInterval": 0,
		"failureRate": 0,
		"flinkJobId": "",
		"id": 0,
		"jobCode": "",
		"jobDesc": "",
		"jobDsVOS": [
			{
				"dsId": 0,
				"dsType": "",
				"id": 0,
				"jobId": 0,
				"runSql": ""
			}
		],
		"jobName": "",
		"jobStatus": 0,
		"jobStatusName": "",
		"jobType": 0,
		"jobTypeName": "",
		"minPauseBetweenCheckpoints": 0,
		"modifyBy": "",
		"modifyTime": "",
		"pageSize": 0,
		"parallelism": 0,
		"parms": {},
		"program": "",
		"restartAttempts": 0,
		"restartStrategy": 0,
		"savepointPath": "",
		"stateSavepointsDir": "",
		"tableExecMinibatchAllowlatency": 0,
		"tableExecMinibatchSize": 0,
		"tableOptimizerAggPhaseStrategy": 0,
		"tableOptimizerDistinctAggSplitEnabled": 0,
		"timeType": 0,
		"useJar": "",
		"version": ""
	},
	"msg": "",
	"success": true
}
```


## 作业日志


**接口地址**:`/jobs/{jobId}/log`


**请求方式**:`GET`


**请求数据类型**:`*`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|jobId|jobId|path|true|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|公共 响应|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|data|数据|object||
|msg|状态信息|string||
|success|请求是否成功|boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"data": {},
	"msg": "",
	"success": true
}
```


# 字典


## 获取虚拟ID


**接口地址**:`/dict/getSnowflakeId`


**请求方式**:`GET`


**请求数据类型**:`*`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


暂无


**响应示例**:
```javascript

```


## 字典列表 接口


**接口地址**:`/dict/list`


**请求方式**:`GET`


**请求数据类型**:`*`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|公共 响应«Map«string,List«字典表»»»|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|data|数据|array|字典表|
|&emsp;&emsp;createBy|创建人|string||
|&emsp;&emsp;createTime|创建时间|string(date-time)||
|&emsp;&emsp;cssClass|样式属性（其他样式扩展）|string||
|&emsp;&emsp;dictLabel|字典标签|string||
|&emsp;&emsp;dictName|字典名称|string||
|&emsp;&emsp;dictSort|字典排序|integer(int32)||
|&emsp;&emsp;dictType|字典类型|string||
|&emsp;&emsp;dictValue|字典键值|string||
|&emsp;&emsp;id|主键|integer(int64)||
|&emsp;&emsp;isDefault|是否默认（Y是 N否）|string||
|&emsp;&emsp;listClass|表格回显样式|string||
|&emsp;&emsp;modifyBy|修改人|string||
|&emsp;&emsp;modifyTime|修改时间|string(date-time)||
|&emsp;&emsp;remark|备注|string||
|&emsp;&emsp;status|状态（0正常 1停用）|string||
|msg|状态信息|string||
|success|请求是否成功|boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"data": {
		"additionalProperties1": [
			{
				"createBy": "",
				"createTime": "",
				"cssClass": "",
				"dictLabel": "",
				"dictName": "",
				"dictSort": 0,
				"dictType": "",
				"dictValue": "",
				"id": 0,
				"isDefault": "",
				"listClass": "",
				"modifyBy": "",
				"modifyTime": "",
				"remark": "",
				"status": ""
			}
		]
	},
	"msg": "",
	"success": true
}
```


## 根据字典类型获取字典集合 接口


**接口地址**:`/dict/list/{dictType}`


**请求方式**:`GET`


**请求数据类型**:`*`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|dictType|字典类型|path|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|公共 响应«List«字典表»»|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|data|数据|array|字典表|
|&emsp;&emsp;createBy|创建人|string||
|&emsp;&emsp;createTime|创建时间|string(date-time)||
|&emsp;&emsp;cssClass|样式属性（其他样式扩展）|string||
|&emsp;&emsp;dictLabel|字典标签|string||
|&emsp;&emsp;dictName|字典名称|string||
|&emsp;&emsp;dictSort|字典排序|integer(int32)||
|&emsp;&emsp;dictType|字典类型|string||
|&emsp;&emsp;dictValue|字典键值|string||
|&emsp;&emsp;id|主键|integer(int64)||
|&emsp;&emsp;isDefault|是否默认（Y是 N否）|string||
|&emsp;&emsp;listClass|表格回显样式|string||
|&emsp;&emsp;modifyBy|修改人|string||
|&emsp;&emsp;modifyTime|修改时间|string(date-time)||
|&emsp;&emsp;remark|备注|string||
|&emsp;&emsp;status|状态（0正常 1停用）|string||
|msg|状态信息|string||
|success|请求是否成功|boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"data": [
		{
			"createBy": "",
			"createTime": "",
			"cssClass": "",
			"dictLabel": "",
			"dictName": "",
			"dictSort": 0,
			"dictType": "",
			"dictValue": "",
			"id": 0,
			"isDefault": "",
			"listClass": "",
			"modifyBy": "",
			"modifyTime": "",
			"remark": "",
			"status": ""
		}
	],
	"msg": "",
	"success": true
}
```


# 总览


## 集群总览接口


**接口地址**:`/dashboard/clusterInfo`


**请求方式**:`GET`


**请求数据类型**:`*`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|clusterId|clusterId|query|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|公共 响应«ClusterOverviewWithVersion»|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|data|数据|ClusterOverviewWithVersion|ClusterOverviewWithVersion|
|&emsp;&emsp;flinkCommit||string||
|&emsp;&emsp;flinkVersion||string||
|&emsp;&emsp;jobsCancelled||integer(int32)||
|&emsp;&emsp;jobsFailed||integer(int32)||
|&emsp;&emsp;jobsFinished||integer(int32)||
|&emsp;&emsp;jobsRunning||integer(int32)||
|&emsp;&emsp;slotsAvailable||integer(int32)||
|&emsp;&emsp;slotsTotal||integer(int32)||
|&emsp;&emsp;taskmanagers||integer(int32)||
|msg|状态信息|string||
|success|请求是否成功|boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"data": {
		"flinkCommit": "",
		"flinkVersion": "",
		"jobsCancelled": 0,
		"jobsFailed": 0,
		"jobsFinished": 0,
		"jobsRunning": 0,
		"slotsAvailable": 0,
		"slotsTotal": 0,
		"taskmanagers": 0
	},
	"msg": "",
	"success": true
}
```


# 数据源管理


## 新增动作


**接口地址**:`/ds/addDO`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:添加数据源


**请求示例**:


```javascript
{
	"curPage": 0,
	"dsName": "",
	"dsSchemaColumnVOS": [
		null
	],
	"dsType": "",
	"dsVersion": 0,
	"id": 0,
	"pageSize": 0,
	"parms": {},
	"schemaFile": "",
	"schemaType": 0,
	"structureType": 0,
	"tableName": "",
	"tableType": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|dsVO|dsVO|body|true|数据源  视图实体|数据源  视图实体|
|&emsp;&emsp;createBy|创建人||false|string||
|&emsp;&emsp;createTime|创建时间||false|string(date-time)||
|&emsp;&emsp;curPage|当前页||false|integer(int32)||
|&emsp;&emsp;ddlEnable|是否启用ddl,默认0 启用。1-不启用。目前 维表不使用ddl。保留该字段||false|integer(int32)||
|&emsp;&emsp;dsDdl|ddl建表语句，维表不需要||false|string||
|&emsp;&emsp;dsName|数据源名称||false|string||
|&emsp;&emsp;dsSchemaColumnVOS|schema列投影 业务实体对象||false|array|数据源列投影  视图实体|
|&emsp;&emsp;&emsp;&emsp;basicType|基础数据类型||false|string||
|&emsp;&emsp;&emsp;&emsp;comment|描述||false|string||
|&emsp;&emsp;&emsp;&emsp;dsId|关联数据源ID，指定所属数据源||false|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;eventTime|是否是事件时间||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;flinkType|Flink SQL属性类型||false|string||
|&emsp;&emsp;&emsp;&emsp;id|主键id||false|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;joinKey|是否是连接key,只有维表类型需要该字段||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;level|层级||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;name|字段属性名||false|string||
|&emsp;&emsp;&emsp;&emsp;res1|备用字段1||false|string||
|&emsp;&emsp;&emsp;&emsp;res2|备用字段2||false|string||
|&emsp;&emsp;&emsp;&emsp;res3|备用字段3||false|string||
|&emsp;&emsp;&emsp;&emsp;res4|备用字段4||false|string||
|&emsp;&emsp;&emsp;&emsp;res5|备用字段5||false|string||
|&emsp;&emsp;&emsp;&emsp;virtualId|虚拟id，采用雪花算法||false|string||
|&emsp;&emsp;&emsp;&emsp;virtualPid|虚拟父级id，默认顶级为0||false|string||
|&emsp;&emsp;dsType|数据源类型 source-源表 sink-结果表 side-维表 udf-UDF自定义函数||false|string||
|&emsp;&emsp;dsVersion|版本号，保留字段||false|integer(int32)||
|&emsp;&emsp;id|主键id||false|integer(int64)||
|&emsp;&emsp;jsonFieldVO|各个表类型特定属性值 业务实体对象||false|数据源特定表类型对应的属性模版定义  视图实体|数据源特定表类型对应的属性模版定义  视图实体|
|&emsp;&emsp;&emsp;&emsp;dsId|关联数据源定义表t_ds主键||false|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;id|主键id||false|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;jsonValue|属性值||false|array|JsonField|
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;defaultValue|||false|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;describe|||false|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;fieldName|||false|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;fieldValue|||false|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;label|||false|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;maxLength|||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;minLength|||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;multiple|||false|boolean||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;option|||false|array|Option|
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;dName|||false|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;hasUnion|||false|boolean||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;name|||false|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;unionFields|||false|array|UnionField|
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;defaultValue|||false|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;describe|||false|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;fieldName|||false|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;fieldValue|||false|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;hidden|||false|boolean||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;label|||false|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;maxLength|||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;minLength|||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;readOnly|||false|boolean||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;required|||false|boolean||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;sequence|||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;tips|||false|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;type|||false|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;readOnly|||false|boolean||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;required|||false|boolean||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;sequence|||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;tips|||false|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;type|||false|string||
|&emsp;&emsp;modifyBy|修改人||false|string||
|&emsp;&emsp;modifyTime|修改时间||false|string(date-time)||
|&emsp;&emsp;pageSize|页面大小||false|integer(int32)||
|&emsp;&emsp;parms|自定义参数||false|object||
|&emsp;&emsp;schemaFile|schema文件内容||false|string||
|&emsp;&emsp;schemaType|schema类型，默认0-json  , 1-avro||false|integer(int32)||
|&emsp;&emsp;structureType|结构类型，单一结构-0或者多层嵌套结构-1||false|integer(int32)||
|&emsp;&emsp;tableName|表的别名，用于特征加工||false|string||
|&emsp;&emsp;tableType|表类型，见字典表 dc_type_source、dc_type_side、dc_type_sink||false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|公共 响应|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|data|数据|object||
|msg|状态信息|string||
|success|请求是否成功|boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"data": {},
	"msg": "",
	"success": true
}
```


## 校验表的别名


**接口地址**:`/ds/checkTableName`


**请求方式**:`GET`


**请求数据类型**:`*`


**响应数据类型**:`*/*`


**接口描述**:校验表的别名是否已经存在


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|tableName|别名|body|true|String|String|
|dsId|如果是修改，需要传入本身ID，用于排除自身|query|false|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|公共 响应|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|data|数据|object||
|msg|状态信息|string||
|success|请求是否成功|boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"data": {},
	"msg": "",
	"success": true
}
```


## 数据源明细


**接口地址**:`/ds/get`


**请求方式**:`GET`


**请求数据类型**:`*`


**响应数据类型**:`*/*,application/json`


**接口描述**:数据源明细


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|主键|query|true|integer(int64)||
|curPage|当前页|query|false|integer(int32)||
|dsName|数据源名称|query|false|string||
|dsType|数据源类型 source-源表 sink-结果表 side-维表 udf-UDF自定义函数|query|false|string||
|dsVersion|版本号，保留字段|query|false|integer(int32)||
|pageSize|页面大小|query|false|integer(int32)||
|parms|自定义参数|query|false|object||
|schemaFile|schema文件内容|query|false|string||
|schemaType|schema类型，默认0-json  , 1-avro|query|false|integer(int32)||
|structureType|结构类型，单一结构-0或者多层嵌套结构-1|query|false|integer(int32)||
|tableName|表的别名，用于特征加工|query|false|string||
|tableType|表类型，见字典表 dc_type_source、dc_type_side、dc_type_sink|query|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|公共 响应«数据源  视图实体»|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|data|数据|数据源  视图实体|数据源  视图实体|
|&emsp;&emsp;createBy|创建人|string||
|&emsp;&emsp;createTime|创建时间|string(date-time)||
|&emsp;&emsp;curPage|当前页|integer(int32)||
|&emsp;&emsp;ddlEnable|是否启用ddl,默认0 启用。1-不启用。目前 维表不使用ddl。保留该字段|integer(int32)||
|&emsp;&emsp;dsDdl|ddl建表语句，维表不需要|string||
|&emsp;&emsp;dsName|数据源名称|string||
|&emsp;&emsp;dsSchemaColumnVOS|schema列投影 业务实体对象|array|数据源列投影  视图实体|
|&emsp;&emsp;&emsp;&emsp;basicType|基础数据类型||false|string||
|&emsp;&emsp;&emsp;&emsp;comment|描述||false|string||
|&emsp;&emsp;&emsp;&emsp;dsId|关联数据源ID，指定所属数据源||false|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;eventTime|是否是事件时间||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;flinkType|Flink SQL属性类型||false|string||
|&emsp;&emsp;&emsp;&emsp;id|主键id||false|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;joinKey|是否是连接key,只有维表类型需要该字段||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;level|层级||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;name|字段属性名||false|string||
|&emsp;&emsp;&emsp;&emsp;res1|备用字段1||false|string||
|&emsp;&emsp;&emsp;&emsp;res2|备用字段2||false|string||
|&emsp;&emsp;&emsp;&emsp;res3|备用字段3||false|string||
|&emsp;&emsp;&emsp;&emsp;res4|备用字段4||false|string||
|&emsp;&emsp;&emsp;&emsp;res5|备用字段5||false|string||
|&emsp;&emsp;&emsp;&emsp;virtualId|虚拟id，采用雪花算法||false|string||
|&emsp;&emsp;&emsp;&emsp;virtualPid|虚拟父级id，默认顶级为0||false|string||
|&emsp;&emsp;dsType|数据源类型 source-源表 sink-结果表 side-维表 udf-UDF自定义函数|string||
|&emsp;&emsp;dsVersion|版本号，保留字段|integer(int32)||
|&emsp;&emsp;id|主键id|integer(int64)||
|&emsp;&emsp;jsonFieldVO|各个表类型特定属性值 业务实体对象|数据源特定表类型对应的属性模版定义  视图实体|数据源特定表类型对应的属性模版定义  视图实体|
|&emsp;&emsp;&emsp;&emsp;dsId|关联数据源定义表t_ds主键||false|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;id|主键id||false|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;jsonValue|属性值||false|array|JsonField|
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;defaultValue|||false|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;describe|||false|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;fieldName|||false|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;fieldValue|||false|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;label|||false|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;maxLength|||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;minLength|||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;multiple|||false|boolean||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;option|||false|array|Option|
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;dName|||false|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;hasUnion|||false|boolean||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;name|||false|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;unionFields|||false|array|UnionField|
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;defaultValue|||false|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;describe|||false|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;fieldName|||false|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;fieldValue|||false|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;hidden|||false|boolean||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;label|||false|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;maxLength|||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;minLength|||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;readOnly|||false|boolean||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;required|||false|boolean||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;sequence|||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;tips|||false|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;type|||false|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;readOnly|||false|boolean||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;required|||false|boolean||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;sequence|||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;tips|||false|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;type|||false|string||
|&emsp;&emsp;modifyBy|修改人|string||
|&emsp;&emsp;modifyTime|修改时间|string(date-time)||
|&emsp;&emsp;pageSize|页面大小|integer(int32)||
|&emsp;&emsp;parms|自定义参数|object||
|&emsp;&emsp;schemaFile|schema文件内容|string||
|&emsp;&emsp;schemaType|schema类型，默认0-json  , 1-avro|integer(int32)||
|&emsp;&emsp;structureType|结构类型，单一结构-0或者多层嵌套结构-1|integer(int32)||
|&emsp;&emsp;tableName|表的别名，用于特征加工|string||
|&emsp;&emsp;tableType|表类型，见字典表 dc_type_source、dc_type_side、dc_type_sink|string||
|msg|状态信息|string||
|success|请求是否成功|boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"data": {
		"createBy": "",
		"createTime": "",
		"curPage": 0,
		"ddlEnable": 0,
		"dsDdl": "",
		"dsName": "",
		"dsSchemaColumnVOS": [
			{
				"basicType": "",
				"comment": "",
				"dsId": 0,
				"eventTime": 0,
				"flinkType": "",
				"id": 0,
				"joinKey": 0,
				"level": 0,
				"name": "",
				"res1": "",
				"res2": "",
				"res3": "",
				"res4": "",
				"res5": "",
				"virtualId": "",
				"virtualPid": ""
			}
		],
		"dsType": "",
		"dsVersion": 0,
		"id": 0,
		"jsonFieldVO": {
			"dsId": 0,
			"id": 0,
			"jsonValue": [
				{
					"defaultValue": "",
					"describe": "",
					"fieldName": "",
					"fieldValue": "",
					"label": "",
					"maxLength": 0,
					"minLength": 0,
					"multiple": true,
					"option": [
						{
							"dName": "",
							"hasUnion": true,
							"name": "",
							"unionFields": [
								{
									"defaultValue": "",
									"describe": "",
									"fieldName": "",
									"fieldValue": "",
									"hidden": true,
									"label": "",
									"maxLength": 0,
									"minLength": 0,
									"readOnly": true,
									"required": true,
									"sequence": 0,
									"tips": "",
									"type": ""
								}
							]
						}
					],
					"readOnly": true,
					"required": true,
					"sequence": 0,
					"tips": "",
					"type": ""
				}
			]
		},
		"modifyBy": "",
		"modifyTime": "",
		"pageSize": 0,
		"parms": {},
		"schemaFile": "",
		"schemaType": 0,
		"structureType": 0,
		"tableName": "",
		"tableType": ""
	},
	"msg": "",
	"success": true
}
```


## 获取自定义字段模版


**接口地址**:`/ds/getFieldTemplete`


**请求方式**:`GET`


**请求数据类型**:`*`


**响应数据类型**:`*/*,application/json`


**接口描述**:根据功能类型和表类型 获取不同的属性字段,页面调用补充界面属性


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|dsType|功能类型|query|true|string||
|tableType|表类型|query|true|string||
|curPage|当前页|query|false|integer(int32)||
|dsName|数据源名称|query|false|string||
|dsVersion|版本号，保留字段|query|false|integer(int32)||
|id|主键id|query|false|integer(int64)||
|pageSize|页面大小|query|false|integer(int32)||
|parms|自定义参数|query|false|object||
|schemaType|schema类型，默认0-json  , 1-avro|query|false|integer(int32)||
|structureType|结构类型，单一结构-0或者多层嵌套结构-1|query|false|integer(int32)||
|tableName|表的别名，用于特征加工|query|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|公共 响应|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|data|数据|object||
|msg|状态信息|string||
|success|请求是否成功|boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"data": {},
	"msg": "",
	"success": true
}
```


## 获取支持的数据类型


**接口地址**:`/ds/getTypeList`


**请求方式**:`GET`


**请求数据类型**:`*`


**响应数据类型**:`*/*`


**接口描述**:列投影，获取支持的数据类型,flinkSQLTypes：Flink SQL支持的数据类型，basicTypes：基础数据类型


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|公共 响应«Map»|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|data|数据|object||
|msg|状态信息|string||
|success|请求是否成功|boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"data": {},
	"msg": "",
	"success": true
}
```


## 列表查询


**接口地址**:`/ds/list`


**请求方式**:`GET`


**请求数据类型**:`*`


**响应数据类型**:`*/*,application/json`


**接口描述**:数据源列表


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|dsType|数据源类型,见字典ds_type|query|true|string||
|curPage|当前页|query|false|integer(int32)||
|dsName|数据源名称|query|false|string||
|dsVersion|版本号，保留字段|query|false|integer(int32)||
|pageSize|页面大小|query|false|integer(int32)||
|parms|自定义参数|query|false|object||
|schemaType|schema类型，默认0-json  , 1-avro|query|false|integer(int32)||
|structureType|结构类型，单一结构-0或者多层嵌套结构-1|query|false|integer(int32)||
|tableName|表的别名，用于特征加工|query|false|string||
|tableType|表类型，见字典表 dc_type_source、dc_type_side、dc_type_sink|query|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|公共 响应«IPage«数据源  持久层实体»»|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|data|数据|IPage«数据源  持久层实体»|IPage«数据源  持久层实体»|
|&emsp;&emsp;current||integer(int64)||
|&emsp;&emsp;pages||integer(int64)||
|&emsp;&emsp;records||array|数据源  持久层实体|
|&emsp;&emsp;&emsp;&emsp;createBy|创建人||false|string||
|&emsp;&emsp;&emsp;&emsp;createTime|创建时间||false|string(date-time)||
|&emsp;&emsp;&emsp;&emsp;ddlEnable|是否启用ddl,默认0 启用。1-不启用。目前 维表不使用ddl。保留该字段||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;dsDdl|ddl建表语句||false|string||
|&emsp;&emsp;&emsp;&emsp;dsName|数据源名称||false|string||
|&emsp;&emsp;&emsp;&emsp;dsType|数据源类型 0-源表 1-结果表 2-维表||false|string||
|&emsp;&emsp;&emsp;&emsp;dsVersion|版本号||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;id|主键||false|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;modifyBy|修改人||false|string||
|&emsp;&emsp;&emsp;&emsp;modifyTime|修改时间||false|string(date-time)||
|&emsp;&emsp;&emsp;&emsp;schemaFile|schema文件内容||false|string||
|&emsp;&emsp;&emsp;&emsp;schemaType|schema_type,默认0 json||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;structureType|结构类型，单一结构-0或者多层嵌套结构-1||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;tableName|别名，用于SQL加工||false|string||
|&emsp;&emsp;&emsp;&emsp;tableType|表类型，见字典表 dc_type_source、side、slink||false|string||
|&emsp;&emsp;searchCount||boolean||
|&emsp;&emsp;size||integer(int64)||
|&emsp;&emsp;total||integer(int64)||
|msg|状态信息|string||
|success|请求是否成功|boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"data": {
		"current": 0,
		"pages": 0,
		"records": [
			{
				"createBy": "",
				"createTime": "",
				"ddlEnable": 0,
				"dsDdl": "",
				"dsName": "",
				"dsType": "",
				"dsVersion": 0,
				"id": 0,
				"modifyBy": "",
				"modifyTime": "",
				"schemaFile": "",
				"schemaType": 0,
				"structureType": 0,
				"tableName": "",
				"tableType": ""
			}
		],
		"searchCount": true,
		"size": 0,
		"total": 0
	},
	"msg": "",
	"success": true
}
```


## 修改动作


**接口地址**:`/ds/modifyDO`


**请求方式**:`PUT`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:修改数据源


**请求示例**:


```javascript
{
	"createBy": "",
	"createTime": "",
	"curPage": 0,
	"ddlEnable": 0,
	"dsDdl": "",
	"dsName": "",
	"dsSchemaColumnVOS": [
		{
			"basicType": "",
			"comment": "",
			"dsId": 0,
			"eventTime": 0,
			"flinkType": "",
			"id": 0,
			"joinKey": 0,
			"level": 0,
			"name": "",
			"res1": "",
			"res2": "",
			"res3": "",
			"res4": "",
			"res5": "",
			"virtualId": "",
			"virtualPid": ""
		}
	],
	"dsType": "",
	"dsVersion": 0,
	"id": 0,
	"jsonFieldVO": {
		"dsId": 0,
		"id": 0,
		"jsonValue": [
			{
				"defaultValue": "",
				"describe": "",
				"fieldName": "",
				"fieldValue": "",
				"label": "",
				"maxLength": 0,
				"minLength": 0,
				"multiple": true,
				"option": [
					{
						"dName": "",
						"hasUnion": true,
						"name": "",
						"unionFields": [
							{
								"defaultValue": "",
								"describe": "",
								"fieldName": "",
								"fieldValue": "",
								"hidden": true,
								"label": "",
								"maxLength": 0,
								"minLength": 0,
								"readOnly": true,
								"required": true,
								"sequence": 0,
								"tips": "",
								"type": ""
							}
						]
					}
				],
				"readOnly": true,
				"required": true,
				"sequence": 0,
				"tips": "",
				"type": ""
			}
		]
	},
	"modifyBy": "",
	"modifyTime": "",
	"pageSize": 0,
	"parms": {},
	"schemaFile": "",
	"schemaType": 0,
	"structureType": 0,
	"tableName": "",
	"tableType": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|dsVO|dsVO|body|true|数据源  视图实体|数据源  视图实体|
|&emsp;&emsp;createBy|创建人||false|string||
|&emsp;&emsp;createTime|创建时间||false|string(date-time)||
|&emsp;&emsp;curPage|当前页||false|integer(int32)||
|&emsp;&emsp;ddlEnable|是否启用ddl,默认0 启用。1-不启用。目前 维表不使用ddl。保留该字段||false|integer(int32)||
|&emsp;&emsp;dsDdl|ddl建表语句，维表不需要||false|string||
|&emsp;&emsp;dsName|数据源名称||false|string||
|&emsp;&emsp;dsSchemaColumnVOS|schema列投影 业务实体对象||false|array|数据源列投影  视图实体|
|&emsp;&emsp;&emsp;&emsp;basicType|基础数据类型||false|string||
|&emsp;&emsp;&emsp;&emsp;comment|描述||false|string||
|&emsp;&emsp;&emsp;&emsp;dsId|关联数据源ID，指定所属数据源||false|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;eventTime|是否是事件时间||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;flinkType|Flink SQL属性类型||false|string||
|&emsp;&emsp;&emsp;&emsp;id|主键id||false|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;joinKey|是否是连接key,只有维表类型需要该字段||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;level|层级||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;name|字段属性名||false|string||
|&emsp;&emsp;&emsp;&emsp;res1|备用字段1||false|string||
|&emsp;&emsp;&emsp;&emsp;res2|备用字段2||false|string||
|&emsp;&emsp;&emsp;&emsp;res3|备用字段3||false|string||
|&emsp;&emsp;&emsp;&emsp;res4|备用字段4||false|string||
|&emsp;&emsp;&emsp;&emsp;res5|备用字段5||false|string||
|&emsp;&emsp;&emsp;&emsp;virtualId|虚拟id，采用雪花算法||false|string||
|&emsp;&emsp;&emsp;&emsp;virtualPid|虚拟父级id，默认顶级为0||false|string||
|&emsp;&emsp;dsType|数据源类型 source-源表 sink-结果表 side-维表 udf-UDF自定义函数||false|string||
|&emsp;&emsp;dsVersion|版本号，保留字段||false|integer(int32)||
|&emsp;&emsp;id|主键id||false|integer(int64)||
|&emsp;&emsp;jsonFieldVO|各个表类型特定属性值 业务实体对象||false|数据源特定表类型对应的属性模版定义  视图实体|数据源特定表类型对应的属性模版定义  视图实体|
|&emsp;&emsp;&emsp;&emsp;dsId|关联数据源定义表t_ds主键||false|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;id|主键id||false|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;jsonValue|属性值||false|array|JsonField|
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;defaultValue|||false|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;describe|||false|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;fieldName|||false|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;fieldValue|||false|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;label|||false|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;maxLength|||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;minLength|||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;multiple|||false|boolean||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;option|||false|array|Option|
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;dName|||false|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;hasUnion|||false|boolean||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;name|||false|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;unionFields|||false|array|UnionField|
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;defaultValue|||false|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;describe|||false|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;fieldName|||false|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;fieldValue|||false|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;hidden|||false|boolean||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;label|||false|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;maxLength|||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;minLength|||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;readOnly|||false|boolean||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;required|||false|boolean||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;sequence|||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;tips|||false|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;type|||false|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;readOnly|||false|boolean||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;required|||false|boolean||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;sequence|||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;tips|||false|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;type|||false|string||
|&emsp;&emsp;modifyBy|修改人||false|string||
|&emsp;&emsp;modifyTime|修改时间||false|string(date-time)||
|&emsp;&emsp;pageSize|页面大小||false|integer(int32)||
|&emsp;&emsp;parms|自定义参数||false|object||
|&emsp;&emsp;schemaFile|schema文件内容||false|string||
|&emsp;&emsp;schemaType|schema类型，默认0-json  , 1-avro||false|integer(int32)||
|&emsp;&emsp;structureType|结构类型，单一结构-0或者多层嵌套结构-1||false|integer(int32)||
|&emsp;&emsp;tableName|表的别名，用于特征加工||false|string||
|&emsp;&emsp;tableType|表类型，见字典表 dc_type_source、dc_type_side、dc_type_sink||false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|公共 响应|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|data|数据|object||
|msg|状态信息|string||
|success|请求是否成功|boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"data": {},
	"msg": "",
	"success": true
}
```


## 删除动作


**接口地址**:`/ds/removeDO`


**请求方式**:`DELETE`


**请求数据类型**:`*`


**响应数据类型**:`*/*`


**接口描述**:删除数据源


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|id|query|false|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|公共 响应|
|204|No Content||
|401|Unauthorized||
|403|Forbidden||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|data|数据|object||
|msg|状态信息|string||
|success|请求是否成功|boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"data": {},
	"msg": "",
	"success": true
}
```


## 上传schema文件


**接口地址**:`/ds/schemaAnalysisDO`


**请求方式**:`POST`


**请求数据类型**:`multipart/form-data`


**响应数据类型**:`*/*`


**接口描述**:提交并解析schema


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|file|文件流对象,接收数组格式|formData|true|file||
|schemaType|schema类型 0-json 1-arvo,详情见字典schema_type|query|true|string||
|structureType|结构类型，单一结构- 0 或者 多层嵌套结构- 1|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|公共 响应«Map»|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|data|数据|object||
|msg|状态信息|string||
|success|请求是否成功|boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"data": {},
	"msg": "",
	"success": true
}
```


# 文件上传下载操作接口


## 通用下载


**接口地址**:`/file/down`


**请求方式**:`GET`


**请求数据类型**:`*`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|fileName|下载的文件名|query|false|string||
|filePath|文件路径|query|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


暂无


**响应示例**:
```javascript

```


# 文件管理


## jar包列表获取(无分页)


**接口地址**:`/jars/list`


**请求方式**:`GET`


**请求数据类型**:`*`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|clusterId||query|false|integer(int64)||
|createBy|创建人|query|false|string||
|createTime|创建时间|query|false|string(date-time)||
|curPage|当前页|query|false|integer(int32)||
|entryClass||query|false|string||
|id||query|false|integer(int64)||
|modifyBy|修改人|query|false|string||
|modifyTime|修改时间|query|false|string(date-time)||
|pageSize|页面大小|query|false|integer(int32)||
|parms|自定义参数|query|false|object||
|resName||query|false|string||
|resSize||query|false|integer(int64)||
|resType||query|false|integer(int32)||
|resUname||query|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|公共 响应«List«jar包资源»»|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|data|数据|array|jar包资源|
|&emsp;&emsp;clusterId|集群ID|integer(int64)||
|&emsp;&emsp;createBy|创建人|string||
|&emsp;&emsp;createTime|创建时间|string(date-time)||
|&emsp;&emsp;entryClass|启动类|string||
|&emsp;&emsp;id|主键|integer(int64)||
|&emsp;&emsp;modifyBy|修改人|string||
|&emsp;&emsp;modifyTime|修改时间|string(date-time)||
|&emsp;&emsp;resName|资源原始名称|string||
|&emsp;&emsp;resSize|文件大小|integer(int64)||
|&emsp;&emsp;resSizeStr||string||
|&emsp;&emsp;resType|资源类型 0:jar|integer(int32)||
|&emsp;&emsp;resUname|资源新名称|string||
|&emsp;&emsp;status|资源状态|integer(int32)||
|msg|状态信息|string||
|success|请求是否成功|boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"data": [
		{
			"clusterId": 0,
			"createBy": "",
			"createTime": "",
			"entryClass": "",
			"id": 0,
			"modifyBy": "",
			"modifyTime": "",
			"resName": "",
			"resSize": 0,
			"resSizeStr": "",
			"resType": 0,
			"resUname": "",
			"status": 0
		}
	],
	"msg": "",
	"success": true
}
```


## jar包列表分页查询


**接口地址**:`/jars/listPage`


**请求方式**:`GET`


**请求数据类型**:`*`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|clusterId||query|false|integer(int64)||
|createBy|创建人|query|false|string||
|createTime|创建时间|query|false|string(date-time)||
|curPage|当前页|query|false|integer(int32)||
|entryClass||query|false|string||
|modifyBy|修改人|query|false|string||
|modifyTime|修改时间|query|false|string(date-time)||
|pageSize|页面大小|query|false|integer(int32)||
|resName||query|false|string||
|resType||query|false|integer(int32)||
|resUname||query|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|公共 响应«IPage«jar包资源»»|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|data|数据|IPage«jar包资源»|IPage«jar包资源»|
|&emsp;&emsp;current||integer(int64)||
|&emsp;&emsp;pages||integer(int64)||
|&emsp;&emsp;records||array|jar包资源|
|&emsp;&emsp;&emsp;&emsp;clusterId|集群ID||false|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;createBy|创建人||false|string||
|&emsp;&emsp;&emsp;&emsp;createTime|创建时间||false|string(date-time)||
|&emsp;&emsp;&emsp;&emsp;entryClass|启动类||false|string||
|&emsp;&emsp;&emsp;&emsp;id|主键||false|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;modifyBy|修改人||false|string||
|&emsp;&emsp;&emsp;&emsp;modifyTime|修改时间||false|string(date-time)||
|&emsp;&emsp;&emsp;&emsp;resName|资源原始名称||false|string||
|&emsp;&emsp;&emsp;&emsp;resSize|文件大小||false|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;resSizeStr|||false|string||
|&emsp;&emsp;&emsp;&emsp;resType|资源类型 0:jar||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;resUname|资源新名称||false|string||
|&emsp;&emsp;&emsp;&emsp;status|资源状态||false|integer(int32)||
|&emsp;&emsp;searchCount||boolean||
|&emsp;&emsp;size||integer(int64)||
|&emsp;&emsp;total||integer(int64)||
|msg|状态信息|string||
|success|请求是否成功|boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"data": {
		"current": 0,
		"pages": 0,
		"records": [
			{
				"clusterId": 0,
				"createBy": "",
				"createTime": "",
				"entryClass": "",
				"id": 0,
				"modifyBy": "",
				"modifyTime": "",
				"resName": "",
				"resSize": 0,
				"resSizeStr": "",
				"resType": 0,
				"resUname": "",
				"status": 0
			}
		],
		"searchCount": true,
		"size": 0,
		"total": 0
	},
	"msg": "",
	"success": true
}
```


## jar包删除


**接口地址**:`/jars/removeDO`


**请求方式**:`DELETE`


**请求数据类型**:`*`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|jarid|jar包Id|query|true|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|公共 响应|
|204|No Content||
|401|Unauthorized||
|403|Forbidden||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|data|数据|object||
|msg|状态信息|string||
|success|请求是否成功|boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"data": {},
	"msg": "",
	"success": true
}
```


## jar包上传


**接口地址**:`/jars/upload`


**请求方式**:`POST`


**请求数据类型**:`multipart/form-data`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|jarfile|jarfile|formData|true|file||
|clusterId|集群ID|query|false|integer(int64)||
|entryClass|启动类|query|false|string||
|resSizeStr||query|false|string||
|resType|资源类型 0:jar|query|false|integer(int32)||
|status|资源状态|query|false|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|公共 响应«JarUploadResponseBody»|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|data|数据|JarUploadResponseBody|JarUploadResponseBody|
|&emsp;&emsp;filename||string||
|&emsp;&emsp;status|可用值:success|string||
|msg|状态信息|string||
|success|请求是否成功|boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"data": {
		"filename": "",
		"status": ""
	},
	"msg": "",
	"success": true
}
```


# 自定义函数


## 自定义函数删除


**接口地址**:`/ds/udf/delete`


**请求方式**:`DELETE`


**请求数据类型**:`*`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|主键id|query|true|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|公共 响应|
|204|No Content||
|401|Unauthorized||
|403|Forbidden||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|data|数据|object||
|msg|状态信息|string||
|success|请求是否成功|boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"data": {},
	"msg": "",
	"success": true
}
```


## 自定义函数分页列表


**接口地址**:`/ds/udf/list`


**请求方式**:`GET`


**请求数据类型**:`*`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|createBy|创建人|query|false|string||
|createTime|创建时间|query|false|string(date-time)||
|curPage|当前页|query|false|integer(int32)||
|modifyBy|修改人|query|false|string||
|modifyTime|修改时间|query|false|string(date-time)||
|pageSize|页面大小|query|false|integer(int32)||
|udfClass||query|false|string||
|udfDesc||query|false|string||
|udfName||query|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|公共 响应«IPage«自定义函数对象»»|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|data|数据|IPage«自定义函数对象»|IPage«自定义函数对象»|
|&emsp;&emsp;current||integer(int64)||
|&emsp;&emsp;pages||integer(int64)||
|&emsp;&emsp;records||array|自定义函数对象|
|&emsp;&emsp;&emsp;&emsp;createBy|创建人||false|string||
|&emsp;&emsp;&emsp;&emsp;createTime|创建时间||false|string(date-time)||
|&emsp;&emsp;&emsp;&emsp;deleteFlag|删除标记(0:保存 1:已删除)||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;id|主键||false|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;jarName|jar包名称||false|string||
|&emsp;&emsp;&emsp;&emsp;modifyBy|修改人||false|string||
|&emsp;&emsp;&emsp;&emsp;modifyTime|修改时间||false|string(date-time)||
|&emsp;&emsp;&emsp;&emsp;udfClass|加载的class||false|string||
|&emsp;&emsp;&emsp;&emsp;udfDesc|函数描述||false|string||
|&emsp;&emsp;&emsp;&emsp;udfName|函数名||false|string||
|&emsp;&emsp;&emsp;&emsp;udfPath|存储路径||false|string||
|&emsp;&emsp;&emsp;&emsp;udfType|自定义函数类型 SCALA ,TABLE,AGGREGATE,TABLEAGG||false|string||
|&emsp;&emsp;searchCount||boolean||
|&emsp;&emsp;size||integer(int64)||
|&emsp;&emsp;total||integer(int64)||
|msg|状态信息|string||
|success|请求是否成功|boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"data": {
		"current": 0,
		"pages": 0,
		"records": [
			{
				"createBy": "",
				"createTime": "",
				"deleteFlag": 0,
				"id": 0,
				"jarName": "",
				"modifyBy": "",
				"modifyTime": "",
				"udfClass": "",
				"udfDesc": "",
				"udfName": "",
				"udfPath": "",
				"udfType": ""
			}
		],
		"searchCount": true,
		"size": 0,
		"total": 0
	},
	"msg": "",
	"success": true
}
```


## 自定义函数更新


**接口地址**:`/ds/udf/update`


**请求方式**:`PUT`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|主键|query|false|integer(int64)||
|udfDesc|函数描述|query|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|公共 响应|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|data|数据|object||
|msg|状态信息|string||
|success|请求是否成功|boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"data": {},
	"msg": "",
	"success": true
}
```


## 自定义函数jar包上传


**接口地址**:`/ds/udf/upload`


**请求方式**:`POST`


**请求数据类型**:`multipart/form-data`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|jarfile|jarfile|formData|true|file||
|udfClass|加载的class|query|false|string||
|udfDesc|函数描述|query|false|string||
|udfName|函数名|query|false|string||
|udfType|自定义函数类型 SCALA ,TABLE,AGGREGATE,TABLEAGG|query|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|公共 响应«JarUploadResponseBody»|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|data|数据|JarUploadResponseBody|JarUploadResponseBody|
|&emsp;&emsp;filename||string||
|&emsp;&emsp;status|可用值:success|string||
|msg|状态信息|string||
|success|请求是否成功|boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"data": {
		"filename": "",
		"status": ""
	},
	"msg": "",
	"success": true
}
```


# 辅助文档接口


## 根据字典类型获取字典集合 接口


**接口地址**:`/doc/get/{key}`


**请求方式**:`GET`


**请求数据类型**:`*`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|key|文档key|path|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|公共 响应«string»|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|data|数据|string||
|msg|状态信息|string||
|success|请求是否成功|boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"data": "",
	"msg": "",
	"success": true
}
```


# 集群管理


## 集群添加


**接口地址**:`/cluster/add`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|address|集群地址|query|true|string||
|name|集群名称|query|true|string||
|type|集群类型|query|true|string||
|remark|备注|query|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|公共 响应|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|data|数据|object||
|msg|状态信息|string||
|success|请求是否成功|boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"data": {},
	"msg": "",
	"success": true
}
```


## 集群下拉列表查询(无分页)


**接口地址**:`/cluster/check/list`


**请求方式**:`GET`


**请求数据类型**:`*`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|address||query|false|string||
|createBy|创建人|query|false|string||
|createTime|创建时间|query|false|string(date-time)||
|curPage|当前页|query|false|integer(int32)||
|delFlag||query|false|integer(int32)||
|id||query|false|integer(int64)||
|jobCancelCount||query|false|integer(int32)||
|jobCompleteCount||query|false|integer(int32)||
|jobFailureCount||query|false|integer(int32)||
|modifyBy|修改人|query|false|string||
|modifyTime|修改时间|query|false|string(date-time)||
|name||query|false|string||
|pageSize|页面大小|query|false|integer(int32)||
|parms|自定义参数|query|false|object||
|remark||query|false|string||
|slotAvailableCount||query|false|integer(int32)||
|slotRunningCount||query|false|integer(int32)||
|slotTotal||query|false|integer(int32)||
|taskManagerCount||query|false|integer(int32)||
|type||query|false|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|公共 响应«List«集群»»|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|data|数据|array|集群|
|&emsp;&emsp;address|集群地址|string||
|&emsp;&emsp;createBy|创建人|string||
|&emsp;&emsp;createTime|创建时间|string(date-time)||
|&emsp;&emsp;delFlag|删除标识，逻辑删除。0 未启用,1 启用,-1删除|integer(int32)||
|&emsp;&emsp;id|主键|integer(int64)||
|&emsp;&emsp;modifyBy|修改人|string||
|&emsp;&emsp;modifyTime|修改时间|string(date-time)||
|&emsp;&emsp;name|集群名称|string||
|&emsp;&emsp;remark|备注|string||
|&emsp;&emsp;type|集群类型,枚举|integer(int32)||
|msg|状态信息|string||
|success|请求是否成功|boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"data": [
		{
			"address": "",
			"createBy": "",
			"createTime": "",
			"delFlag": 0,
			"id": 0,
			"modifyBy": "",
			"modifyTime": "",
			"name": "",
			"remark": "",
			"type": 0
		}
	],
	"msg": "",
	"success": true
}
```


## 集群删除


**接口地址**:`/cluster/delete`


**请求方式**:`DELETE`


**请求数据类型**:`*`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|集群编号|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|公共 响应|
|204|No Content||
|401|Unauthorized||
|403|Forbidden||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|data|数据|object||
|msg|状态信息|string||
|success|请求是否成功|boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"data": {},
	"msg": "",
	"success": true
}
```


## 单个集群详细信息查询


**接口地址**:`/cluster/info`


**请求方式**:`GET`


**请求数据类型**:`*`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|clusterId|集群Id|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|公共 响应«公共 视图»|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|data|数据|公共 视图|公共 视图|
|&emsp;&emsp;address||string||
|&emsp;&emsp;createBy|创建人|string||
|&emsp;&emsp;createTime|创建时间|string(date-time)||
|&emsp;&emsp;curPage|当前页|integer(int32)||
|&emsp;&emsp;delFlag||integer(int32)||
|&emsp;&emsp;id||integer(int64)||
|&emsp;&emsp;jobCancelCount||integer(int32)||
|&emsp;&emsp;jobCompleteCount||integer(int32)||
|&emsp;&emsp;jobFailureCount||integer(int32)||
|&emsp;&emsp;modifyBy|修改人|string||
|&emsp;&emsp;modifyTime|修改时间|string(date-time)||
|&emsp;&emsp;name||string||
|&emsp;&emsp;pageSize|页面大小|integer(int32)||
|&emsp;&emsp;parms|自定义参数|object||
|&emsp;&emsp;remark||string||
|&emsp;&emsp;slotAvailableCount||integer(int32)||
|&emsp;&emsp;slotRunningCount||integer(int32)||
|&emsp;&emsp;slotTotal||integer(int32)||
|&emsp;&emsp;taskManagerCount||integer(int32)||
|&emsp;&emsp;type||integer(int32)||
|msg|状态信息|string||
|success|请求是否成功|boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"data": {
		"address": "",
		"createBy": "",
		"createTime": "",
		"curPage": 0,
		"delFlag": 0,
		"id": 0,
		"jobCancelCount": 0,
		"jobCompleteCount": 0,
		"jobFailureCount": 0,
		"modifyBy": "",
		"modifyTime": "",
		"name": "",
		"pageSize": 0,
		"parms": {},
		"remark": "",
		"slotAvailableCount": 0,
		"slotRunningCount": 0,
		"slotTotal": 0,
		"taskManagerCount": 0,
		"type": 0
	},
	"msg": "",
	"success": true
}
```


## 集群列表分页查询


**接口地址**:`/cluster/list`


**请求方式**:`GET`


**请求数据类型**:`*`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|address||query|false|string||
|createBy|创建人|query|false|string||
|createTime|创建时间|query|false|string(date-time)||
|curPage|当前页|query|false|integer(int32)||
|modifyBy|修改人|query|false|string||
|modifyTime|修改时间|query|false|string(date-time)||
|name||query|false|string||
|pageSize|页面大小|query|false|integer(int32)||
|remark||query|false|string||
|type||query|false|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|公共 响应«IPage«集群»»|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|data|数据|IPage«集群»|IPage«集群»|
|&emsp;&emsp;current||integer(int64)||
|&emsp;&emsp;pages||integer(int64)||
|&emsp;&emsp;records||array|集群|
|&emsp;&emsp;&emsp;&emsp;address|集群地址||false|string||
|&emsp;&emsp;&emsp;&emsp;createBy|创建人||false|string||
|&emsp;&emsp;&emsp;&emsp;createTime|创建时间||false|string(date-time)||
|&emsp;&emsp;&emsp;&emsp;delFlag|删除标识，逻辑删除。0 未启用,1 启用,-1删除||false|integer(int32)||
|&emsp;&emsp;&emsp;&emsp;id|主键||false|integer(int64)||
|&emsp;&emsp;&emsp;&emsp;modifyBy|修改人||false|string||
|&emsp;&emsp;&emsp;&emsp;modifyTime|修改时间||false|string(date-time)||
|&emsp;&emsp;&emsp;&emsp;name|集群名称||false|string||
|&emsp;&emsp;&emsp;&emsp;remark|备注||false|string||
|&emsp;&emsp;&emsp;&emsp;type|集群类型,枚举||false|integer(int32)||
|&emsp;&emsp;searchCount||boolean||
|&emsp;&emsp;size||integer(int64)||
|&emsp;&emsp;total||integer(int64)||
|msg|状态信息|string||
|success|请求是否成功|boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"data": {
		"current": 0,
		"pages": 0,
		"records": [
			{
				"address": "",
				"createBy": "",
				"createTime": "",
				"delFlag": 0,
				"id": 0,
				"modifyBy": "",
				"modifyTime": "",
				"name": "",
				"remark": "",
				"type": 0
			}
		],
		"searchCount": true,
		"size": 0,
		"total": 0
	},
	"msg": "",
	"success": true
}
```


## 集群修改


**接口地址**:`/cluster/update`


**请求方式**:`PUT`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | in    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|集群编号|query|true|string||
|address|集群地址|query|false|string||
|name|集群名称|query|false|string||
|remark|描述|query|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|公共 响应|
|201|Created||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|code|状态码|integer(int32)|integer(int32)|
|data|数据|object||
|msg|状态信息|string||
|success|请求是否成功|boolean||


**响应示例**:
```javascript
{
	"code": 0,
	"data": {},
	"msg": "",
	"success": true
}
```