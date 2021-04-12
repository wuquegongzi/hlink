package com.haibao.flink.utils;

import cn.hutool.core.util.StrUtil;
import com.haibao.flink.cli.SqlCommandParser.SqlCommandCall;
import com.haibao.flink.connectors.side.http.HttpAsyncLookupTableSource;
import com.haibao.flink.connectors.side.jdbc.JdbcAsyncLookupTableSource;
import com.haibao.flink.enums.CheckpointCleanupModeEnum;
import com.haibao.flink.enums.CheckpointModeEnum;
import com.haibao.flink.enums.CheckpointStatebackendTypeEnum;
import com.haibao.flink.enums.DsTypeEnum;
import com.haibao.flink.enums.RestartStrategyEnum;
import com.haibao.flink.enums.TableTypeEnum;
import com.haibao.flink.enums.TimeTypeEnum;
import com.haibao.flink.model.Ds;
import com.haibao.flink.model.Job;
import com.haibao.flink.model.JobDs;
import com.haibao.flink.model.Udf;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.contrib.streaming.state.RocksDBStateBackend;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.SqlParserException;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName EnvUtils
 * @Description SQL 作业设置
 * @Author ml.c
 * @Date 2020/3/10 8:23 下午
 * @Version 1.0
 */
public class EnvUtils {

    static final Logger LOG = LoggerFactory.getLogger(EnvUtils.class);


    public void tEnvConfigSetting(Job job, StreamTableEnvironment tEnv) {

        Configuration configuration = tEnv.getConfig().getConfiguration();
        // set low-level key-value options

        //微型批次聚合，减少状态操作
        if(null != job.getEnableMinibatchOptimization() && 1 == job.getEnableMinibatchOptimization()){
            long latency = job.getTableExecMinibatchAllowlatency() < 1 ? 1 : job.getTableExecMinibatchAllowlatency();
            long bsize = job.getTableExecMinibatchSize() < 1 ? 1000 : job.getTableExecMinibatchSize();
            // enable mini-batch optimization
            configuration.setString("table.exec.mini-batch.enabled", "true");
            // use 5 seconds to buffer input records
            configuration.setString("table.exec.mini-batch.allow-latency", latency+" s");
            // the maximum number of records can be buffered by each aggregate operator task
            configuration.setString("table.exec.mini-batch.size", String.valueOf(bsize));
        }

        //启用局部本地全局聚合，开启两阶段
        if(null != job.getTableOptimizerAggPhaseStrategy() && job.getTableOptimizerAggPhaseStrategy() == 1){
            // enable two-phase, i.e. local-global aggregatio
            configuration.setString("table.optimizer.agg-phase-strategy", "TWO_PHASE");
        }

        //启用不同的agg分割
        if(null != job.getTableOptimizerDistinctAggSplitEnabled() && job.getTableOptimizerDistinctAggSplitEnabled() == 1){
            // enable distinct agg split
            configuration.setString("table.optimizer.distinct-agg.split.enabled", "true");
        }

    }

    /**
     * @author: c.zh
     * @description: 注册udf, udf和tableEnv须由同一个类加载器加载
     * @date: 2020/3/16
     **/
    public void registerUDF(Job job, StreamTableEnvironment tEnv) throws Exception {

        ClassLoader levelClassLoader = tEnv.getClass().getClassLoader();
        List<Udf> udfs = job.getUdfList();

        if(null == udfs){
            return;
        }
        for (Udf udf : udfs) {

            String udfPath=udf.getUdfPath();
            URL url = null;

            if(StrUtil.isEmpty(udfPath)){
               continue;
            }else{

                try {
                    url=new URL(udfPath);
                    url.openConnection();
                } catch (IOException e) {
//                    if(!udfPath.contains("://")){
//                        udfPath = "file://"+udfPath;
//                        url = new URL(udfPath);
//                    }
                    File file = new File(udfPath);
                    url = file.toURI().toURL();
                }
            }

            try {
                URLClassLoader classLoader = UdfUtil.loadExtraJar(url, (URLClassLoader) levelClassLoader);
                UdfUtil.registerUDF(udf.getUdfType(), udf.getUdfClass(), udf.getUdfName(), tEnv, classLoader);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 执行sql 加工
     *
     * @param job
     * @param tEnv
     */
    public void sqlTranslation(Job job, StreamTableEnvironment tEnv) {

        List<JobDs> jobDsList = job.getJobDsList();
        jobDsList.forEach(jobDs -> {

            if (DsTypeEnum.SINK.getType().equals(jobDs.getDsType())
                    && StrUtil.isNotEmpty(jobDs.getRunSql())) {
                Ds ds = jobDs.getDs();
                String sql = "insert into " + ds.getTableName() + " " + jobDs.getRunSql();

                LOG.info("执行特征加工语句:{}",sql);
                tEnv.sqlUpdate(sql);
            }

        });
    }

    /**
     * 注册表 schema
     *
     * @param job
     * @param env
     * @param tEnv
     */
    public void registerTable(Job job, StreamExecutionEnvironment env, StreamTableEnvironment tEnv) {

        List<JobDs> jobDsList = job.getJobDsList();
        if(null == jobDsList){
            return;
        }

        List<Ds> dsList = new ArrayList<>();
        List<String> ddlList = new ArrayList<String>();

        jobDsList.forEach(jobDs -> {
            Ds ds = jobDs.getDs();
            //判断是否开启ddl
            if (1 == ds.getDdlEnable() && StrUtil.isNotEmpty(ds.getDsDdl())) {
                ddlList.add(ds.getDsDdl());
            } else {
                dsList.add(ds);
            }
        });

        //支持ddl的类型 表注册 执行
        ddlList.forEach(ddl ->{
            //todo 针对ddl做一些特殊处理
            tEnv.sqlUpdate(ddl);
        });

        //判断表类型，执行对应自定义实现注册方法  定制
        dsList.forEach(ds -> {

            //源表
            if (DsTypeEnum.SOURCE.getType().equals(ds.getDsType())) {
                //kafka 通用版本
                if(TableTypeEnum.SOURCE_KAFKA.getType().equals(ds.getDsType())){

                }

            }
            //维表
            else if (DsTypeEnum.SIDE.getType().equals(ds.getDsType())) {
                SideTableBuilder sideTableBuilder = new SideTableBuilder();

                //mysql
                if (TableTypeEnum.SIDE_MYSQL.getType().equals(ds.getTableType())) {

                    JdbcAsyncLookupTableSource tableSource = sideTableBuilder.getJdbcAsyncLookupTableSource(ds,TableTypeEnum.SIDE_MYSQL.getType());
                    //别名
                    tEnv.registerTableSource(ds.getTableName(), tableSource);
                }
                //oracle
                else if (TableTypeEnum.SIDE_ORACLE.getType().equals(ds.getTableType())) {

                    JdbcAsyncLookupTableSource tableSource = sideTableBuilder.getJdbcAsyncLookupTableSource(ds,TableTypeEnum.SIDE_ORACLE.getType());
                    tEnv.registerTableSource(ds.getTableName(), tableSource);
                }
                //http
                else if(StringUtils.equalsIgnoreCase(TableTypeEnum.SIDE_HTTP.getType(),ds.getTableType())){
                    HttpAsyncLookupTableSource tableSource = sideTableBuilder.getHttpAsyncLookupTableSource(ds);
                    tEnv.registerTableSource(ds.getTableName(), tableSource);
                }else{
                    LOG.error("暂不支持该维表类型！{}",ds.getTableType());
                }

            }
            //目标表
            else if (DsTypeEnum.SINK.getType().equals(ds.getDsType())) {


            }
        });
    }

    /**
     * env 环境 设置
     *
     * @param job
     * @param env
     */
    public void envConfigSetting(Job job, StreamExecutionEnvironment env) {

        //时间属性 设置
       if(TimeTypeEnum.EVENTTIME.getCode() == job.getTimeType()){
           // Event Time：事件发生时间，附加在每条记录上的时间戳
           env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
       }else if(TimeTypeEnum.INGESTIONTIME.getCode() == job.getTimeType()){
           //Ingestion Time：事件进入 Flink 的时间，与事件时间类似
           env.setStreamTimeCharacteristic(TimeCharacteristic.IngestionTime);
       }else{
           // default  Processing Time：执行各个算子操作的系统时间
           env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);
       }

        CheckpointConfig chkConfig = env.getCheckpointConfig();

        //作业检查点设置
        if (null != job.getCheckpointInterval() && job.getCheckpointInterval() > 0) {
            LOG.info("开启检查点：{}",job.getCheckpointInterval());
            //开启 checkpoint
            env.enableCheckpointing(job.getCheckpointInterval());
            //设置模式
            CheckpointingMode checkpointingMode = null;
            //exactly_one，仅一次语义
            if (CheckpointModeEnum.EXACTLY_ONCE.getCode() == job.getCheckpointMode()) {
                LOG.info("检查点：EXACTLY_ONCE，仅一次语义");
                checkpointingMode = CheckpointingMode.EXACTLY_ONCE;
            } else if (CheckpointModeEnum.AT_LEAST_ONCE.getCode() == job.getCheckpointMode()) {
                LOG.info("检查点：AT_LEAST_ONCE，至少一次语义");
                checkpointingMode = CheckpointingMode.AT_LEAST_ONCE;
            }

            if (null != checkpointingMode) {
                chkConfig.setCheckpointingMode(checkpointingMode);
            }

            //设置 checkpoint 保留策略
            CheckpointConfig.ExternalizedCheckpointCleanup externalizedCheckpointCleanup = null;
            if (CheckpointCleanupModeEnum.RETAIN_ON_CANCELLATION.getCode() == job.getCheckpointCleanupMode()) {
                //取消程序时，保留 checkpoint 状态文件
                LOG.info("设置 checkpoint 保留策略：RETAIN_ON_CANCELLATION");
                externalizedCheckpointCleanup = CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION;
            } else if (CheckpointCleanupModeEnum.DELETE_ON_CANCELLATION.getCode() == job.getCheckpointCleanupMode()) {
                LOG.info("设置 checkpoint 保留策略：DELETE_ON_CANCELLATION");
                externalizedCheckpointCleanup = CheckpointConfig.ExternalizedCheckpointCleanup.DELETE_ON_CANCELLATION;
            }

            if (null != externalizedCheckpointCleanup) {
                chkConfig.enableExternalizedCheckpoints(externalizedCheckpointCleanup);
            }

            //确保检查点之间有1s的时间间隔【checkpoint最小间隔】
            Long minPauseBetweenCheckpoints = (null == job.getMinPauseBetweenCheckpoints() || job.getMinPauseBetweenCheckpoints() < 1000 )  ? 1000 : job.getMinPauseBetweenCheckpoints();
            chkConfig.setMinPauseBetweenCheckpoints(minPauseBetweenCheckpoints);
            LOG.info("设置 checkpoint最小间隔：{}",minPauseBetweenCheckpoints);

            //检查点必须在10s之内完成，或者被丢弃【checkpoint超时时间】
            Long checkpointTimeout = null == job.getCheckpointTimeout()? 10:job.getCheckpointTimeout();
            chkConfig.setCheckpointTimeout(checkpointTimeout);
            LOG.info("设置 checkpoint超时时间：{}",checkpointTimeout);

            //同一时间只允许进行一次检查点
            int maxConcurrentCheckpoints = (null == job.getCheckpointMaxCuncurrency() || job.getCheckpointMaxCuncurrency() < 1) ? 1 : job.getCheckpointMaxCuncurrency();
            chkConfig.setMaxConcurrentCheckpoints(maxConcurrentCheckpoints);
            LOG.info("设置 checkpoint并发：{}",maxConcurrentCheckpoints);
        }

        //并行度
        env.setMaxParallelism(job.getMaxParallelism());
        LOG.info("设置 最大并行度：{}",job.getMaxParallelism());

        //设置statebackend  三种State Backend：MemoryStateBackend、FsStateBackend、RocksDBStateBackend
        // 将检查点保存在hdfs上面，默认保存在内存中。这里先保存到本地  hdfs/s3/
        // env.setStateBackend(new FsStateBackend("file:///Users/temp/cp/"));
        // env.setStateBackend(new RocksDBStateBackend("hdfs://"));
        if(StrUtil.isNotEmpty(job.getCheckpointStatebackendPath())){
            if(CheckpointStatebackendTypeEnum.FS.getCode().equals(job.getCheckpointStatebackendType())){
                env.setStateBackend(new FsStateBackend(job.getCheckpointStatebackendPath()));
                LOG.info("检查点存储方式：{},检查点存储路径：{}",CheckpointStatebackendTypeEnum.FS.getType(),job.getCheckpointStatebackendPath());
            }else if(CheckpointStatebackendTypeEnum.ROCKSDB.getCode().equals(job.getCheckpointStatebackendType())){
                try {
                    LOG.info("检查点存储方式：{},检查点存储路径：{}",CheckpointStatebackendTypeEnum.ROCKSDB.getType(),job.getCheckpointStatebackendPath());
                    env.setStateBackend(new RocksDBStateBackend(job.getCheckpointStatebackendPath()));
                } catch (IOException e) {
                    LOG.error("检查点设置statebackend异常:{}",e.getMessage());
                }
            }
        }

        //设置重启策略
        if (RestartStrategyEnum.NO_RESTART.getCode() == job.getRestartStrategy()) {
            // 默认 无重启 (No Restart) 策略
            env.setRestartStrategy(RestartStrategies.noRestart());
            LOG.info("设置重启策略：无重启 (No Restart) 策略");
        } else if (RestartStrategyEnum.FIXED_DELAY.getCode() == job.getRestartStrategy()) {
            //固定间隔 (Fixed Delay) 重启策略
            env.setRestartStrategy(RestartStrategies.fixedDelayRestart(
                    // 尝试重启的次数
                    job.getRestartAttempts(),
                    // 间隔
                    Time.of(job.getDelayInterval(), TimeUnit.SECONDS)
            ));
            LOG.info("设置重启策略：固定间隔 (Fixed Delay) 重启策略,尝试重启的次数{},间隔(s){}", job.getRestartAttempts(),job.getDelayInterval());
        } else if (RestartStrategyEnum.FAILURE_RATE.getCode() == job.getRestartStrategy()) {
            //失败率 (Failure Rate) 重启策略
            env.setRestartStrategy(RestartStrategies.failureRateRestart(
                    // 一个时间段内的最大失败次数
                    job.getFailureRate(),
                    // 衡量失败次数的是时间段
                    Time.of(job.getFailureInterval(), TimeUnit.MINUTES),
                    // 间隔
                    Time.of(job.getDelayInterval(), TimeUnit.SECONDS)
            ));

            LOG.info("设置重启策略：失败率 (Failure Rate) 重启策略,一个时间段内的最大失败次数{},衡量失败次数的是时间段(m){},间隔(s){}",job.getFailureRate(),job.getFailureInterval(),job.getDelayInterval());
        }

    }


    // -------------------------------------SQL Cli 执行辅助-------------------------------------------------------

    public void callCommand(StreamTableEnvironment tEnv, SqlCommandCall cmdCall) {
        switch (cmdCall.command) {
            case SET:
                callSet(tEnv,cmdCall);
                break;
            case CREATE_TABLE:
                callCreateTable(tEnv,cmdCall);
                break;
            case INSERT_INTO:
                callInsertInto(tEnv,cmdCall);
                break;
            default:
                throw new RuntimeException("Unsupported command: " + cmdCall.command);
        }
    }

    private void callSet(StreamTableEnvironment tEnv, SqlCommandCall cmdCall) {
        String key = cmdCall.operands[0];
        String value = cmdCall.operands[1];
        tEnv.getConfig().getConfiguration().setString(key, value);
    }

    private void callCreateTable(StreamTableEnvironment tEnv, SqlCommandCall cmdCall) {
        String ddl = cmdCall.operands[0];
        try {
            tEnv.sqlUpdate(ddl);
        } catch (SqlParserException e) {
            throw new RuntimeException("SQL parse failed:\n" + ddl + "\n", e);
        }
    }

    private void callInsertInto(StreamTableEnvironment tEnv, SqlCommandCall cmdCall) {
        String dml = cmdCall.operands[0];
        try {
            tEnv.sqlUpdate(dml);
        } catch (SqlParserException e) {
            throw new RuntimeException("SQL parse failed:\n" + dml + "\n", e);
        }
    }

}
