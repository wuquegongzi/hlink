package com.haibao.admin.web.service;

import com.nextbreakpoint.flinkclient.api.ApiException;
import com.nextbreakpoint.flinkclient.model.*;
import com.haibao.admin.web.common.result.Response;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @ClassName FlinkApiService
 * @Description Flink rest api 接口
 * @Author ml.c
 * @Date 2020/3/6 7:41 下午
 * @Version 1.0
 */
public interface FlinkApiService {


    Response<ClusterOverviewWithVersion> getOverview(String clusterAddress);

    String runJar(String clusterAddress, String jarId, Boolean allowNrs, String savepointPath, String programArg, String entryClass, Integer parallelism) throws IOException, ApiException;

    String terminateJobWithSavePoint(String flinkJobId, String savePointDir,String address) throws ApiException;

    String getJobOverview(Long jobId);

    Response<String> runSQLJob(Map map) throws IOException, TemplateException, InterruptedException;

    JarUploadResponseBody uploadJar(String address, File file) throws ApiException;

    void deleteJar(String address, String resUname) throws ApiException;

    Response<JobExecutionResultResponseBody> getJobResult(String clusterAddress,String flinkJobId);

    Response<JobPlanInfo> getJobPlan(String clusterAddress,String flinkJobId);

    Response<JobExceptionsInfo> getJobExceptions(String clusterAddress,String flinkJobId);

    Response<JobDetailsInfo> getJobDetails(String clusterAddress,String flinkJobId);

    void asyncGetJobSavepointStatus(Long jobId, String triggerId, String address);

    Response<Map<String,Object>> getJobManagerConfig(String address);

    void terminateJob(String flinkJobId, String address) throws ApiException;

    Response triggerSavepoint(String flinkJobId, String savepointRootPath, String address);

    Response triggerSavepointDisposal(String savepointPathOrigin, String address);


    Response<JarListInfo> getJarLists(String clusterAddress);


    /**
     * 获取manager管理信息
     * @return
     * */
//    Response getManagerConfig();
}
