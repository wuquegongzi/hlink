package com.haibao.flink.model;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 作业管理-主信息表
 * </p>
 *
 * @author jobob
 * @since 2020-02-05
 */
public class Job implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 作业编号
     * */
    private String jobCode;

    /**
     * 作业名称
     */
    private String jobName;

    /**
     * 作业类型  0-SQL 1-JAR
     */
    private Integer jobType;

    /**
     * 集群ID
     */
    private Long clusterId;

    /**
     * 关联的flink任务id
     * */
    private String flinkJobId;

    /**
     * 作业描述
     */
    private String jobDesc;

    /**
     * 作业状态0未启动，1运行，2结束
     */
    private Integer jobStatus;

    /**
     * 0 废弃 1 正常
     */
    private Integer delFlag;

    /**
     * 任务运行时使用的JAR
     */
    private String useJar;
    /**
     * 入口类
     * */
    private String entryClass;

    /**
     * 参数
     */
    private String program;

    /**
     * 存储点路径
     */
    private String savepointPath;

    /**
     * Allow Non Restore State,允许非还原状态
     */
    private Boolean allowNrs;

    /**
     * 并行度
     * */
    private Integer parallelism;
    /**
     * 计算单元个数，1个计算单元资源为1核3GB2个并发度，当前可用资源为146核168.00GB
     */
    private Integer computingUnit;

    /**
     * 时间类型
     */
    private Integer timeType;

    /**
     * 重启策略
     */
    private Integer restartStrategy;

    /**
     * 尝试重启的次数
     */
    private Integer restartAttempts;

    /**
     * 重启间隔时间(s)
     */
    private Integer delayInterval;

    /**
     * 一个时间段内的最大失败次数
     */
    private Integer failureRate;

    /**
     * 计算失败率的时间间隔(m)
     */
    private Integer failureInterval;

    /**
     * Checkpoint Interval (ms),设置该值则开启检查点，为0则不开启
     */
    private Long checkpointInterval;

    private Integer checkpointMode;

    private Integer checkpointCleanupMode;

    /**
     * 生成checkpoint的超时时间
     */
    private Long checkpointTimeout;

    /**
     * 最大并发生成Checkpoint数
     */
    private Integer checkpointMaxCuncurrency;

    private Long minPauseBetweenCheckpoints;

    /**
     * statebackend方式，见字典statebackend_type
     */
    private Integer checkpointStatebackendType;

    /**
     * statebackend存储地址
     */
    private String checkpointStatebackendPath;

    /**
     * 版本号
     */
    private String version;

    /**
     * SQL作业关联的数据源
     */
    private List<JobDs> jobDsList;

    /**
     * 作业用到的UDF
     */
    private List<Udf> udfList;

    /**
     * 最大并行度
     */
    private int maxParallelism;

    /**
     * 开启微型批次聚合 0-不开启 1-开启
     */
    private Integer enableMinibatchOptimization;

    /**
     * 缓冲输入记录最大等待时间(s). 注：若开启微批次聚合，其值必须大于零
     */
    private Long tableExecMinibatchAllowlatency;

    /**
     * 缓冲最大输入记录数.注：若开启微批次聚合，则其值必须为正
     */
    private Long tableExecMinibatchSize;

    /**
     * 启用局部本地全局聚合. 0-禁用 1-开启
     */
    private Integer tableOptimizerAggPhaseStrategy;

    /**
     * 启用不同的agg分割. 0-禁用 1-开启
     */
    private Integer tableOptimizerDistinctAggSplitEnabled;

    /**
     * 辅助用于file文件型的作业
     */
    private List<String> fileSQLList;

    public List<JobDs> getJobDsList() {
        return jobDsList;
    }

    public void setJobDsList(List<JobDs> jobDsList) {
        this.jobDsList = jobDsList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobCode() {
        return jobCode;
    }

    public void setJobCode(String jobCode) {
        this.jobCode = jobCode;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Integer getJobType() {
        return jobType;
    }

    public void setJobType(Integer jobType) {
        this.jobType = jobType;
    }

    public Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(Long clusterId) {
        this.clusterId = clusterId;
    }

    public String getFlinkJobId() {
        return flinkJobId;
    }

    public void setFlinkJobId(String flinkJobId) {
        this.flinkJobId = flinkJobId;
    }

    public String getJobDesc() {
        return jobDesc;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    public Integer getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(Integer jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getUseJar() {
        return useJar;
    }

    public void setUseJar(String useJar) {
        this.useJar = useJar;
    }

    public String getEntryClass() {
        return entryClass;
    }

    public void setEntryClass(String entryClass) {
        this.entryClass = entryClass;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getSavepointPath() {
        return savepointPath;
    }

    public void setSavepointPath(String savepointPath) {
        this.savepointPath = savepointPath;
    }

    public Boolean getAllowNrs() {
        return allowNrs;
    }

    public void setAllowNrs(Boolean allowNrs) {
        this.allowNrs = allowNrs;
    }

    public Integer getParallelism() {
        return parallelism;
    }

    public void setParallelism(Integer parallelism) {
        this.parallelism = parallelism;
    }

    public Integer getComputingUnit() {
        return computingUnit;
    }

    public void setComputingUnit(Integer computingUnit) {
        this.computingUnit = computingUnit;
    }

    public Long getCheckpointInterval() {
        return checkpointInterval;
    }

    public void setCheckpointInterval(Long checkpointInterval) {
        this.checkpointInterval = checkpointInterval;
    }

    public Long getCheckpointTimeout() {
        return checkpointTimeout;
    }

    public void setCheckpointTimeout(Long checkpointTimeout) {
        this.checkpointTimeout = checkpointTimeout;
    }

    public Integer getCheckpointMaxCuncurrency() {
        return checkpointMaxCuncurrency;
    }

    public void setCheckpointMaxCuncurrency(Integer checkpointMaxCuncurrency) {
        this.checkpointMaxCuncurrency = checkpointMaxCuncurrency;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public void setTimeType(Integer timeType) {
        this.timeType = timeType;
    }

    public void setRestartStrategy(Integer restartStrategy) {
        this.restartStrategy = restartStrategy;
    }

    public void setCheckpointMode(Integer checkpointMode) {
        this.checkpointMode = checkpointMode;
    }

    public void setCheckpointCleanupMode(Integer checkpointCleanupMode) {
        this.checkpointCleanupMode = checkpointCleanupMode;
    }

    public Long getMinPauseBetweenCheckpoints() {
        return minPauseBetweenCheckpoints;
    }

    public void setMinPauseBetweenCheckpoints(Long minPauseBetweenCheckpoints) {
        this.minPauseBetweenCheckpoints = minPauseBetweenCheckpoints;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(int delFlag) {
        this.delFlag = delFlag;
    }

    public int getTimeType() {
        return timeType;
    }

    public void setTimeType(int timeType) {
        this.timeType = timeType;
    }

    public int getRestartStrategy() {
        return restartStrategy;
    }

    public void setRestartStrategy(int restartStrategy) {
        this.restartStrategy = restartStrategy;
    }

    public int getCheckpointMode() {
        return checkpointMode;
    }

    public void setCheckpointMode(int checkpointMode) {
        this.checkpointMode = checkpointMode;
    }

    public int getCheckpointCleanupMode() {
        return checkpointCleanupMode;
    }

    public void setCheckpointCleanupMode(int checkpointCleanupMode) {
        this.checkpointCleanupMode = checkpointCleanupMode;
    }

    public Integer getRestartAttempts() {
        return restartAttempts;
    }

    public void setRestartAttempts(Integer restartAttempts) {
        this.restartAttempts = restartAttempts;
    }

    public Integer getDelayInterval() {
        return delayInterval;
    }

    public void setDelayInterval(Integer delayInterval) {
        this.delayInterval = delayInterval;
    }

    public Integer getFailureRate() {
        return failureRate;
    }

    public void setFailureRate(Integer failureRate) {
        this.failureRate = failureRate;
    }

    public Integer getFailureInterval() {
        return failureInterval;
    }

    public void setFailureInterval(Integer failureInterval) {
        this.failureInterval = failureInterval;
    }

    public int getMaxParallelism() {
        return maxParallelism;
    }

    public void setMaxParallelism(int maxParallelism) {
        this.maxParallelism = maxParallelism;
    }

    public List<Udf> getUdfList() {
        return udfList;
    }

    public void setUdfList(List<Udf> udfList) {
        this.udfList = udfList;
    }

    public Integer getEnableMinibatchOptimization() {
        return enableMinibatchOptimization;
    }

    public void setEnableMinibatchOptimization(Integer enableMinibatchOptimization) {
        this.enableMinibatchOptimization = enableMinibatchOptimization;
    }

    public Long getTableExecMinibatchAllowlatency() {
        return tableExecMinibatchAllowlatency;
    }

    public void setTableExecMinibatchAllowlatency(Long tableExecMinibatchAllowlatency) {
        this.tableExecMinibatchAllowlatency = tableExecMinibatchAllowlatency;
    }

    public Long getTableExecMinibatchSize() {
        return tableExecMinibatchSize;
    }

    public void setTableExecMinibatchSize(Long tableExecMinibatchSize) {
        this.tableExecMinibatchSize = tableExecMinibatchSize;
    }

    public Integer getTableOptimizerAggPhaseStrategy() {
        return tableOptimizerAggPhaseStrategy;
    }

    public void setTableOptimizerAggPhaseStrategy(Integer tableOptimizerAggPhaseStrategy) {
        this.tableOptimizerAggPhaseStrategy = tableOptimizerAggPhaseStrategy;
    }

    public Integer getTableOptimizerDistinctAggSplitEnabled() {
        return tableOptimizerDistinctAggSplitEnabled;
    }

    public void setTableOptimizerDistinctAggSplitEnabled(Integer tableOptimizerDistinctAggSplitEnabled) {
        this.tableOptimizerDistinctAggSplitEnabled = tableOptimizerDistinctAggSplitEnabled;
    }

    public List<String> getFileSQLList() {
        return fileSQLList;
    }

    public void setFileSQLList(List<String> fileSQLList) {
        this.fileSQLList = fileSQLList;
    }

    public Integer getCheckpointStatebackendType() {
        return checkpointStatebackendType;
    }

    public void setCheckpointStatebackendType(Integer checkpointStatebackendType) {
        this.checkpointStatebackendType = checkpointStatebackendType;
    }

    public String getCheckpointStatebackendPath() {
        return checkpointStatebackendPath;
    }

    public void setCheckpointStatebackendPath(String checkpointStatebackendPath) {
        this.checkpointStatebackendPath = checkpointStatebackendPath;
    }
}
