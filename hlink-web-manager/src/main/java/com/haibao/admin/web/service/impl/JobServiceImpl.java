package com.haibao.admin.web.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.haibao.admin.utils.HelpUtils;
import com.haibao.admin.utils.OkHttpUtil;
import com.haibao.admin.web.entity.TCluster;
import com.haibao.admin.web.entity.TDs;
import com.haibao.admin.web.entity.TDsJsonField;
import com.haibao.admin.web.entity.TJob;
import com.haibao.admin.web.entity.TJobDs;
import com.haibao.admin.web.entity.TJobDsSink;
import com.haibao.admin.web.entity.TJobLog;
import com.haibao.admin.web.service.ClusterService;
import com.haibao.admin.web.service.DsJsonFieldService;
import com.haibao.admin.web.service.DsSchemaColumnService;
import com.haibao.admin.web.service.DsService;
import com.haibao.admin.web.service.FlinkApiService;
import com.haibao.admin.web.service.JobDsService;
import com.haibao.admin.web.service.JobDsSinkService;
import com.haibao.admin.web.service.JobLogService;
import com.haibao.admin.web.service.JobService;
import com.haibao.admin.web.vo.JobDsVO;
import com.haibao.admin.web.vo.JobVO;
import com.haibao.flink.enums.CheckpointStatebackendTypeEnum;
import com.haibao.flink.enums.DsTypeEnum;
import com.haibao.flink.log.LogEvent;
import com.haibao.flink.log.LogLevelEnum;
import com.haibao.flink.utils.GsonUtils;
import com.nextbreakpoint.flinkclient.api.ApiException;
import com.nextbreakpoint.flinkclient.model.ExecutionExceptionInfo;
import com.nextbreakpoint.flinkclient.model.JarFileInfo;
import com.nextbreakpoint.flinkclient.model.JarListInfo;
import com.nextbreakpoint.flinkclient.model.JobExceptionsInfo;
import com.haibao.admin.web.common.enums.CodeEnum;
import com.haibao.admin.web.common.enums.JobStatusEnum;
import com.haibao.admin.web.common.enums.JobTypeEnum;
import com.haibao.admin.web.common.result.Response;
import com.haibao.admin.web.mapper.JobMapper;
import com.haibao.admin.web.vo.templete.JsonField;
import freemarker.template.TemplateException;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by baoyu on 2020/2/5.
 * Describe
 */
@Service
public class JobServiceImpl extends ServiceImpl<JobMapper, TJob> implements JobService {

    private Logger LOGGER = LoggerFactory.getLogger(JobServiceImpl.class);

    @Autowired
    private JobDsService jobDsService;
    @Autowired
    private JobDsSinkService jobDsSinkService;
    @Autowired
    private JobLogService jobLogService;

    @Autowired
    private FlinkApiService flinkApiService;

    @Autowired
    private ClusterService clusterService;
    @Autowired
    private DsService dsService;
    @Autowired
    private DsJsonFieldService dsJsonFieldService;
    @Autowired
    private DsSchemaColumnService dsSchemaColumnService;

    @Autowired
    private HttpServletRequest request;

    /**
     * 检查点 存储方式，全局默认从配置文件取
     */
    @Value("${flink.job.checkpoint-statebackend-type}")
    private String checkpointStatebackendType;
    @Value("${flink.job.checkpoint-statebackend-path}")
    private String checkpointStatebackendPath;
    @Value("${spring.profiles.active}")
    private String profilesActive;

    /**
     * 作业新增
     * @param jobVO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response<TJob> createJob(JobVO jobVO) {

        int jobType=jobVO.getJobType();
        jobVO.setJobCode(HelpUtils.getJobCode(jobType));

        int checkpointStatebackendTypeVO = CheckpointStatebackendTypeEnum.MEMORY.getCode();
        String checkpointStatebackendPathVO = "";

        //设置全局 检查点存储方式和路径
        if(StrUtil.isNotEmpty(checkpointStatebackendPath)){
            if(CheckpointStatebackendTypeEnum.FS.getType().equals(checkpointStatebackendType) ){
                checkpointStatebackendTypeVO = CheckpointStatebackendTypeEnum.FS.getCode();
                checkpointStatebackendPathVO = checkpointStatebackendPath;
            }else if(CheckpointStatebackendTypeEnum.ROCKSDB.getType().equals(checkpointStatebackendType) ){
                checkpointStatebackendTypeVO = CheckpointStatebackendTypeEnum.FS.getCode();
                checkpointStatebackendPathVO = checkpointStatebackendPath;
            }
        }
        jobVO.setCheckpointStatebackendType(checkpointStatebackendTypeVO);
        jobVO.setCheckpointStatebackendPath(checkpointStatebackendPathVO);

        //需要根据类型做不同的表操作
        TJob tJob;
        if(JobTypeEnum.JAR.getType()==jobType){
            tJob = new TJob();
            BeanUtils.copyProperties(jobVO,tJob);
            save(tJob);
        }else{ //SQL类型，需要保存数据源映射关系
            tJob = saveSQLJob(jobVO);
        }

        return Response.success(tJob);
    }

    /**
     * 新增SQL作业 动作
     * @param jobVO
     * @return
     */
    TJob saveSQLJob(JobVO jobVO) {
        TJob tJob = new TJob();
        BeanUtils.copyProperties(jobVO,tJob);
        save(tJob);

        List<JobDsVO> jobDsVOS = jobVO.getJobDsVOS();

        //保存源表和维表映射
        List<TJobDs> jobDsList = jobDsVOS.stream().filter(jobDsVO -> {
            if(!DsTypeEnum.SINK.getType().equals(jobDsVO.getDsType())){
                return true;
            }
            return false;
        }).collect(Collectors.toList())
                .stream().map(jobDsVO -> {
            TJobDs jobDs = new TJobDs();
            BeanUtils.copyProperties(jobDsVO,jobDs);
            jobDs.setJobId(tJob.getId());

            return jobDs;
        }).collect(Collectors.toList());

        jobDsService.saveBatch(jobDsList);

        //保存目标表映射
        List<TJobDsSink> jobDsSinkList = jobDsVOS.stream().filter(jobDsVO -> {
            if(DsTypeEnum.SINK.getType().equals(jobDsVO.getDsType())){
                return true;
            }
            return false;
        }).collect(Collectors.toList())
                .stream().map(jobDsVO -> {
                    TJobDsSink jobDsSink = new TJobDsSink();
                    BeanUtils.copyProperties(jobDsVO,jobDsSink);
                    jobDsSink.setJobId(tJob.getId());

                    return jobDsSink;
                }).collect(Collectors.toList());

        jobDsSinkService.saveBatch(jobDsSinkList);

        return tJob;
    }

    /**
     * 作业列表
     * @param jobQuery 查询信息
     * @return
     */
    @Override
    public Response<IPage<JobVO>> selectJobs(JobVO jobQuery) {

        Page<JobVO> page=new Page<>(jobQuery.getCurPage(),jobQuery.getPageSize());

        IPage<JobVO> result = baseMapper.selectJobList(page,jobQuery);

        List<JobVO> jobList=result.getRecords();
        //转换枚举值-->名称
        if(CollectionUtils.isNotEmpty(jobList)){
            for(JobVO jobVO:jobList){
                Integer jobStatus=jobVO.getJobStatus();
//                Integer clusterType=jobVO.getClusterType();
                Integer jobType=jobVO.getJobType();
                if(JobStatusEnum.getById(jobStatus)!=null){
                    jobVO.setJobStatusName(JobStatusEnum.getById(jobStatus).getDesc());
                }
                if(JobTypeEnum.getById(jobType)!=null){
                    jobVO.setJobTypeName(JobTypeEnum.getById(jobType).toString());
                }
            }
        }
        return Response.success(result);
    }

    /**
     * 作业删除
     * @param jobId 作业Id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response deleteJob(Long jobId) {
        TJob jobDO= getById(jobId);
        if(jobDO==null){
            return Response.error(CodeEnum.JOB_NOT_FOUND);
        }
        if(JobStatusEnum.RUNNING.getStatus().equals(jobDO.getJobStatus())){
            return Response.error(CodeEnum.JOB_IS_RUNNING);
        }
        //删除作业
        removeById(jobId);

        if(JobTypeEnum.SQL.getType() == jobDO.getJobType().intValue()){
           //及联删除数据源映射
            QueryWrapper<TJobDs> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("job_id",jobId);
           jobDsService.remove(queryWrapper);

            QueryWrapper<TJobDsSink> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("job_id",jobId);
            jobDsSinkService.remove(queryWrapper2);
        }
        //及联删除作业日志
        QueryWrapper<TJobLog> queryWrapper3 = new QueryWrapper<>();
        queryWrapper3.eq("job_id",jobId);
        jobLogService.remove(queryWrapper3);

        return Response.success();
    }

    /**
     * 作业运行
     * @param id
     * @return
     */
    @Override
    public Response run(Long id) {

        if(id==null){
            return Response.error(CodeEnum.PARAM_ERROR);
        }
        TJob tJob = getById(id);
        if(tJob==null){
            return Response.error(CodeEnum.JOB_NOT_FOUND);
        }

        if(JobStatusEnum.RUNNING.getStatus().equals(tJob.getJobStatus())){
            return Response.error(CodeEnum.JOB_IS_RUNNING);
        }

        //获取集群信息
        TCluster cluster=clusterService.getById(tJob.getClusterId());
        //作业启动条件 检测 (1、作业完整性  2、作业数据源依赖 完整性  3、作业执行环境 等)
        Response checkResponse = null;
        try {
            checkResponse = checkEnv(cluster,tJob);
        } catch (Exception e) {
            return Response.error(0,"作业检测异常："+e.getMessage());
        }
        if(!checkResponse.isSuccess()){
            return checkResponse;
        }

        String clusterAddress=cluster.getAddress();
        String savepointPath=tJob.getSavepointPath();
        Integer parallelism=tJob.getParallelism();
        Integer allowNrs = tJob.getAllowNrs();

        try {

            int jobType=tJob.getJobType();
            //JAR作业类型
            if(JobTypeEnum.JAR.getType()==jobType){

                String jarId = tJob.getUseJar();
                if(StringUtils.isBlank(jarId)){
                    return Response.error(CodeEnum.PLEASE_CONFIG_JAR_ID);
                }

                Boolean allowNrsBoolean= allowNrs==0?false:true;
                String programArg=tJob.getProgram();
                String entryClass=tJob.getEntryClass();

                //======================记录日志========================================
                Map parmMap = Maps.newLinkedHashMap();
                parmMap.put("clusterAddress",clusterAddress);
                parmMap.put("jarId",jarId);
                parmMap.put("allowNrsBoolean",allowNrsBoolean);
                parmMap.put("savepointPath",savepointPath);
                parmMap.put("programArg",programArg);
                parmMap.put("entryClass",entryClass);
                parmMap.put("parallelism",parallelism);

                Map logMap = new LinkedHashMap(2);
                logMap.put("入参",parmMap);
                //====================================================================

                String resultStr = null;
                try {
                    resultStr = flinkApiService.runJar(clusterAddress,jarId,allowNrsBoolean,savepointPath,programArg,entryClass,parallelism);
                } catch (IOException e) {
                    e.printStackTrace();
                    logMap.put("异常信息",e.getMessage());
                } catch (ApiException e) {
                    e.printStackTrace();
                    logMap.put("异常信息",e.getMessage());
                }

                JsonObject result = null;
                boolean is = false;
                if(null != resultStr){
                    result=JsonParser.parseString(resultStr).getAsJsonObject();
                    
                    String flinkJobId= null;
                    try {
                        flinkJobId = null != result.get("jobid")? result.get("jobid").getAsString():"";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(StrUtil.isNotEmpty(flinkJobId)){
                        tJob.setFlinkJobId(flinkJobId);
                        tJob.setJobStatus(JobStatusEnum.RUNNING.getStatus());
                        updateById(tJob);
                        is = true;
                    }
                }

                logMap.put("运行结果",result);
                LogEvent logEvent = new LogEvent();
                logEvent.setTimestamp(System.currentTimeMillis());
                logEvent.setMessage(GsonUtils.gsonString(logMap));
                logEvent.setType("JAR作业启动");
                logEvent.setLevel(LogLevelEnum.INFO.name());

                Map tags = Maps.newLinkedHashMap();
                tags.put("URL",request.getRequestURL().toString());
                tags.put("IP",request.getRemoteAddr());
                tags.put("操作人员","");

                logEvent.setTags(tags);

                jobLogService.sendLog(id,GsonUtils.gsonString(logEvent),"up", 0);

                if(is){
                    return Response.success();
                }
            }else{
                //SQL作业类型，执行shell，调用sql-submit jar,默认采用ds数据库连接方式
                Map map = new HashMap(5);
                map.put("clusterAddress",clusterAddress);
                map.put("jobID",id);
                map.put("parallelism",parallelism);
                map.put("allowNrs",allowNrs);
                map.put("profilesActive",profilesActive);

                //非未提交的作业，设置的保存点才有效
                if(!tJob.getJobStatus().equals(JobStatusEnum.UNCOMMITTED.getStatus())){
                    map.put("savepointPath",savepointPath);
                }

                //设置作业状态为 启动中 ing
                tJob.setJobStatus(JobStatusEnum.STARTING.getStatus());
                updateById(tJob);

                //异步提交作业
                Response<String> response = flinkApiService.runSQLJob(map);

                //只是请求成功，并不能说明作业启动成功
                if(response.isSuccess()){
                    return Response.success("作业启动中，请稍后！");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }

        tJob.setJobStatus(JobStatusEnum.FAILED.getStatus());
        updateById(tJob);
        return Response.error(CodeEnum.JOB_START_ERROR);
    }

    /**
     * 作业启动条件 检测 (1、作业完整性  2、作业数据源依赖 完整性  3、作业执行环境 等)
     * @param cluster
     * @param tJob
     * @return
     */
    private Response checkEnv(TCluster cluster, TJob tJob) {

        LOGGER.info("校验作业完整性以及可行性...begin...{}",tJob.getJobName());

       //step1、 作业完整性
        if(JobTypeEnum.JAR.getType().equals(tJob.getJobType())){
           //校验一下jar是否还存在
            Response<JarListInfo> jarListInfoResponse = null;
            try {
                jarListInfoResponse = flinkApiService.getJarLists(cluster.getAddress());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(null != jarListInfoResponse && jarListInfoResponse.isSuccess()){
                JarListInfo jarListInfo = jarListInfoResponse.getData();
                List<JarFileInfo> jarFileInfos = jarListInfo.getFiles();
                boolean exists = false;
                for (JarFileInfo jarFileInfo : jarFileInfos) {
                    if(jarFileInfo.getId().equals(tJob.getUseJar())){
                        exists = true;
                    }
                }
                if(!exists){
                    return Response.error(0,"集群中jar缺失，请检查！-"+tJob.getUseJar());
                }
            }else{
                return Response.error(0,"集群暂时不可用，请检查！");
            }
        }else{
            //step2、作业数据源依赖 完整性
            QueryWrapper<TJobDs> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("job_id",tJob.getId());
            List<TJobDs> tJobDsList = jobDsService.list(queryWrapper);

            long sourceCount = 0;
            if(null != tJobDsList){
                sourceCount = tJobDsList.stream().filter(tJobDs -> {
                    if(DsTypeEnum.SOURCE.getType().equals(tJobDs.getDsType())){
                        return true;
                    }
                    return false;
                }).count();
            }
            if(sourceCount < 1){
                return  Response.error(0,"缺少数据源-源表配置，请检查！");
            }

            for (TJobDs tJobDs : tJobDsList) {
                TDs tDs = dsService.getById(tJobDs.getDsId());
                if(null == tDs){
                    return  Response.error(0,"数据源缺失，请检查！");
                }
                if(DsTypeEnum.SOURCE.getType().equals(tJobDs.getDsType())){
                    if(StrUtil.isEmpty(tDs.getDsDdl())){
                        return  Response.error(0,"数据源["+tDs.getDsName()+"]配置不完整，请检查！");
                    };
                }else if(DsTypeEnum.SIDE.getType().equals(tJobDs.getDsType())){
                    QueryWrapper<TDsJsonField> queryWrapper2 = new QueryWrapper();
                    queryWrapper2.eq("ds_id", tDs.getId());
                    TDsJsonField dsJsonField = dsJsonFieldService.getOne(queryWrapper2);
                    if(null == dsJsonField || StrUtil.isEmpty(dsJsonField.getJsonValue())){
                        return  Response.error(0,"数据源["+tDs.getDsName()+"]配置不完整，请检查！");
                    }
                    List<JsonField> jsonFields = GsonUtils.jsonToList(dsJsonField.getJsonValue(),JsonField.class);

                    boolean checkJsonFields =  dsService.checkJsonFields(jsonFields);
                    if (!checkJsonFields) {
                        return Response.error(CodeEnum.PARAM_ERROR, "数据源["+tDs.getDsName()+"]必填字段不可为空！");
                    }
                }

            }
            QueryWrapper<TJobDsSink> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("job_id",tJob.getId());
            List<TJobDsSink> jobDsSinkList =  jobDsSinkService.list(queryWrapper2);

            if(null == jobDsSinkList || jobDsSinkList.size() < 1){
                return  Response.error(0,"缺少数据源-目标表配置，请检查！");
            }
            for (TJobDsSink tJobDsSink: jobDsSinkList) {
                String runSql = tJobDsSink.getRunSql();
                if(StrUtil.isEmpty(runSql)){
                    return  Response.error(0,"缺少特征加工语句，请检查！");
                }
                if(runSql.lastIndexOf(";") == runSql.length()-1){
                    return  Response.error(0,"特征加工语句不可以';'结尾");
                }

                TDs tDs = dsService.getById(tJobDsSink.getDsId());
                if(null == tDs){
                    return  Response.error(0,"数据源缺失，请检查！");
                }
                if(StrUtil.isEmpty(tDs.getDsDdl())){
                    return  Response.error(0,"数据源["+tDs.getDsName()+"]配置不完整，请检查！");
                };
            }

        }

        //step3、作业执行环境
        if (null == cluster) {
            return Response.error(CodeEnum.CLUSTER_INFO_IS_NULL);
        }
        if(StrUtil.isEmpty(cluster.getAddress())){
            return  Response.error(0,"集群信息不完整，请检查");
        }
        okhttp3.Response response= null;
        try {
            response = OkHttpUtil.httpAccessibility(cluster.getAddress()+"/config",null);
        } catch (Exception e) {}
        if(response==null||!response.isSuccessful()){
            return  Response.error(0,"集群地址不可用，请检查");
        }

        //todo 检测集群资源是否充足


        return Response.success();
    }

    /**
     * 作业停止
     * @param jobId
     * @return
     */
    @Override
    public Response cancel(Long jobId) {
        TJob jobDO = this.getById(jobId);
        if(jobDO==null){
            return Response.error(CodeEnum.JOB_NOT_FOUND);
        }
        if(!JobStatusEnum.RUNNING.getStatus().equals(jobDO.getJobStatus())){
            return Response.error(CodeEnum.JOB_NOT_RUNNINT);
        }
        try {
            TCluster tCluster=clusterService.getById(jobDO.getClusterId());
            String address=tCluster.getAddress();
            //获取集群保存点路径配置
            String savepointRootPath=jobDO.getStateSavepointsDir();
            if(StringUtils.isBlank(savepointRootPath)){
                Response<Map<String,Object>> configMapResponse=flinkApiService.getJobManagerConfig(address);
                if(!configMapResponse.isSuccess()){
                    return configMapResponse;
                }
                Map<String,Object> configMap=configMapResponse.getData();
                if(configMap!=null && configMap.get("state.savepoints.dir")!=null){
                    savepointRootPath= (String) configMap.get("state.savepoints.dir");
                }
            }
            //如果存在保存点路径配置，则触发作业保存点，并停止作业。更新作业信息。否则直接停止作业，更新作业信息
            if(StringUtils.isNotBlank(savepointRootPath)){
                //触发保存点，后停止任务
                String triggerId=flinkApiService.terminateJobWithSavePoint(jobDO.getFlinkJobId(),savepointRootPath,address);
                jobDO.setJobStatus(JobStatusEnum.CANCELLING.getStatus());
                jobDO.setModifyTime(new Date());
                updateById(jobDO);
                //异步读取保存点信息并更新到job
                flinkApiService.asyncGetJobSavepointStatus(jobId,triggerId,address);

            }else{
                flinkApiService.terminateJob(jobDO.getFlinkJobId(),address);
                jobDO.setJobStatus(JobStatusEnum.CANCELED.getStatus());
                updateById(jobDO);
            }

            LogEvent logEvent = new LogEvent();
            logEvent.setTimestamp(System.currentTimeMillis());
            logEvent.setMessage("作业已停止");
            logEvent.setType("作业停止操作");
            logEvent.setLevel(LogLevelEnum.INFO.name());

            Map tags = Maps.newLinkedHashMap();
            tags.put("URL",request.getRequestURL().toString());
            tags.put("IP",request.getRemoteAddr());
            tags.put("操作人员","");
            logEvent.setTags(tags);
            jobLogService.sendLog(jobDO.getId(),GsonUtils.gsonString(logEvent),"append", 0);

            return Response.success();
        } catch (ApiException e) {
            e.printStackTrace();
            jobLogService.sendLog(jobId,"取消异常："+e.getMessage(),"append", 1);
            return Response.error(CodeEnum.JOB_CANCEL_ERROR);
        }
    }

    /**
     * 作业概览
     * @param jobId
     * @return
     */
    @Override
    public Response overview(Long jobId) {
        String result=flinkApiService.getJobOverview(jobId);
        JsonArray jsonArray=JsonParser.parseString(result).getAsJsonArray();
        return Response.success(jsonArray);
    }

    /**
     * 更新动作
     * @param jobVO 更新信息
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Response updateJob(JobVO jobVO) {

        Long id=jobVO.getId();
        if(id==null){
            return Response.error(CodeEnum.PARAM_ERROR);
        }
        TJob tJob=getById(id);
        if(tJob==null){
            return Response.error(CodeEnum.JOB_NOT_FOUND);
        }

        BeanUtils.copyProperties(jobVO,tJob);

        updateById(tJob);

        //若是SQL作业，需要更新数据源映射关系
        if(JobTypeEnum.SQL.getType() == jobVO.getJobType().intValue()){

            //先删除旧的数据源映射
            QueryWrapper<TJobDs> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("job_id",id);
            jobDsService.remove(queryWrapper);

            QueryWrapper<TJobDsSink> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("job_id",id);
            jobDsSinkService.remove(queryWrapper2);

            List<JobDsVO> jobDsVOS = jobVO.getJobDsVOS();

            //保存源表和维表映射
            List<TJobDs> jobDsList = jobDsVOS.stream().filter(jobDsVO -> {
                if(!DsTypeEnum.SINK.getType().equals(jobDsVO.getDsType())){
                    return true;
                }
                return false;
            }).collect(Collectors.toList())
                    .stream().map(jobDsVO -> {
                        TJobDs jobDs = new TJobDs();
                        BeanUtils.copyProperties(jobDsVO,jobDs);
                        jobDs.setJobId(tJob.getId());

                        return jobDs;
                    }).collect(Collectors.toList());

            jobDsService.saveBatch(jobDsList);

            //保存目标表映射
            List<TJobDsSink> jobDsSinkList = jobDsVOS.stream().filter(jobDsVO -> {
                if(DsTypeEnum.SINK.getType().equals(jobDsVO.getDsType())){
                    return true;
                }
                return false;
            }).collect(Collectors.toList())
                    .stream().map(jobDsVO -> {
                        TJobDsSink jobDsSink = new TJobDsSink();
                        BeanUtils.copyProperties(jobDsVO,jobDsSink);
                        jobDsSink.setJobId(tJob.getId());

                        return jobDsSink;
                    }).collect(Collectors.toList());

            jobDsSinkService.saveBatch(jobDsSinkList);

        }

        return Response.success();
    }

    /**
     * 获取明细
     * @param jobId
     * @return
     */
    @Override
    public Response<JobVO> getJobByID(Long jobId) {

        TJob job = this.getById(jobId);

        if(null == job){
           return Response.error(404,"未找到作业信息");
        }

        JobVO jobVO = new JobVO();
        BeanUtils.copyProperties(job,jobVO);

        //如果是SQL作业，需要拼接数据源映射
        if(JobTypeEnum.SQL.getType() == job.getJobType().intValue()){
            QueryWrapper<TJobDs> queryWrapper = new QueryWrapper<TJobDs>();
            queryWrapper.eq("job_id",jobId);
            List<TJobDs> jobDsList = jobDsService.list(queryWrapper);

            QueryWrapper<TJobDsSink> queryWrapper2 = new QueryWrapper<TJobDsSink>();
            queryWrapper2.eq("job_id",jobId);
            List<TJobDsSink> jobDsSinkList = jobDsSinkService.list(queryWrapper2);

           List<JobDsVO> jobDsVOS = jobDsList.stream().map(tJobDs -> {
                JobDsVO jobDsVO = new JobDsVO();
                BeanUtils.copyProperties(tJobDs,jobDsVO);
                return jobDsVO;
            }).collect(Collectors.toList());

            List<JobDsVO> jobDsVOS2 = jobDsSinkList.stream().map(tJobDs -> {
                JobDsVO jobDsVO = new JobDsVO();
                BeanUtils.copyProperties(tJobDs,jobDsVO);
                jobDsVO.setDsType(DsTypeEnum.SINK.getType());
                return jobDsVO;
            }).collect(Collectors.toList());

            jobDsVOS.addAll(jobDsVOS2);

            jobVO.setJobDsVOS(jobDsVOS);
        }

        return Response.success(jobVO);
    }

    /**
     * 重启
     * @param jobId
     * @return
     */
    @Override
    public Response restart(Long jobId) {

        Response res = this.cancel(jobId);
        if(res.isSuccess()){
            res = this.run(jobId);
        }
        return res;
    }

    /**
     * 生成保存点
     * @param jobId
     * @return
     */
    @Override
    public Response triggerSavePoint(Long jobId) {
        TJob jobDO = this.getById(jobId);
        if(jobDO==null){
            return Response.error(CodeEnum.JOB_NOT_FOUND);
        }
        if(!JobStatusEnum.RUNNING.getStatus().equals(jobDO.getJobStatus())){
            return Response.error(CodeEnum.JOB_NOT_RUNNING_CANNOT_TRIGGER_SAVEPOINT);
        }
        if(StringUtils.isBlank(jobDO.getFlinkJobId())){
            return Response.error(CodeEnum.FLINK_JOB_ID_IS_EMPTY);
        }
        TCluster tCluster=clusterService.getById(jobDO.getClusterId());
        String address=tCluster.getAddress();
        //获取保存点路径
        String savepointRootPath=jobDO.getStateSavepointsDir();
        if(StringUtils.isBlank(savepointRootPath)){
            //获取集群保存点路径配置
            Response<Map<String,Object>> configMapResponse=flinkApiService.getJobManagerConfig(address);
            if(!configMapResponse.isSuccess()){
                return configMapResponse;
            }
            Map<String,Object> configMap=configMapResponse.getData();
            //如果存在保存点路径配置，则触发作业保存点,更新作业信息
            if(configMap!=null && configMap.get("state.savepoints.dir")!=null) {
                savepointRootPath = (String) configMap.get("state.savepoints.dir");
            }
        }
        if(StringUtils.isBlank(savepointRootPath)){
            return Response.error(CodeEnum.SAVE_POINT_ROOT_PATH_IS_EMPTY);
        }

        //触发保存点操作，返回保存点完整路径
        Response response=flinkApiService.triggerSavepoint(jobDO.getFlinkJobId(),savepointRootPath,address);
        if(!response.isSuccess()){
            jobLogService.sendLog(jobId,GsonUtils.gsonString(response),"append", 1);
            return response;
        }
        String savepointPath= (String) response.getData();

        //清理历史保存点信息
        String savepointPathOrigin=jobDO.getSavepointPath();
        flinkApiService.triggerSavepointDisposal(savepointPathOrigin,address);

        //更新保存点信息
        jobDO.setSavepointPath(savepointPath);
        jobDO.setModifyTime(new Date());
        updateById(jobDO);

        jobLogService.sendLog(jobId,"更新保存点信息，保存点完整路径："+savepointPath,"append", 1);
        return Response.success();
    }

    /**
     * 获取日志信息
     * @param jobId
     * @return
     */
    @Override
    public Response getLog(Long jobId) {
        TJob job = this.getById(jobId);

        if(null == job){
            return Response.error(404,"未获取到该作业信息！");
        }

        //获取flink api 异常信息
        Response<JobExceptionsInfo> jobExceptionsInfoResponse = null;
        if(job.getClusterId() > 0 && StrUtil.isNotEmpty(job.getFlinkJobId())){
            TCluster cluster = clusterService.getById(job.getClusterId());
            try {
                jobExceptionsInfoResponse = flinkApiService.getJobExceptions(cluster.getAddress(),job.getFlinkJobId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //获取DB中保存的日志
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("job_id",jobId);
        TJobLog jobLog = jobLogService.getOne(queryWrapper);

        StringBuffer sb = new StringBuffer();
        if(null != jobLog){
//            sb.append("\n启动日志：\n");
            sb.append(jobLog.getJobLog());
        }
        if(null != jobExceptionsInfoResponse && jobExceptionsInfoResponse.isSuccess()){
            sb.append("\n ============================= \n Flink集群异常信息：");
            JobExceptionsInfo jobExceptionsInfo = jobExceptionsInfoResponse.getData();
            String rootEx =  jobExceptionsInfo.getRootException();
            List<ExecutionExceptionInfo> executionExceptionInfos = jobExceptionsInfo.getAllExceptions();

            if(StrUtil.isNotEmpty(rootEx)){
                sb.append("\n RootException：\n").append(rootEx);
            }else{
                sb.append("No Root Exception");
            }
            if(executionExceptionInfos.size() > 0){
                sb.append("executionExceptionInfos：\n");
            }
            for (ExecutionExceptionInfo e: executionExceptionInfos) {
                sb.append(GsonUtils.gsonString(e));
            }
        }

        return Response.success(sb.toString());
    }

}
