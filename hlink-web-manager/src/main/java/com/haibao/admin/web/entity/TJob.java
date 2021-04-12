package com.haibao.admin.web.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


/**
 * <p>
 * 作业管理-主信息表
 * </p>
 *
 * @author jobob
 * @since 2020-02-05
 */
@ApiModel("作业  持久层实体")
@TableName("t_job")
@Data
public class TJob extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 作业编号
     * */
    @ApiModelProperty(value = "作业编号")
    private String jobCode;

    /**
     * 作业名称
     */
    @ApiModelProperty(value = "作业名称")
    @NotEmpty(message="作业名称不可为空")
    private String jobName;

    /**
     * 作业类型  0-SQL 1-JAR
     */
    @ApiModelProperty(value = "作业类型  0-SQL 1-JAR")
    @NotNull(message="作业类型必填")
    private Integer jobType;

    /**
     * 集群ID
     */
    @ApiModelProperty(value = "集群ID")
    private Long clusterId;

    /**
     * 关联的flink任务id
     * */
    @ApiModelProperty(value = "关联的集群内运行的作业ID")
    private String flinkJobId;

    /**
     * 作业描述
     */
    @ApiModelProperty(value = "作业描述")
    private String jobDesc;

    /**
     * 作业状态0未启动，1运行，2结束
     */
    @ApiModelProperty(value = "作业状态0未运行，1运行中，2取消，3失败, 9提交中 ")
    private Integer jobStatus;

    /**
     * 0 废弃 1 正常
     */
    @ApiModelProperty(value = "0 废弃 1 正常 ")
    private Integer delFlag;

    /**
     * 任务运行时使用的JAR
     */
    @ApiModelProperty(value = "JAR任务运行时使用的JAR文件")
    private String useJar;
    /**
     * 入口类
     * */
    @ApiModelProperty(value = "JAR任务运行时使用的入口类")
    private String entryClass;

    /**
     * 参数
     */
    @ApiModelProperty(value = "JAR任务运行时使用的参数")
    private String program;

    /**
     * 保存点存储自定义目录
     */
    @ApiModelProperty(value = "当前保存点位置")
    private String savepointPath;
    /**
     * 自定义的保存点生成根路径
     * */
    @ApiModelProperty(value = "自定义保存点生成根路径")
    private String stateSavepointsDir;

    /**
     * Allow Non Restore State,允许非还原状态
     */
    @ApiModelProperty(value = "Allow Non Restore State,允许非还原状态")
    private Integer allowNrs;

    /**
     * 并行度
     * */
    @ApiModelProperty(value = "并行度")
    private Integer parallelism;
    /**
     * 计算单元个数，1个计算单元资源为1核3GB2个并发度，当前可用资源为146核168.00GB
     */
    @ApiModelProperty(value = "计算单元个数，eg:1个计算单元资源为1核3GB2个并发度，当前可用资源为146核168.00GB")
    private Integer computingUnit;

    /**
     * 时间类型
     */
    @ApiModelProperty(value = "Flink 时间类型，对应字典 flink_time_type")
    private Integer timeType;

    /**
     * 版本号
     */
    @ApiModelProperty(value = "版本号 保留字段")
    private String version;

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
    @TableField("checkpoint_Interval")
    private Long checkpointInterval;

    @ApiModelProperty(value = "checkpoint模式，对应字典 checkpoint_mode")
    private Integer checkpointMode;

    @ApiModelProperty(value = "checkpoint清除策略，对应字典 checkpoint_cleanup_mode")
    private Boolean checkpointCleanupMode;

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

    @ApiModelProperty(value = "statebackend方式，见字典statebackend_type ")
    private Integer checkpointStatebackendType;

    @ApiModelProperty(value = "statebackend存储地址")
    private String checkpointStatebackendPath;

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
