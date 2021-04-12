package com.haibao.flink;

import cn.hutool.core.lang.UUID;
import com.haibao.flink.cli.CliOptions;
import com.haibao.flink.cli.CliOptionsParser;
import com.haibao.flink.cli.CliParser;
import com.haibao.flink.cli.SqlCommandParser;
import com.haibao.flink.cli.SqlCommandParser.SqlCommandCall;
import com.haibao.flink.db.DBParser;
import com.haibao.flink.model.Job;
import com.haibao.flink.utils.EnvUtils;
import com.haibao.flink.utils.GsonUtils;
import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author: ml.c
 * @Date: 2020/1/8 11:12 上午
 * @Description:  作业执行主入口
 */
public class SqlSubmit {

    static Logger LOG = LoggerFactory.getLogger(SqlSubmit.class);
    private static final String CLI_DATA_MODE = "cli";
    private static final String DS_DATA_MODE = "ds";

    private StreamTableEnvironment tEnv;
    private Job job;
    private String dataMode;

    public SqlSubmit(Job job, String dataMode) {
        this.job = job;
        this.dataMode = dataMode;
    }

    /**
     * @author: ml.c
     * @Date: 2020/1/8 11:14 上午
     * @Description: * **mode**
     * * 描述：执行模式，也就是flink集群的工作模式
     * * local: 本地模式
     * * standalone: 提交到独立部署模式的flink集群
     * * yarn: 提交到yarn模式的flink集群(即提交到已有flink集群) todo
     * * yarnPer: yarn per_job模式提交(即创建新flink application) todo
     * * 必选：否
     * * 默认值：local
     */
    public static void main(String[] args) throws Exception {

        if (args.length < 1) {
            LOG.error("参数不足 ./sql-submit --data-mode=<data-mode>");
            throw new RuntimeException("./sql-submit --data-mode=<data-mode>");
        }

        String dataMode = CLI_DATA_MODE;
        String jobID = "0";
        String profilesActive = "prod";
        //默认最大并行度为99
        String maxParallelism = "50";

        LOG.info("参数传入:");
        for (int i = 0; i < args.length; i++) {
            if(args[i].contains("=")){
                if(null == args[i].split("=")[1]){
                  continue;
                }

                if (args[i].contains("--data-mode")) {
                    dataMode = args[i].split("=")[1];
                    LOG.info("dataMode:{}",dataMode);
                } else if (args[i].contains("--JobID")) {
                    jobID = args[i].split("=")[1];
                    LOG.info("jobID:{}",jobID);
                } else if (args[i].contains("--maxParallelism")) {
                    maxParallelism = args[i].split("=")[1];
                    LOG.info("maxParallelism:{}",maxParallelism);
                } else if(args[i].contains("--profilesActive")){
                    profilesActive =  args[i].split("=")[1];
                    LOG.info("profilesActive:{}",profilesActive);
                }
            }

        }

        Job job = null;
        //两种模式 1、ds 2、cli 默认ds
        switch (dataMode) {
            case DS_DATA_MODE:
                DBParser dbParser = new DBParser(jobID,profilesActive);
                job = dbParser.aggregate();
                if(null == job){
                    throw new RuntimeException("作业信息不存在，请检查！");
                }
                break;
            case CLI_DATA_MODE:
                //传入文件
                final CliOptions options = CliOptionsParser.parseClient(args);
                CliParser cliParser = new CliParser(options);
                job = cliParser.aggregate();

                break;
            default:
                LOG.error("不支持的模式指定，--data-mode={}",dataMode);
                break;
        }

        job.setMaxParallelism(Integer.valueOf(maxParallelism));
        SqlSubmit submit = new SqlSubmit(job,dataMode);

        JobExecutionResult jobExecutionResult = submit.run();

        LOG.info("任务提交结果：{}", GsonUtils.gsonString(jobExecutionResult));
    }

    /**
     * 作业环境生成与运行
     *
     * @throws Exception
     */
    private JobExecutionResult run() throws Exception {

        //step 1、校验作业配置是否完整  应该在提交动作前进一步校验，防止作业失败

        EnvUtils envUtils = new EnvUtils();

        // step 2 、设置运行参数，eg：并行度、checkpoint
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        if(DS_DATA_MODE.equals(dataMode)) {
            envUtils.envConfigSetting(job, env);
        }

        //step 3 、初始化Table Stream环境
        EnvironmentSettings settings = EnvironmentSettings.newInstance()
                .useBlinkPlanner()
                .inStreamingMode()
                .build();
        this.tEnv = StreamTableEnvironment.create(env, settings);

        String jobName = "";
        //作业执行方式
        if(CLI_DATA_MODE.equals(dataMode)){
            jobName = "CLI_"+ UUID.fastUUID();

            List<SqlCommandCall> sqlCalls = SqlCommandParser.parse(job.getFileSQLList());
            for (SqlCommandParser.SqlCommandCall call : sqlCalls) {
                envUtils.callCommand(tEnv,call);
            }
        }else{
            jobName = job.getJobCode().concat("_").concat(job.getJobName());

            envUtils.tEnvConfigSetting(job, tEnv);

            //step 4、批量注册所有UDF
            envUtils.registerUDF(job, tEnv);

            //step 5、判断类型表，进行表注册
            envUtils.registerTable(job, env, tEnv);

            //step 6、执行所有 特征加工sql
            envUtils.sqlTranslation(job, tEnv);
        }

        JobExecutionResult jobExecutionResult = tEnv.execute(jobName);

        return jobExecutionResult;
    }

}
