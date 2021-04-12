package com.haibao.admin.web.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by baoyu on 2020/2/7.
 * Describe
 */
@ApiModel("作业  视图实体")
@Data
@EqualsAndHashCode(callSuper = true)
public class JobVO extends BaseVO {

    @ApiModelProperty(value = "主键id")
    private Long id;
    /**
     * 作业编号
     * */
    @ApiModelProperty(value = "作业编号")
    private String jobCode;

    /**
     * 作业名称
     */
    @ApiModelProperty(value = "作业名称")
    @NotEmpty(message = "作业名称 不能为空")
    @Size(max = 120, min = 1, message = "作业名称 长度需要在1-120之间")
    private String jobName;

    /**
     * 作业类型  0-SQL 1-JAR
     */
    @ApiModelProperty(value = "作业类型  0-SQL 1-JAR")
    @NotNull(message = "作业类型 不能为空")
    private Integer jobType;
    @ApiModelProperty(value = "作业类型名称 辅助转义字段")
    private String jobTypeName;

    /**
     * 集群ID
     */
    @ApiModelProperty(value = "映射的集群ID")
    @NotNull(message = "请选择集群")
    private Long clusterId;

    /**
     * 集群名称
     */
    @ApiModelProperty(value = "集群名称")
    private String clusterName;
    /**
     * 集群类型
     */
    @ApiModelProperty(value = "集群类型，目前默认 0-sdandlone")
    private Integer clusterType;
    @ApiModelProperty(value = "集群类型 名称，辅助 转义字段")
    private String clusterTypeName;

    /**
     * 计算单元
     */
    @ApiModelProperty(value = "计算单元")
    private Integer computingUnit;

    /**
     * 作业描述
     */
    @ApiModelProperty(value = "作业描述")
    @Size(max = 500, min = 0, message = "作业描述 长度需要在500字以内")
    private String jobDesc;

    /**
     * 作业状态0未运行，1运行中，2取消，3失败
     */
    @ApiModelProperty(value = "作业状态0未运行，1运行中，2取消，3失败, 9提交中 ")
    private Integer jobStatus;
    @ApiModelProperty(value = "作业状态名称 转义辅助字段")
    private String jobStatusName;

    /**
     * 并行度
     * */
    @ApiModelProperty(value = "并行度")
    private Integer parallelism;

    /**
     * 存储点路径
     */
    @ApiModelProperty(value = "存储点路径")
    private String savepointPath;

    /**
     * 自定义的保存点生成根路径
     * */
    @ApiModelProperty(value = "自定义保存点生成根路径")
    private String stateSavepointsDir;

    /**
     * flink 任务 运行ID
     */
    @ApiModelProperty(value = "集群内运行的作业ID")
    private String flinkJobId;

    @ApiModelProperty(value = "版本号 保留字段")
    private String version;


    //=======================

    /**
     * 0 废弃 1 正常
     */
    @ApiModelProperty(value = "逻辑删除标识 0 废弃 1 正常 ,默认为1，保留字段")
    private Integer delFlag;

    /**
     * 任务运行时使用的JAR
     */
    @ApiModelProperty(value = "JAR任务运行时使用的JAR文件")
    private String useJar;

    /**
     * 入口类
     */
    @ApiModelProperty(value = "JAR任务运行时使用的入口类")
    private String entryClass;

    /**
     * 参数
     */
    @ApiModelProperty(value = "JAR任务运行时使用的参数")
    private String program;

    /**
     * Allow Non Restore State,允许非还原状态
     */
    @ApiModelProperty(value = "Allow Non Restore State,允许非还原状态")
    private Integer allowNrs;

    /**
     * 时间类型
     */
    @ApiModelProperty(value = "Flink 时间类型，对应字典 flink_time_type")
    private Integer timeType;

    /**
     * 重启策略
     */
    @ApiModelProperty(value = "重启策略，对应字典 restart_strategy")
    private Integer restartStrategy;

    /**
     * 尝试重启的次数
     */
    @ApiModelProperty(value = "尝试重启的次数")
    private Integer restartAttempts;

    /**
     * 重启间隔时间(s)
     */
    @ApiModelProperty(value = "重启间隔时间(s)")
    private Integer delayInterval;

    /**
     * 一个时间段内的最大失败次数
     */
    @ApiModelProperty(value = "一个时间段内的最大失败次数")
    private Integer failureRate;

    /**
     * 计算失败率的时间间隔(m)
     */
    @ApiModelProperty(value = "计算失败率的时间间隔(m)")
    private Integer failureInterval;

    /**
     * Checkpoint Interval (ms),设置该值则开启检查点，为0则不开启
     */
    @ApiModelProperty(value = "Checkpoint Interval (ms),设置该值则开启检查点，为0则不开启")
    private Long checkpointInterval;

    @ApiModelProperty(value = "checkpoint模式，对应字典 checkpoint_mode")
    private Integer checkpointMode;

    @ApiModelProperty(value = "checkpoint清除策略，对应字典 checkpoint_cleanup_mode")
    private Integer checkpointCleanupMode;

    @ApiModelProperty(value = "statebackend方式，见字典statebackend_type ")
    private Integer checkpointStatebackendType;

    @ApiModelProperty(value = "statebackend存储地址")
    private String checkpointStatebackendPath;

    /**
     * 生成checkpoint的超时时间
     */
    @ApiModelProperty(value = "生成checkpoint的超时时间")
    private Long checkpointTimeout;

    /**
     * 最大并发生成Checkpoint数
     */
    @ApiModelProperty(value = "最大并发生成Checkpoint数")
    private Integer checkpointMaxCuncurrency;

    @ApiModelProperty(value = "确保检查点之间有1s的时间间隔【checkpoint最小间隔】")
    private Long minPauseBetweenCheckpoints;

    /**
     * SQL作业专用 数据源映射
     */
    @ApiModelProperty(value = "SQL作业专用 数据源映射")
    List<JobDsVO> jobDsVOS;

    @ApiModelProperty(value = "开启微型批次聚合 0-不开启 1-开启")
    private Integer enableMinibatchOptimization;

    @ApiModelProperty(value = "缓冲输入记录最大等待时间(s). 注：若开启微批次聚合，其值必须大于零")
    private Long tableExecMinibatchAllowlatency;

    @ApiModelProperty(value = "缓冲最大输入记录数.注：若开启微批次聚合，则其值必须为正")
    private Long tableExecMinibatchSize;

    @ApiModelProperty(value = "启用局部本地全局聚合. 0-禁用 1-开启")
    private Integer tableOptimizerAggPhaseStrategy;

    @ApiModelProperty(value = "启用不同的agg分割. 0-禁用 1-开启")
    private Integer tableOptimizerDistinctAggSplitEnabled;
}
