package com.haibao.admin.web.service.impl;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.haibao.admin.config.flink.FlinkApiClient;
import com.haibao.admin.utils.ExecUtils;
import com.haibao.admin.utils.FlinkUtils;
import com.haibao.admin.utils.HttpUtil;
import com.haibao.admin.web.service.JobLogService;
import com.haibao.admin.web.service.JobService;
import com.haibao.flink.log.LogEvent;
import com.haibao.flink.log.LogLevelEnum;
import com.haibao.flink.utils.GsonUtils;
import com.nextbreakpoint.flinkclient.api.ApiException;
import com.nextbreakpoint.flinkclient.api.FlinkApi;
import com.nextbreakpoint.flinkclient.model.*;
import com.haibao.admin.web.common.enums.CodeEnum;
import com.haibao.admin.web.common.enums.JobStatusEnum;
import com.haibao.admin.web.common.result.Response;
import com.haibao.admin.web.entity.TJob;
import com.haibao.admin.web.service.FlinkApiService;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * @ClassName
 * @Description
 * @Author ml.c
 * @Date 2020/3/6 7:42 下午
 * @Version 1.0
 */
@Service("flinkApiService")
public class FlinkApiServiceImpl implements FlinkApiService {

    private Logger logger = LoggerFactory.getLogger(FlinkApiServiceImpl.class);

    @Autowired
    JobService jobService;
    @Autowired
    JobLogService jobLogService;

    @Autowired
    FlinkApiClient flinkApiClient;

    @Autowired
    private HttpUtil httpUtil;

    @Autowired
    private FreeMarkerConfigurer configurer;

    @Autowired
    HttpServletRequest request;

    //提交SQL 作业专用，不要动哦
    @Value("${flink.client.dir}")
    private String flinkClientDir;
    @Value("${flink.shell.datamode}")
    private String dataMode;
    @Value("${flink.shell.jarpath}")
    private String jarPath;

    /**
     * zhengce
     * 集群总览
     * @param clusterAddress
     * @return
     */
    @Override
    public Response<ClusterOverviewWithVersion> getOverview(String clusterAddress) {

        ClusterOverviewWithVersion clusterOverviewWithVersion;

        try {
            FlinkApi flinkApi = flinkApiClient.getFlinkApi(clusterAddress);
            clusterOverviewWithVersion = flinkApi.getOverview();

        } catch (ApiException e) {
            logger.error(e.getMessage(),e);
            return Response.error(500,e.getMessage());
        }

        return Response.success(clusterOverviewWithVersion);
    }

    /**
     *  baoyu
     * jar运行
     * @param clusterAddress
     * @param jarId
     * @param allowNrs
     * @param savepointPath
     * @param programArg
     * @param entryClass
     * @param parallelism
     * @return
     * @throws IOException
     * @throws ApiException
     */
    @Override
    public String runJar(String clusterAddress, String jarId, Boolean allowNrs, String savepointPath, String programArg, String entryClass, Integer parallelism) throws IOException, ApiException {
        Map<String,Object> map=new HashMap<>(5);
        if(StringUtils.isBlank(clusterAddress)){
            throw new ApiException("Missing the required parameter \'clusterAddress\' when calling runJar(Async)");
        }
        if(StringUtils.isEmpty(jarId)){
            throw new ApiException("Missing the required parameter \'jarid\' when calling runJar(Async)");
        }
        if(allowNrs!=null){
            map.put("allowNonRestoredState",allowNrs);
        }
        if(StringUtils.isNotBlank(savepointPath) && !"null".equals(savepointPath)){
            map.put("savepointPath",savepointPath);
        }
        if(StringUtils.isNotBlank(programArg)){
            map.put("programArg",programArg);
        }
        if(StringUtils.isNotBlank(entryClass)){
            map.put("entryClass",entryClass);
        }
        if(parallelism!=null){
            map.put("parallelism",parallelism);
        }
        String basePath=clusterAddress;
        if(clusterAddress.lastIndexOf("/")==clusterAddress.length()-1){
            basePath=clusterAddress.substring(0,clusterAddress.lastIndexOf("/"));
        }
        String url=basePath+"/jars/"+jarId+"/run";
        String result = httpUtil.postjson(url, GsonUtils.gsonString(map));
        return result;
    }

    /**
     * baoyu
     * 触发作业保存点，并取消作业
     * @param flinkJobId
     * @param address
     * @return triggerid 用于查询任务停止状态情况
     * @throws ApiException
     */
    @Override
    public String terminateJobWithSavePoint(String flinkJobId,String savePointDir, String address) throws ApiException {
        FlinkApi flinkApi = flinkApiClient.getFlinkApi(address);
        //触发保存点，并进行任务取消
        SavepointTriggerRequestBody savepointTriggerRequestBody=new SavepointTriggerRequestBody();
        savepointTriggerRequestBody.setCancelJob(true);
        savepointTriggerRequestBody.setTargetDirectory(savePointDir);
        TriggerResponse triggerResponse=flinkApi.createJobSavepoint(savepointTriggerRequestBody,flinkJobId);
        String requestId=triggerResponse.getRequestId();
        logger.info("async triggers a savepoint, and cancels the job,flinkJobId：{},response triggerid:{}",flinkJobId,requestId);
        return requestId;
    }
    /**
     * baoyu.xie
     * @param jobId 本地mysql 作业id
     * @param triggerId 动作查询标识
     * */
    @Override
    public void asyncGetJobSavepointStatus(Long jobId, String triggerId, String address) {
        CompletableFuture<Response<String>> future = CompletableFuture.supplyAsync(() -> {
            TJob job=jobService.getById(jobId);
            String flinkJobId=job.getFlinkJobId();
            FlinkApi flinkApi = flinkApiClient.getFlinkApi(address);
            AsynchronousOperationResult result= null;
            logger.info("异步获取保存点触发结果信息,开始检测,flinkJobId:{}",flinkJobId);
            jobLogService.sendLog(jobId,"异步获取保存点触发结果信息,开始检测,flinkJobId:"+flinkJobId,"append", 1);
            try {
                long times = 0L;
                while (true){
                    result = flinkApi.getJobSavepointStatus(flinkJobId,triggerId);
                    QueueStatus status =result.getStatus();
                    if(status.getId()== QueueStatus.IdEnum.COMPLETED){
                        break;
                    }
                    Thread.sleep(1000);
                    if(times%1000==0){
                        logger.info("异步获取保存点触发结果信息,flinkJobId:{},检索次数{},未检索到保存点触发结果信息",flinkJobId,times);
                    }
                }
                logger.info("异步获取保存点触发结果信息,响应结束,flinkJobId:{},result:{}",flinkJobId,result.toString());
                jobLogService.sendLog(jobId,"异步获取保存点触发结果信息,响应结束,flinkJobId:"+flinkJobId+",result:"+result.toString(),"append", 1);

                //清理原保存点
                triggerSavepointDisposal(job.getSavepointPath(),address);
                Map<String,Object> map= (Map<String, Object>) result.getOperation();
                String localtion=String.valueOf(map.get("location"));
                return Response.success(localtion);
            } catch (ApiException e) {
                e.printStackTrace();
                logger.info("异步获取保存点触发结果信息,响应异常,flinkJobId:{},error:{}",flinkJobId,e.getMessage());
                jobLogService.sendLog(jobId,"异步获取保存点触发结果信息,响应异常,flinkJobId:"+flinkJobId+",error:"+e.getMessage(),"append", 1);

            } catch (InterruptedException e) {
                e.printStackTrace();
                logger.info("异步获取保存点触发结果信息,响应异常,flinkJobId:{},error:{}",flinkJobId,e.getMessage());
                jobLogService.sendLog(jobId,"异步获取保存点触发结果信息,响应异常,flinkJobId:"+flinkJobId+",error:"+e.getMessage(),"append", 1);
            }
            return Response.error(CodeEnum.GET_SAVE_POINT_STATUS_ERROR);
        });

        //回调处理
        future.whenComplete((result, exception) -> {
            TJob job=jobService.getById(jobId);
            job.setModifyTime(new Date());
            int jobStatus = JobStatusEnum.FAILED.getStatus();
            String savepoint="";
            if (result!=null && result.isSuccess()) {
                savepoint=result.getData();
                jobStatus=JobStatusEnum.CANCELED.getStatus();
                job.setSavepointPath(savepoint);
            }
            job.setJobStatus(jobStatus);
            jobService.updateById(job);

            jobLogService.sendLog(jobId,"异步回调，保存点地址："+savepoint+"。更新状态为："+jobStatus,"append", 1);
        });
    }
    /**
     * baoyu.xie
     * @return jobmanager配置信息
     * */
    @Override
    public Response<Map<String,Object>> getJobManagerConfig(String address) {
        String basePath=address;
        if(address.lastIndexOf("/")==address.length()-1){
            basePath=address.substring(0,address.lastIndexOf("/"));
        }
        String url=basePath+"/jobmanager/config";
        try {
            String result = httpUtil.get(url);
            List<Map<String,Object>> configList=GsonUtils.gsonToListMaps(result);
            Map<String,Object> map=new HashMap<>();
            if(CollectionUtils.isNotEmpty(configList)){
                for(Map<String,Object> config:configList){
                    map.put(String.valueOf(config.get("key")),config.get("value"));
                }
            }
            return Response.success(map);
        } catch (IOException e) {
            e.printStackTrace();
            return Response.error(CodeEnum.GET_FLINK_JOB_MANAGER_ERROR);
        }
    }

    @Override
    public void terminateJob(String flinkJobId, String address) throws ApiException {
        FlinkApi flinkApi = flinkApiClient.getFlinkApi(address);
        flinkApi.terminateJob(flinkJobId,"cancel");
    }

    /**
     * 触发作业保存点操作
     * 返回保存点信息
     * */
    @Override
    public Response triggerSavepoint(String flinkJobId, String savepointRootPath, String address) {
        logger.info("触发作业保存点开始[flinkjobId:{},savepointRootPath:{}]",flinkJobId,savepointRootPath);
        FlinkApi flinkApi = flinkApiClient.getFlinkApi(address);
        SavepointTriggerRequestBody triggerRequestBody=new SavepointTriggerRequestBody();
        triggerRequestBody.cancelJob(false);
        triggerRequestBody.targetDirectory(savepointRootPath);
        TriggerResponse triggerResponse= null;
        try {
            triggerResponse = flinkApi.createJobSavepoint(triggerRequestBody,flinkJobId);
            String triggerId=triggerResponse.getRequestId();
            AsynchronousOperationResult result=null;
            while (true){
                result = flinkApi.getJobSavepointStatus(flinkJobId,triggerId);
                QueueStatus status =result.getStatus();
                if(status.getId()== QueueStatus.IdEnum.COMPLETED){
                    break;
                }
                Thread.sleep(100);
            }
            Map<String,Object> map= (Map<String, Object>) result.getOperation();
            String localtion=String.valueOf(map.get("location"));
            logger.info("触发作业保存点结束[flinkjobId:{},保存点位置savepointPath:{}]",flinkJobId,savepointRootPath,localtion);
            if(StringUtils.isBlank(localtion)){
                return Response.error(CodeEnum.GET_SAVE_POINT_PATH_EMPTY);
            }
            return Response.success(localtion);
        } catch (Exception e) {
            logger.info("触发作业保存点异常[flinkjobId:{}",flinkJobId);
            e.printStackTrace();
            return Response.error(CodeEnum.GET_SAVE_POINT_STATUS_ERROR);
        }
    }
    /**
     * 历史保存点信息清理
     * @param savepointPathOrigin 历史保存点位置
     * @address 地址
     * */
    @Override
    public Response triggerSavepointDisposal(String savepointPathOrigin, String address) {
        if(StringUtils.isBlank(savepointPathOrigin)){
            return Response.success();
        }
        logger.info("触发保存点路径清理开始[savepointPath:{}]",savepointPathOrigin);
        FlinkApi flinkApi = flinkApiClient.getFlinkApi(address);
        SavepointDisposalRequest request=new SavepointDisposalRequest();
        request.setSavepointPath(savepointPathOrigin);
        try {
            flinkApi.triggerSavepointDisposal(request);
            logger.info("触发保存点路径清理结束[savepointPath:{}]",savepointPathOrigin);
            return Response.success();
        } catch (ApiException e) {
            e.printStackTrace();
            logger.info("触发保存点路径清理异常[savepointPath:{}]",savepointPathOrigin);
            return Response.error(CodeEnum.SAVE_POINT_DISPOSAL_ERROR);
        }
    }

    /**
     * 获取单个作业的视图 todo
     * @param jobId
     * @return
     */
    @Override
    public String getJobOverview(Long jobId) {
        return null;
    }

    @Override
    public Response runSQLJob(Map parmMap) {

        String clusterAddress = (String)parmMap.get("clusterAddress");
        Long  jobID = (Long)parmMap.get("jobID");
        Integer allowNrs = (Integer)parmMap.get("allowNrs");
        String profilesActive = (String)parmMap.get("profilesActive");

        Map map = new HashMap(10);
        map.put("FLINK_CLUSTER", FlinkUtils.getIPAndPort(clusterAddress));
        map.put("JobID",jobID);
        map.put("FLINK_DIR",flinkClientDir);
        //后面优化枚举 todo
        map.put("FLINK_ACTION","run");
        map.put("JAR_PATH",jarPath);
        map.put("DATA_MODE",dataMode);
        map.put("ALLOWNRS",allowNrs);
        map.put("PROFILES_ACTIVE",profilesActive);

        if(null != parmMap.get("savepointPath")
                && StrUtil.isNotEmpty((String)parmMap.get("savepointPath"))
        && !"null".equals((String)parmMap.get("savepointPath"))){
            map.put("SAVEPOINT_PATH",parmMap.get("savepointPath"));
        }
        if(null != parmMap.get("parallelism")){
            map.put("PARALLELISM",parmMap.get("parallelism"));
        }

        //是否需要自动计算最大并行度 暂时先设置一个默认值 todo
        map.put("MaxParallelism",50);

        //===================log======================
        Map logMap = Maps.newLinkedHashMap();
        logMap.put("入参",map);
        LogEvent logEvent = new LogEvent();
        logEvent.setTimestamp(System.currentTimeMillis());
        logEvent.setMessage(GsonUtils.gsonString(logMap));
        logEvent.setType("SQL作业异步启动");
        logEvent.setLevel(LogLevelEnum.INFO.name());

        Map tags = Maps.newLinkedHashMap();
        try {
            tags.put("URL",request.getRequestURI());
            tags.put("IP",request.getRemoteAddr());
            tags.put("操作人员","");
        } catch (Exception e) {
            e.printStackTrace();
        }

        logEvent.setTags(tags);

        jobLogService.sendLog(jobID,GsonUtils.gsonString(logEvent),"up", 0);
       //===================log======================

        String ftlPath = "job_sh/standlone_submit.ftl";

        String commandStr = null;
        try {
            Template template = configurer.getConfiguration().getTemplate(ftlPath);
            commandStr = FreeMarkerTemplateUtils.processTemplateIntoString(template,map);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
            jobLogService.sendLog(jobID,"异常："+e.getMessage(),"append", 1);
        }

        if(StrUtil.isBlank(commandStr)){
            return Response.error(JobStatusEnum.FAILED.getStatus(),JobStatusEnum.FAILED.getDesc());
        }

        //异步提交cmd
        runSQLJobAsync(commandStr,clusterAddress,jobID);

        return Response.success();
    }

    /**
     * 异步 cmd
     * @param commandStr
     * @param clusterAddress
     * @param jobID
     */
    private void runSQLJobAsync(String commandStr, String clusterAddress, Long jobID){

        CompletableFuture<Response<String>> future = CompletableFuture.supplyAsync(() -> {

            ExecUtils execUtils = new ExecUtils();
            Response<String> response = null;

            try {
                jobLogService.sendLog(jobID,"=============提交Flink SQL 作业=============\n"+commandStr+"\n","append",1);
                response = execUtils.executeFlinkSQLJob(commandStr);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return response;
        });

        //回调处理
        future.whenComplete((result, exception) -> {

            String flinkJobId = "";
            String error = "";
            int jobStatus = JobStatusEnum.FAILED.getStatus();

            if (null == exception) {
                logger.info("异步执行SQL任务结果:{} ",result);
                jobLogService.sendLog(jobID,"异步执行SQL任务结果: " + result,"append", 1);

                Response<String> response = result;
                if(response.isSuccess()){
                    //二次确认任务状况
                    flinkJobId = response.getData();
                    //获取作业运行结果
                    Response<JobExecutionResultResponseBody> resultResponseBodyResponse =
                            getJobResult(clusterAddress,flinkJobId);
                    if(resultResponseBodyResponse.isSuccess()){
                        JobExecutionResultResponseBody jobExecutionResultResponseBody =
                                resultResponseBodyResponse.getData();
                        if(QueueStatus.IdEnum.IN_PROGRESS == jobExecutionResultResponseBody.getStatus().getId()){
                            jobStatus = JobStatusEnum.RUNNING.getStatus();
                        }else if(QueueStatus.IdEnum.COMPLETED == jobExecutionResultResponseBody.getStatus().getId()){
                            jobStatus = JobStatusEnum.FINISHED.getStatus();
                        }else{
                            error = GsonUtils.gsonString(jobExecutionResultResponseBody.getJobExecutionResult());
                        }
                    }
                }else{
                    error = response.getData();
                }
            }else{
                //启动失败！
                error = exception.getMessage();
            }

            if(StrUtil.isNotEmpty(error)){
                logger.error("作业{}提交运行异常：{}",jobID,error);
                jobLogService.sendLog(jobID,"运行异常："+error,"append", 1);
            }

            TJob tJob = new TJob();
            tJob.setId(jobID);
            tJob.setFlinkJobId(flinkJobId);
            tJob.setJobStatus(jobStatus);
            jobService.updateById(tJob);
        });
    }


    @Override
    public JarUploadResponseBody uploadJar(String address, File file) throws ApiException {

        FlinkApi flinkApi = flinkApiClient.getFlinkApi(address);
        return flinkApi.uploadJar(file);
    }

    @Override
    public void deleteJar(String address, String resUname) throws ApiException {
        FlinkApi flinkApi = flinkApiClient.getFlinkApi(address);
        flinkApi.deleteJar(resUname);
    }

    @Override
    public Response<JobExecutionResultResponseBody> getJobResult(String clusterAddress, String flinkJobId) {
        FlinkApi flinkApi = flinkApiClient.getFlinkApi(clusterAddress);
        JobExecutionResultResponseBody obj = null;
        try {
            obj = flinkApi.getJobResult(flinkJobId);
//            logger.info("获取"+flinkJobId+",JobExecutionResultResponseBody:"+ obj);
            return Response.success(obj);
        } catch (ApiException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return Response.error(e.getCode(),e.getMessage());
        }
    }

    @Override
    public Response<JobPlanInfo> getJobPlan(String clusterAddress, String flinkJobId) {
        FlinkApi flinkApi = flinkApiClient.getFlinkApi(clusterAddress);
        JobPlanInfo obj = null;
        try {
            obj = flinkApi.getJobPlan(flinkJobId);
//            logger.info("获取"+flinkJobId+",JobPlanInfo:"+ obj);
            return Response.success(obj);
        } catch (ApiException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return Response.error(e.getCode(),e.getMessage());
        }
    }

    @Override
    public Response<JobExceptionsInfo> getJobExceptions(String clusterAddress, String flinkJobId) {
        FlinkApi flinkApi = flinkApiClient.getFlinkApi(clusterAddress);
        JobExceptionsInfo jobExceptions = null;
        try {
            jobExceptions = flinkApi.getJobExceptions(flinkJobId);

//            logger.info("获取"+flinkJobId+"JobExceptionsInfo:"+ jobExceptions);
            if(null == jobExceptions){
               return Response.success();
            }
            return Response.success(jobExceptions);
        } catch (ApiException e) {
//            e.printStackTrace();
            logger.error(e.getMessage());
            return Response.error(e.getCode(),e.getMessage());
        }
    }

    @Override
    public Response<JobDetailsInfo> getJobDetails(String clusterAddress, String flinkJobId) {

        FlinkApi flinkApi = flinkApiClient.getFlinkApi(clusterAddress);
        JobDetailsInfo jobDetailsInfo = null;
        try {
            jobDetailsInfo = flinkApi.getJobDetails(flinkJobId);
            logger.info("JobDetailsInfo:"+ jobDetailsInfo.getJid());
            return Response.success(jobDetailsInfo);
        } catch (ApiException e) {
//            e.printStackTrace();
//            logger.error(e.getMessage());
            return Response.error(e.getCode(),e.getMessage());
        }
    }

    @Override
    public Response<JarListInfo> getJarLists(String clusterAddress) {

        FlinkApi flinkApi = flinkApiClient.getFlinkApi(clusterAddress);
        try {
            JarListInfo jarListInfo = flinkApi.listJars();
//            logger.info("JarListInfo:"+ jarListInfo);
            return Response.success(jarListInfo);
        } catch (ApiException e) {
            logger.error(e.getMessage());
            return Response.error(e.getCode(),e.getMessage());
        }
    }
}
